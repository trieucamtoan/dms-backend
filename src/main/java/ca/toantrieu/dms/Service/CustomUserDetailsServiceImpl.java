
package ca.toantrieu.dms.Service;

import ca.toantrieu.dms.Model.Role;
import ca.toantrieu.dms.Model.User;
import ca.toantrieu.dms.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

//Providing user details
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByUserName(username);
        if(user == null){
            throw new UsernameNotFoundException("User Name "+username +"Not Found");
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        //Get all the roles
        for (Role role : user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        System.out.println("Granted Authorities : " + grantedAuthorities);
        return new org.springframework.security.core.userdetails.User(user.getUserName(),
                user.getPassword(), grantedAuthorities);
    }

}