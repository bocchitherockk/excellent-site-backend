package org.green_building.excellent_training.services;

import org.green_building.excellent_training.dtos.AuthRequestDto;
import org.green_building.excellent_training.dtos.AuthResponseDto;
import org.green_building.excellent_training.dtos.UserRequestDto;
import org.green_building.excellent_training.dtos.UserResponseDto;
import org.green_building.excellent_training.entities.Role;
import org.green_building.excellent_training.entities.User;
import org.green_building.excellent_training.exceptions.AuthenticationException;
import org.green_building.excellent_training.exceptions.ResourceNotFoundException;
import org.green_building.excellent_training.repositories.RolesRepository;
import org.green_building.excellent_training.repositories.UsersRepository;
import org.green_building.excellent_training.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
            UsersRepository usersRepository,
            RolesRepository rolesRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponseDto login(AuthRequestDto credentials) {
        User user = usersRepository.findByUsername(credentials.getUsername())
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));

        if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }

        String token = jwtTokenProvider.createToken(user);
        return new AuthResponseDto(token, UserResponseDto.from(user));
    }

    /*
    public AuthResponseDto register(UserRequestDto request) {
        // Default role is USER if not specified
        if (request.getRoleId() == null) {
            Role userRole = rolesRepository.findByName(Role.USER)
                .orElseThrow(() -> new ResourceNotFoundException("role", "name", Role.USER));
            request.setRoleId(userRole.getId());
        }

        // Encode password
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        // Create user
        User user = User.from(request);
        user = usersRepository.save(user);

        // Generate token
        String token = jwtTokenProvider.createToken(user);

        return new AuthResponseDto(token, UserResponseDto.from(user));
    }
    */
}
