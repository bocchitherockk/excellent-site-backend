package org.green_building.excellent_training;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

import org.green_building.excellent_training.entities.Role;
import org.green_building.excellent_training.repositories.RolesRepository;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    /*
    @Bean
    CommandLineRunner commandLineRunner(RolesRepository rolesRepository) {
	return args -> {
	    Role role1 = new Role("this is a new role :D");
	    Role role2 = new Role("this is role 2");
	    rolesRepository.save(role1);
	    rolesRepository.save(role2);
	};
    }
    */
}
