package org.green_building.excellent_training.services;

import org.green_building.excellent_training.dtos.AuthRequestDto;
import org.green_building.excellent_training.dtos.AuthResponseDto;
import org.green_building.excellent_training.dtos.UserDto;
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

    public AuthResponseDto login(AuthRequestDto request) {
        User user = usersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }

        String token = jwtTokenProvider.createToken(user);

        return new AuthResponseDto(token, UserDto.from(user));
    }
    
    public AuthResponseDto register(UserDto userDto) {
        // Default role is USER if not specified
        if (userDto.getRoleId() == null) {
            Role userRole = rolesRepository.findByName(Role.USER)
                .orElseThrow(() -> new ResourceNotFoundException("role", "name", Role.USER));
            userDto.setRoleId(userRole.getId());
        }
        
        // Encode password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Create user
        User user = User.from(userDto);
        user = usersRepository.save(user);

        // Generate token
        String token = jwtTokenProvider.createToken(user);

        return new AuthResponseDto(token, UserDto.from(user));
    }
}
