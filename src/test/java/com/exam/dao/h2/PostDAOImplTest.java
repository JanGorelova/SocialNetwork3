package com.exam.dao.h2;

import com.exam.connection_pool.ConnectionPool;
import com.exam.dao.PostDAO;
import com.exam.models.Post;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by Vasiliy Bobkov on 21.03.2017.
 */
public class PostDAOImplTest {
    private static PostDAO postDAO;

    @BeforeClass
    public static void setUp() throws Exception {
        ConnectionPool.create("src/main/resources/db.properties");
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        connectionPool.initPoolData();
        connectionPool.executeScript("src/main/resources/usersH2Init.sql");
        postDAO = new PostDAOImpl(connectionPool);
    }

    @Test
    public void create() throws Exception {
        String textMessage = "Test post for create method";
        Post post = Post.builder()
                .message(textMessage)
                .time(Instant.now())
                .sender(3L)
                .recipient(2L)
                .build();
        postDAO.create(post);
        List<Post> list = postDAO.getByRecipient(2L, 0, 10);
        boolean isExist = list.stream()
                .map(Post::getMessage)
                .anyMatch(s -> s.equals(textMessage));
        assertTrue(isExist);
    }

    @Test
    public void read() throws Exception {
        String textMessage = "Test post for read method";
        Post post = Post.builder()
                .message(textMessage)
                .time(Instant.now())
                .sender(3L)
                .recipient(2L)
                .build();
        postDAO.create(post);
        List<Post> list = postDAO.getByRecipient(2L, 0, 10);
        Optional<Post> optional = list.stream()
                .filter(s -> s.getMessage().equals(textMessage))
                .findFirst();
        assertTrue(optional.isPresent());
        Post postFromDB = postDAO.read(optional.get().getId()).get();
        assertThat(textMessage, is(postFromDB.getMessage()));
        assertThat(post.getRecipient(), is(postFromDB.getRecipient()));
        assertThat(post.getSender(), is(postFromDB.getSender()));
        assertThat(post.getTime(), is(postFromDB.getTime()));
    }

    @Test
    public void update() throws Exception {
        String textMessage = "Test post for update method";
        Post post = Post.builder()
                .message(textMessage)
                .time(Instant.now())
                .sender(3L)
                .recipient(2L)
                .build();
        postDAO.create(post);
        List<Post> list = postDAO.getByRecipient(2L, 0, 10);
        Optional<Post> optional = list.stream()
                .filter(s -> s.getMessage().equals(textMessage))
                .findFirst();
        assertTrue(optional.isPresent());
        Post postFromDB = postDAO.read(optional.get().getId()).get();
        String newMessage = "New message for update method";
        Instant newTime = Instant.now().minus(30, ChronoUnit.SECONDS);
        Post updatedPost = Post.builder()
                .id(postFromDB.getId())
                .message(newMessage)
                .sender(post.getSender())
                .recipient(post.getRecipient())
                .time(newTime)
                .build();
        postDAO.update(updatedPost);
        Post updatedPostFromDB = postDAO.read(updatedPost.getId()).get();
        assertThat(updatedPost, is(updatedPostFromDB));
    }

    @Test
    public void delete() throws Exception {
        String textMessage = "Test post for delete method";
        Post post = Post.builder()
                .message(textMessage)
                .time(Instant.now())
                .sender(3L)
                .recipient(2L)
                .build();
        postDAO.create(post);
        List<Post> list = postDAO.getByRecipient(2L, 0, 10);
        Optional<Post> optional = list.stream()
                .filter(s -> s.getMessage().equals(textMessage))
                .findFirst();
        optional.map(Post::getId).ifPresent(postDAO::delete);
        list = postDAO.getByRecipient(2L, 0, 10);
        optional = list.stream()
                .filter(s -> s.getMessage().equals(textMessage))
                .findFirst();
        assertFalse(optional.isPresent());
    }

    @Test
    public void getByRecipient() throws Exception {
        String textMessage = "Test post for getByRecipient method";
        Post post = Post.builder()
                .message(textMessage)
                .time(Instant.now())
                .sender(3L)
                .recipient(2L)
                .build();
        postDAO.create(post);
        List<Post> list = postDAO.getByRecipient(2L, 0, 10);
        boolean isExist = list.stream()
                .map(Post::getMessage)
                .anyMatch(s -> s.equals(textMessage));
        assertTrue(isExist);
    }

    @Test
    public void getRows() throws Exception {
        List<Post> list = postDAO.getByRecipient(1L, 0, 10000);
        long rows = postDAO.getRows(1L);
        assertTrue(list.size() == rows);
    }
}