package ca.toantrieu.dms.Service;

import ca.toantrieu.dms.Model.Role;
import ca.toantrieu.dms.Model.User;
import ca.toantrieu.dms.Repository.RoleRepository;
import ca.toantrieu.dms.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserServiceImpl implements CustomUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void saveUser(User user){
        user.setUsername(user.getUserName());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(user.getRoles());
        Role userRole = roleService.findByRole("USER");
        user.addRoles(userRole);
        user.setEmail(user.getEmail());
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    public User findById(long id) {
        User user = userRepository.findById(id).get();
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach((e -> users.add(e)));
        return users;
    }

    @Override
    public void updateUser(User user) {
        User updateUser = userRepository.findById(user.getId()).orElse(null);
        if (updateUser != null){
            user.setUsername(user.getUserName());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setRoles(user.getRoles());
            user.addRoles(new Role("USER"));
            user.setEmail(user.getEmail());
            user.setEnabled(true);
            userRepository.save(user);
        }
        userRepository.save(updateUser);
    }

    @Override
    public void deleteUserById(long userId) {
        userRepository.delete(findById(userId));
    }

    @Override
    public void updateAdminAccess(User user) {
        Role adminRole = roleService.findByRole("ADMIN");
        user.addRoles(adminRole);
        userRepository.save(user);
    }
}
