package ca.toantrieu.dms.Service;

import ca.toantrieu.dms.Model.User;

import java.util.List;

public interface CustomUserService {
    User findByUserName(String username);
    User findById(long id);
    void saveUser(User user);

    List<User> findAllUsers();

    void updateUser(User user);

    void updateAdminAccess(User user);

    void deleteUserById(long userId);

}
