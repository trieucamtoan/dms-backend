package ca.toantrieu.dms;


import ca.toantrieu.dms.Config.SecurityConfig;
import ca.toantrieu.dms.Model.User;
import ca.toantrieu.dms.Repository.UserRepository;
import ca.toantrieu.dms.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class DmsApplication {
	@Autowired
	private UserRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(DmsApplication.class, args);
	}

}
