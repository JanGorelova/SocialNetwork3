package com.exam.logic.services;

import com.exam.dao.UserDAO;
import com.exam.models.User;
import com.exam.servlets.ErrorHandler;
import com.exam.util.NameNormalizer;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Optional;

import static com.exam.logic.Constants.ERROR_MSG;
import static com.exam.servlets.ErrorHandler.ErrorCode.EMAIL_ALREADY_EXIST;

@AllArgsConstructor
@Log4j
public class UserService {

    private final UserDAO userDAO;

//    public UserService(ServletContext context) {
//        this.userDAO = (UserDAO) context.getAttribute(USER_DAO);
//    }

    public Optional<User> getById(long id) {
        return userDAO.read(id);
    }

    public Optional<User> authorize(String login, String password) {

        Optional<User> userOptional = userDAO.getByEmail(login);

        if (userOptional.isPresent() && userOptional.get().
                getPassword().
                equals(password)) return userOptional.map(this::getSafeUser);
        else return Optional.empty();
    }

    public User getSafeUser(User user) {
        return new User(user.getId(),
                user.getEmail(),
                "",
                user.getFirstName(),
                user.getLastName(),
                user.getGender(),
                user.getRole());
    }

    public ErrorHandler.ErrorCode register(User user) {
        if (userDAO.getByEmail(user.getEmail()).isPresent()) {
            return EMAIL_ALREADY_EXIST;
        } else {
                userDAO.create(user);
                return null;
        }
    }
}