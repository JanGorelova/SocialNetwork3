package com.exam.logic.services;

import com.exam.dao.UserDAO;
import com.exam.models.User;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.util.Optional;

@AllArgsConstructor
@Log4j
public class UserService {

    private final UserDAO userDAO;

//    public UserService(ServletContext context) {
//        this.userDAO = (UserDAO) context.getAttribute(USER_DAO);
//    }

    public User getById(long id) {
        return null;
        // TODO: 21.01.2017 dopolit'
    }

    public Optional<User> authorize(String login, String password) {

        Optional<User> userOptional = userDAO.getByEmail(login);

        if (userOptional.isPresent() && userOptional.get().
                getPassword().
                equals(password)) return userOptional.map(this::getSafeUser);
        else return Optional.empty();
    }

    private User getSafeUser(User user) {
        return new User(user.getId(),
                user.getEmail(),
                "skipped",
                user.getFirstName(),
                user.getLastName(),
                user.getGender(),
                user.getRole());
    }
}