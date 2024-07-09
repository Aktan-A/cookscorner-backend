package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.LoginRequestDto;
import com.neobis.cookscorner.dto.LoginResponseDto;
import com.neobis.cookscorner.dto.RegisterRequestDto;
import com.neobis.cookscorner.dto.RegisterResponseDto;
import com.neobis.cookscorner.enums.UserRole;
import com.neobis.cookscorner.exception.ResourceExistsException;
import com.neobis.cookscorner.exception.ResourceNotFoundException;
import com.neobis.cookscorner.model.User;
import com.neobis.cookscorner.repository.UserRepository;
import com.neobis.cookscorner.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) {
        boolean emailExists = userRepository.existsByEmail(registerRequestDto.getEmail());
        if (emailExists) {
            throw new ResourceExistsException(
                    String.format("User with email %s already exists.", registerRequestDto.getEmail()));
        }

        User user = new User();
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setFirstName(registerRequestDto.getFirstName());
        user.setLastName(registerRequestDto.getLastName());
        user.setRole(UserRole.USER_ROLE);

        return modelMapper.map(userRepository.save(user), RegisterResponseDto.class);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );
        Optional<User> user = userRepository.findByEmail(loginRequestDto.getEmail());
        if (user.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("User with email %s was not found.", loginRequestDto.getEmail()));
        }

        User userModel = user.get();
        String accessToken = jwtService.generateToken(userModel);
        return LoginResponseDto.builder().accessToken(accessToken).build();
    }
}
