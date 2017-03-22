package com.exam.logic.services;

import com.exam.dao.PostDAO;
import com.exam.models.Post;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.util.List;
import java.util.Optional;

@Log4j
@AllArgsConstructor
public class PostService {
    private PostDAO postDAO;

    public List<Post> getPosts(Long id, int offset, int limit) {
        return postDAO.getByRecipient(id, offset, limit);
    }

    public boolean hasNextPage(Long id, int offset, int limit) {
        int sum = offset + limit;
        return postDAO.getRows(id) > sum;
    }

    public void createPost(Post post) {
        postDAO.create(post);
    }

    public Optional<Post> getById(Long postId) {
        return postDAO.read(postId);
    }

    public void deletePost(Long id) {
        postDAO.delete(id);
    }
}