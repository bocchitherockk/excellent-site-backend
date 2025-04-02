package org.green_building.excellent_training;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.green_building.excellent_training.entities.Role;
import org.green_building.excellent_training.entities.User;
import org.green_building.excellent_training.repositories.RolesRepository;
import org.green_building.excellent_training.repositories.UsersRepository;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(RolesRepository rolesRepository, UsersRepository usersRepository) {
        return args -> {
            rolesRepository.save(new Role("role 1"));
            rolesRepository.save(new Role("role 2"));
            rolesRepository.save(new Role("role 3"));

            usersRepository.save(
                new User(
                    "user 1",
                    "password 1",
                    rolesRepository.findById(1).get()
                )
            );
            usersRepository.save(
                new User(
                    "user 2",
                    "password 2",
                    rolesRepository.findById(2).get()
                )
            );
            usersRepository.save(
                new User(
                    "user 3",
                    "password 3",
                    rolesRepository.findById(3).get()
                )
            );
        };
    }
}
