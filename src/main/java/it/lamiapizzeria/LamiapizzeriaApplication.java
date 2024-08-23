package it.lamiapizzeria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import it.lamiapizzeria.storage.StorageProperties;
import it.lamiapizzeria.service.IStorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class LamiapizzeriaApplication {

	public static void main(String[] args) {
		String pass = new StandardPasswordEncoder().encode("password-test");
		System.out.println(pass);

		SpringApplication.run(LamiapizzeriaApplication.class, args);
	}

	@Bean
	CommandLineRunner init(IStorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}
}
