package com.exam.logic.services;

import com.exam.dao.RelationDAO;
import com.exam.dao.UserDAO;
import com.exam.models.Relation;
import com.exam.models.User;
import com.exam.servlets.ErrorHandler;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.exam.logic.Constants.NEUTRAL;
import static com.exam.logic.Constants.REQUEST;
import static com.exam.servlets.ErrorHandler.ErrorCode.EMAIL_ALREADY_EXIST;

@AllArgsConstructor
@Log4j
public class UserService {

    private final UserDAO userDAO;
    private final RelationDAO relationDAO;

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

    public List<User> getFriends(Long id, int offset, int limit) {
        List<User> list = new ArrayList<>();
        relationDAO
                .getFriendsID(id, offset, limit)
                .stream()
                .map(userDAO::read)
                .forEach(user -> user.ifPresent(list::add));
        return list;
    }

    public void addFriend(Long sender, Long recipient) {
        relationDAO.addFriend(sender, recipient);
    }

    public List<User> getByName(String name, Integer offset, Integer limit) {
        return userDAO.getByName(name, offset, limit);
    }

    public List<User> getByNames(String firstName, String lastName, Integer offset, Integer limit) {
        return userDAO.getByNames(firstName, lastName, offset, limit);
    }

    public Relation getRelation(Long id_1, Long id_2) {
        return relationDAO.getBetween(id_1, id_2);
    }

    public void deleteRelation(Long id, Long recipientID) {
        Relation relation = relationDAO.getBetween(id, recipientID);
        relationDAO.delete(relation.getId());
    }

    public void sendRequest(Long id, Long recipientID) {
        Relation relation = relationDAO.getBetween(id, recipientID);
        if (relation.getType() == NEUTRAL) {
            relation = Relation.builder().sender(id).recipient(recipientID).type(REQUEST).build();
            relationDAO.create(relation);
        } else throw new RuntimeException("This Relarion exist (hack attack)");
    }

    public List<User> getIncomings(Long userID, int offset, int limit) {
        return relationDAO
                .getIncomingID(userID, offset, limit)
                .stream()
                .map(userDAO::read)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<User> getRequests(Long userID, int offset, int limit) {
        return relationDAO
                .getRequestID(userID, offset, limit)
                .stream()
                .map(userDAO::read)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}