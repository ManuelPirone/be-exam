package org.example.beexam.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.beexam.security.JwtService;
import org.example.beexam.security.SecurityUser;
import org.example.beexam.user.dto.AuthenticationRequest;
import org.example.beexam.user.dto.AuthenticationResponse;
import org.example.beexam.user.dto.RegisterRequest;
import org.example.beexam.user.entity.Role;
import org.example.beexam.user.entity.User;
import org.example.beexam.user.repository.RoleRepository;
import org.example.beexam.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CloudinaryService cloudinaryService;

    public AuthenticationResponse register(RegisterRequest request) {
        // ruolo admin di default per testare
        Role userRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Ruolo ADMIN non trovato."));

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(userRole);

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(new SecurityUser(user));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(new SecurityUser(user));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public String updateProfileImage(String userEmail, MultipartFile file) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        String imageUrl = cloudinaryService.uploadImage(file);

        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }
}