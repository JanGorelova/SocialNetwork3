package com.exam.connection_pool;


import com.exam.util.ExceptionalConsumer;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
@Log4j
public class ConnectionPool {
    @Getter
    private static ConnectionPool instance;
    @Getter
    private BlockingQueue<Connection> connectionQueue;
    @Getter
    private BlockingQueue<Connection> givenAwayConQueue;
    private String driverName;
    private String url;
    private String user;
    private String password;
    private int poolSize;

    private ConnectionPool(String pathToParams) {

        try (FileInputStream fileInputStream = new FileInputStream(pathToParams);
             BufferedInputStream stream = new BufferedInputStream(fileInputStream)) {

            Properties dbProperties = new Properties();
            dbProperties.load(stream);
            this.driverName = dbProperties.getProperty("driver");
            this.url = dbProperties.getProperty("url");
            this.user = dbProperties.getProperty("user");
            this.password = dbProperties.getProperty("password");
            this.poolSize = Integer.parseInt(dbProperties.getProperty("poolSize"));
        } catch (IOException e) {
            log.error(e);
        }
    }

    public static void create(String pathToParams) {
        instance = new ConnectionPool(pathToParams);
    }

    public void initPoolData() throws ConnectionPoolException {
        System.setProperty("file.encoding", "UTF-8");
        try {
            Class.forName(driverName);
            givenAwayConQueue = new ArrayBlockingQueue<>(poolSize);
            connectionQueue = new ArrayBlockingQueue<>(poolSize);
            for (int i = 0; i < poolSize; i++) {
                Connection connection = DriverManager.getConnection(url, user, password);
                PooledConnection pooledConnection = new PooledConnection(connection);
                connectionQueue.add(pooledConnection);
            }
        } catch (SQLException e) {
            throw new ConnectionPoolException("SQLException in connection_pool", e);
        } catch (ClassNotFoundException e) {
            throw new ConnectionPoolException(
                    "Can't find database driver class", e);
        }
    }

    public void dispose() {
        clearConnectionQueue();
    }

    private void clearConnectionQueue() {
        try {
            closeConnectionsQueue(givenAwayConQueue);
            closeConnectionsQueue(connectionQueue);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection takeConnection() throws ConnectionPoolException {
        Connection connection;
        try {
            connection = connectionQueue.take();
            givenAwayConQueue.add(connection);
        } catch (InterruptedException e) {
            throw new ConnectionPoolException(
                    "Error connecting to the data source.", e);
        }
        return connection;
    }

    @SneakyThrows
    public int[] executeScript(String pathToScript) {

        try (Connection connection = takeConnection();
             Statement statement = connection.createStatement()) {

            Arrays.stream(
                    Files.lines(Paths.get(pathToScript))
                            .collect(Collectors.joining())
                            .split(";"))
                    .forEachOrdered(ExceptionalConsumer.toUncheckedConsumer(statement::addBatch));
            return statement.executeBatch();
        }
    }

    private void closeConnectionsQueue(BlockingQueue<Connection> queue)
            throws SQLException {
        Connection connection;
        while ((connection = queue.poll()) != null) {
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
            ((PooledConnection) connection).reallyClose();
        }
    }
}