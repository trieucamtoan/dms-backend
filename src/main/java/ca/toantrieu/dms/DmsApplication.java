package ca.toantrieu.dms;


import ca.toantrieu.dms.Config.SecurityConfig;
import ca.toantrieu.dms.Model.Role;
import ca.toantrieu.dms.Model.User;
import ca.toantrieu.dms.Repository.RoleRepository;
import ca.toantrieu.dms.Repository.UserRepository;
import ca.toantrieu.dms.Service.CustomUserService;
import ca.toantrieu.dms.Service.RoleService;
import ca.toantrieu.dms.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class DmsApplication {
	@Autowired
	private UserRepository repository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private CustomUserService customUserService;
	@Autowired
	private RoleService roleService;

	@Autowired
	private BCryptPasswordEncoder bCrypt;

	public static void main(String[] args) {
		SpringApplication.run(DmsApplication.class, args);
	}

	@PostConstruct
	private void postConstruct() {

		User existingAdmin = customUserService.findByUserName("admin");
		Role userRole = roleService.findByRole("USER");
		Role adminRole = roleService.findByRole("ADMIN");
		if (existingAdmin != null || userRole != null || adminRole != null){
			//Exist admin in the system, avoid creating a new one
		}
		else if (existingAdmin == null && userRole == null || adminRole == null){
			//Initialize roles
			roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("USER"));

			// save Admin account
			User admin = new User("admin", "password", "admin@gmail.com");
			customUserService.saveUser(admin);
			customUserService.updateAdminAccess(admin);
		}
		else {
			System.out.println("Error!!!!");
		}
	}
	@PreDestroy
	private void preDestroy() {
		System.out.println("Destroying...");
		User user = repository.findByUserName("admin");
		if (user != null){
			repository.delete(user);
		}
		else {
			System.out.println("Cannot find user");
		}
	}


}
