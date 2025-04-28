package org.green_building.excellent_training.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.green_building.excellent_training.dtos.DomainRequestDto;
import org.green_building.excellent_training.dtos.EmployerRequestDto;
import org.green_building.excellent_training.dtos.ParticipantRequestDto;
import org.green_building.excellent_training.dtos.ProfileRequestDto;
import org.green_building.excellent_training.dtos.RoleRequestDto;
import org.green_building.excellent_training.dtos.StructureRequestDto;
import org.green_building.excellent_training.dtos.TrainerRequestDto;
import org.green_building.excellent_training.dtos.TrainingSessionRequestDto;
import org.green_building.excellent_training.dtos.UserRequestDto;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class DataLoader {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String dataDir = "src/main/resources/db/data/";

    private <T> List<T> loadFromJson(String path, TypeReference<List<T>> typeReference) throws IOException {
        Path filePath = Paths.get(path);
        if (Files.exists(filePath)) return DataLoader.mapper.readValue(new File(path), typeReference);
        return new ArrayList<>();
    }

    @Bean
    CommandLineRunner loadDataFromJson(
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
            List<RoleRequestDto> roleDtos = this.loadFromJson(DataLoader.dataDir + "roles.json", new TypeReference<List<RoleRequestDto>>() {});
            List<Role> roles = Role.from(roleDtos);
            rolesRepository.saveAll(roles);

            List<UserRequestDto> userDtos = this.loadFromJson(DataLoader.dataDir + "users.json", new TypeReference<List<UserRequestDto>>() {});
            List<User> users = User.from(userDtos);
            for (User user : users) user.setPassword(passwordEncoder.encode(user.getPassword()));
            usersRepository.saveAll(users);

            List<ProfileRequestDto> profileDtos = this.loadFromJson(DataLoader.dataDir + "profiles.json", new TypeReference<List<ProfileRequestDto>>() {});
            List<Profile> profiles = Profile.from(profileDtos);
            profilesRepository.saveAll(profiles);

            List<StructureRequestDto> structureDtos = this.loadFromJson(DataLoader.dataDir + "structures.json", new TypeReference<List<StructureRequestDto>>() {});
            List<Structure> structures = Structure.from(structureDtos);
            structuresRepository.saveAll(structures);

            List<DomainRequestDto> domainDtos = this.loadFromJson(DataLoader.dataDir + "domains.json", new TypeReference<List<DomainRequestDto>>() {});
            List<Domain> domains = Domain.from(domainDtos);
            domainsRepository.saveAll(domains);

            List<EmployerRequestDto> employerDtos = this.loadFromJson(DataLoader.dataDir + "employers.json", new TypeReference<List<EmployerRequestDto>>() {});
            List<Employer> employers = Employer.from(employerDtos);
            employersRepository.saveAll(employers);

            List<ParticipantRequestDto> participantDtos = this.loadFromJson(DataLoader.dataDir + "participants.json", new TypeReference<List<ParticipantRequestDto>>() {});
            List<Participant> participants = Participant.from(participantDtos);
            participantsRepository.saveAll(participants);

            List<TrainerRequestDto> trainerDtos = this.loadFromJson(DataLoader.dataDir + "trainers.json", new TypeReference<List<TrainerRequestDto>>() {});
            List<Trainer> trainers = Trainer.from(trainerDtos);
            trainersRepository.saveAll(trainers);

            List<TrainingSessionRequestDto> trainingSessionDtos = this.loadFromJson(DataLoader.dataDir + "training_sessions.json", new TypeReference<List<TrainingSessionRequestDto>>() {});
            List<TrainingSession> trainingSessions = TrainingSession.from(trainingSessionDtos);
            List<Map<String, Integer>> participations = this.loadFromJson(DataLoader.dataDir + "participations.json", new TypeReference<List<Map<String, Integer>>>() {});
            List<Map<String, Integer>> trains = this.loadFromJson(DataLoader.dataDir + "trains.json", new TypeReference<List<Map<String, Integer>>>() {});
            for (TrainingSession trainingSession : trainingSessions) {
                participations.stream()
                    .filter(p -> p.get("training_session_id").equals(trainingSession.getId()))
                    .forEach(p -> {
                        Integer participantId = p.get("participant_id");
                        Participant participant = participants.stream().filter(participant1 -> participant1.getId().equals(participantId)).findFirst().orElse(null);
                        if (participant != null) trainingSession.getParticipants().add(participant);
                    });
                trains.stream()
                    .filter(t -> t.get("training_session_id").equals(trainingSession.getId()))
                    .forEach(t -> {
                        Integer trainerId = t.get("trainer_id");
                        Trainer trainer = trainers.stream().filter(trainer1 -> trainer1.getId().equals(trainerId)).findFirst().orElse(null);
                        if (trainer != null) trainingSession.getTrainers().add(trainer);
                    });
            }
            trainingSessionsRepository.saveAll(trainingSessions);
        };
    }
}
