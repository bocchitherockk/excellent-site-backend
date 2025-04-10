package org.green_building.excellent_training;

import java.util.ArrayList;
import java.util.List;

import org.green_building.excellent_training.entities.Domain;
import org.green_building.excellent_training.entities.Employer;
import org.green_building.excellent_training.entities.Participant;
import org.green_building.excellent_training.entities.Profile;
import org.green_building.excellent_training.entities.Role;
import org.green_building.excellent_training.entities.Structure;
import org.green_building.excellent_training.entities.Trainer;
import org.green_building.excellent_training.entities.TrainingSession;
import org.green_building.excellent_training.entities.User;
import org.green_building.excellent_training.repositories.DomainsRepository;
import org.green_building.excellent_training.repositories.EmployersRepository;
import org.green_building.excellent_training.repositories.ParticipantsRepository;
import org.green_building.excellent_training.repositories.ProfilesRepository;
import org.green_building.excellent_training.repositories.RolesRepository;
import org.green_building.excellent_training.repositories.StructuresRepository;
import org.green_building.excellent_training.repositories.TrainersRepository;
import org.green_building.excellent_training.repositories.TrainingSessionsRepository;
import org.green_building.excellent_training.repositories.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            RolesRepository rolesRepository, 
            UsersRepository usersRepository,
            PasswordEncoder passwordEncoder,
            ProfilesRepository profilesRepository,
            StructuresRepository structuresRepository,
            ParticipantsRepository participantsRepository,
            DomainsRepository domainsRepository,
            TrainingSessionsRepository trainingSessionsRepository,
            EmployersRepository employersRepository,
            TrainersRepository trainersRepository) {
        return args -> {
            // Create roles
            Role userRole        = rolesRepository.save(new Role(Role.USER));
            Role responsibleRole = rolesRepository.save(new Role(Role.RESPONSIBLE));
            Role adminRole       = rolesRepository.save(new Role(Role.ADMIN));

            // Create users with encoded passwords
            usersRepository.save(new User("username user 1",        passwordEncoder.encode("password 1"), userRole));
            usersRepository.save(new User("username responsible 2", passwordEncoder.encode("password 2"), responsibleRole));
            usersRepository.save(new User("username admin 3",       passwordEncoder.encode("password 3"), adminRole));

            // Create Profiles
            Profile engineer_3Profile = profilesRepository.save(new Profile(Profile.ENGINEER_3));
            Profile engineer_5Profile = profilesRepository.save(new Profile(Profile.ENGINEER_5));
            Profile technicianProfile = profilesRepository.save(new Profile(Profile.TECHNICIAN));
            /*Profile lawyerProfile     = */profilesRepository.save(new Profile(Profile.LAWYER));

            // Create Structures
            Structure centralTunisStructure      = structuresRepository.save(new Structure("central tunis"));
            Structure regionalNabeulStructure    = structuresRepository.save(new Structure("regional nabeul"));
            Structure regionalZaghouaneStructure = structuresRepository.save(new Structure("regional zaghouane"));

            // Create Participants
            Participant participant_1 = participantsRepository.save(new Participant(
                "email@email_1.com",
                "first name 1",
                "last name 1",
                12345678,
                engineer_3Profile,
                centralTunisStructure
            ));
            Participant participant_2 = participantsRepository.save(new Participant(
                "email@email_2.com",
                "first name 2",
                "last name 2",
                12345678,
                engineer_5Profile,
                regionalNabeulStructure
            ));
            Participant participant_3 = participantsRepository.save(new Participant(
                "email@email_3.com",
                "first name 3",
                "last name 3",
                12345678,
                technicianProfile,
                regionalZaghouaneStructure
            ));

            // Create Domains
            Domain domain_1 = domainsRepository.save(new Domain("domain 1"));
            Domain domain_2 = domainsRepository.save(new Domain("domain 2"));
            Domain domain_3 = domainsRepository.save(new Domain("domain 3"));

            // Create Training Sessions
            TrainingSession trainingSession_1 = trainingSessionsRepository.save(new TrainingSession(
                "training session 1",
                2023,
                5,
                1000.0,
                domain_1
            ));
            TrainingSession trainingSession_2 = trainingSessionsRepository.save(new TrainingSession(
                "training session 2",
                2023,
                10,
                2000.0,
                domain_2
            ));
            TrainingSession trainingSession_3 = trainingSessionsRepository.save(new TrainingSession(
                "training session 3",
                2024,
                15,
                3000.0,
                domain_3
            ));

            // Add participants to training sessions
            trainingSession_1.setParticipants(new ArrayList<>(List.of(participant_2, participant_3)));
            trainingSession_2.setParticipants(new ArrayList<>(List.of(participant_1, participant_3)));
            trainingSession_3.setParticipants(new ArrayList<>(List.of(participant_1, participant_2)));
            trainingSessionsRepository.save(trainingSession_1);
            trainingSessionsRepository.save(trainingSession_2);
            trainingSessionsRepository.save(trainingSession_3);

            // Create Employers
            Employer employer_1 = employersRepository.save(new Employer("employer 1"));
            Employer employer_2 = employersRepository.save(new Employer("employer 2"));
            Employer employer_3 = employersRepository.save(new Employer("employer 3"));

            // Create trainers
            Trainer trainer_1 = trainersRepository.save(
                new Trainer(
                    "email@email_1.com",
                    "first name 1",
                    "last name 1",
                    12345678,
                    Trainer.Type.INTERNAL,
                    employer_1
                )
            );
            Trainer trainer_2 = trainersRepository.save(
                new Trainer(
                    "email@email_2.com",
                    "first name 2",
                    "last name 2",
                    12345678,
                    Trainer.Type.EXTERNAL,
                    employer_2
                )
            );
            Trainer trainer_3 = trainersRepository.save(
                new Trainer(
                    "email@email_3.com",
                    "first name 3",
                    "last name 3",
                    12345678,
                    Trainer.Type.INTERNAL,
                    employer_3
                )
            );
        };
    }
}
