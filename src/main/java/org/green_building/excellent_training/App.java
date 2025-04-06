package org.green_building.excellent_training;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    CommandLineRunner commandLineRunner(
            RolesRepository rolesRepository, 
            UsersRepository usersRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Create roles
            Role userRole        = rolesRepository.save(new Role(Role.USER));
            Role responsibleRole = rolesRepository.save(new Role(Role.RESPONSIBLE));
            Role adminRole       = rolesRepository.save(new Role(Role.ADMIN));

            // Create users with encoded passwords
            usersRepository.save(
                new User(
                    "username user 1",
                    passwordEncoder.encode("password 1"),
                    userRole
                )
            );
            usersRepository.save(
                new User(
                    "username responsible 2",
                    passwordEncoder.encode("password 2"),
                    responsibleRole
                )
            );
            usersRepository.save(
                new User(
                    "username admin 3",
                    passwordEncoder.encode("password 3"),
                    adminRole
                )
            );
        };
    }
}
