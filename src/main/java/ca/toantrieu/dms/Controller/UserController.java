package ca.toantrieu.dms.Controller;

import ca.toantrieu.dms.Model.*;
import ca.toantrieu.dms.Repository.RoleRepository;
import ca.toantrieu.dms.Repository.UserRepository;
import ca.toantrieu.dms.Service.CustomUserDetailsServiceImpl;
import ca.toantrieu.dms.Service.CustomUserService;
import ca.toantrieu.dms.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${jwt.http.request.header}")
    private String tokenHeader;

    @Autowired
    private BCryptPasswordEncoder bCrypt;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;



    @GetMapping("/")
    public String welcome() {
//        System.out.println("ADMIN IS : " + request.isUserInRole("ROLE_USER"));
//        System.out.println("ADMIN IS : " + request.isUserInRole("ROLE_ADMIN"));
        return "Welcome to Dine-in Management System";
    }

    @GetMapping("/admin")
//    @Secured("hasRole('ADMIN')")
    public String welcomeAdmin() {
//        System.out.println("ADMIN IS : " + request.isUserInRole("ROLE_ADMIN"));
        return "Welcome to Admin Page";
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
            User user = customUserService.findById(userId);
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
//                String hashedPassword = bCrypt.encode(user.getPassword());
//                User createdUser = new User(user.getUserName(), hashedPassword, user.getEmail());
//                this.userRepository.save(createdUser);
                customUserService.saveUser(user);
                return ResponseEntity.status(HttpStatus.CREATED).body(null);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value="id") long userId){
        try {
            customUserService.deleteUserById(userId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
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
//        User user = (User) customUserDetailsServiceImpl.loadUserByUsername(username);
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
        try {
            if (user.getUserName().equals("")
                    || user.getPassword().equals("")
                    || user.getEmail().equals("")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Field cannot be empty/missing");
            }
            else {
                customUserService.saveUser(user);
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping(URL.LOGIN_URL)
    public ResponseEntity<?> generateToken(@RequestBody AuthorizationRequest authorizationRequest) throws Exception{
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
                    //Get roles set
                    Set<Role> roles = user.getRoles();
                    List<String> res = new ArrayList<>();
                    res.add(token);
                    //Convert to string of roles' name and add to the response
                    for (Role role : roles){
                        res.add(role.getRole());
                    }
//                    return ResponseEntity.status(HttpStatus.OK).body(token);
                    return new ResponseEntity<List<String>>(res, HttpStatus.OK);
                }
                else {
                    System.out.println("Password not matching");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//                    return new ResponseEntity<List<String>>(entities, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new Exception("Invalid username/password");
        }
    }
}