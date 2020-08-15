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
import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)

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
    public List<User> getAllUsers(){
        try {
            List<User> users = this.userRepository.findAll();
            return users;
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


//    @GetMapping("/email")
//    public String getMyEmail() {
//
//    }
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
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<User> register(@RequestBody User user) {
        String hashedPassword = bCrypt.encode(user.getPassword());
        User createdUser = new User(user.getId(), user.getUserName(), hashedPassword, user.getEmail());
        System.out.println(createdUser);
        try {
            if (user.getId() == -1
                    || user.getUserName() == ""
                    || user.getPassword() == ""
                    || user.getEmail() == ""){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            else {
                this.userRepository.save(createdUser);
                return ResponseEntity.status(200).body(createdUser);
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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
                    System.out.println("No match");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new Exception("Invalid username/password");
        }
    }
}