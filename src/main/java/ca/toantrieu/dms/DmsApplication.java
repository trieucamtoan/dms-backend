package ca.toantrieu.dms;


import ca.toantrieu.dms.Model.User;
import ca.toantrieu.dms.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class DmsApplication {
	@Autowired
	private UserRepository repository;

	@PostConstruct
	public void initUsers() {
		List<User> users = Stream.of(
				new User(101, "java", "password", "java@gmail.com")
		).collect(Collectors.toList());

		repository.saveAll(users);
	}
	public static void main(String[] args) {
		SpringApplication.run(DmsApplication.class, args);
	}

}
