
package ca.toantrieu.dms.Service;

import ca.toantrieu.dms.Model.User;
import ca.toantrieu.dms.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

//Providing user details
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByUserName(username);
        if(user == null){
            throw new UsernameNotFoundException("User Name "+username +"Not Found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(),
                user.getPassword(), new ArrayList<>());
    }

}