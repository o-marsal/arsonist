package oml.arsonist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ArsonistApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArsonistApplication.class, args);
	}

}
