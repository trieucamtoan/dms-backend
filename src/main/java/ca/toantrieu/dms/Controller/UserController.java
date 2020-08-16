package ca.toantrieu.dms.Controller;

import ca.toantrieu.dms.Model.*;
import ca.toantrieu.dms.Repository.UserRepository;
import ca.toantrieu.dms.Service.CustomUserDetailsService;
import ca.toantrieu.dms.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Value("${jwt.http.request.header}")
    private String tokenHeader;

    private BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();


    @GetMapping("/")
    public String welcome() {
        return "Welcome to Dine-in Management System";
    }

    @GetMapping(URL.GETALLUSER_URL)
    public ResponseEntity<?> getAllUsers(){
        try {
            List<User> users = this.userRepository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot retrieve info of all users");
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable(value = "id") long userId){
        try {
            User user = this.userRepository.findById(userId);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot retrieve user info");
    }
    @PostMapping("/user/add")
    public ResponseEntity<?> createNewUser(@RequestBody User user){
        try {
            if (!user.getUserName().equals("")
                    && !user.getPassword().equals("")
                    && !user.getEmail().equals("")){
                String hashedPassword = bCrypt.encode(user.getPassword());
                User createdUser = new User(user.getUserName(), hashedPassword, user.getEmail());
                this.userRepository.save(createdUser);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value="id") long userId){
        try {
            User user = this.userRepository.findById(userId);
            if (user != null){
                this.userRepository.delete(user);
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }
            else {
                throw new Exception("Unable to find user");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to find and delete user");
    }

    //Refresh old token to a newer one
    @GetMapping(URL.REFRESH_URL)
    public ResponseEntity<?> refreshToken(HttpServletRequest request){
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        String username = jwtUtil.extractUsername(token);
//        User user = (User) customUserDetailsService.loadUserByUsername(username);
        if (jwtUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtTokenResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping(URL.REGISTER_URL)
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> register(@RequestBody User user) {
        String hashedPassword = bCrypt.encode(user.getPassword());
        User createdUser = new User(user.getUserName(), hashedPassword, user.getEmail());
        System.out.println(createdUser);
        try {
            if (user.getUserName().equals("")
                    || user.getPassword().equals("")
                    || user.getEmail().equals("")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Field cannot be empty/missing");
            }
            else {
                this.userRepository.save(createdUser);
                return ResponseEntity.status(HttpStatus.OK).body(createdUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping(URL.LOGIN_URL)
    public ResponseEntity<String> generateToken(@RequestBody AuthorizationRequest authorizationRequest) throws Exception{
        try {
            User user = this.userRepository.findByUserName(authorizationRequest.getUserName());
            if (user == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot generate token");
            }
            else {
                String hashedPassword = user.getPassword();
                String inputPassword = authorizationRequest.getPassword();
                if (bCrypt.matches(inputPassword, hashedPassword)){
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(authorizationRequest.getUserName(),
                                    authorizationRequest.getPassword()));
                    String token = jwtUtil.generateToken(authorizationRequest.getUserName());
                    return ResponseEntity.status(HttpStatus.OK).body(token);
                }
                else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new Exception("Invalid username/password");
        }
    }
}