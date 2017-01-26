package com.exam.util;

import com.exam.connection_pool.ConnectionPool;
import lombok.extern.log4j.Log4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Log4j
public abstract class DataScriptExecutor {

    public static void initSqlData(String pathToInitSQL, ConnectionPool connectionPool) {
        File file = new File(pathToInitSQL);
        String currentLine;
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while ((currentLine = reader.readLine()) != null) {
                builder.append(currentLine);
            }
        } catch (IOException e) {
            log.fatal("Reading sql script file error", e);
        }
        String[] initState = builder.toString().split(";");

        try (Connection connection = connectionPool.takeConnection();
             Statement statement = connection.createStatement()) {
            for (String state : initState) {
                statement.executeUpdate(state);
            }
        } catch (SQLException e) {
            log.fatal("Init SQL script error", e);
        }
    }
}
