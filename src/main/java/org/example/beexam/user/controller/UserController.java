package org.example.beexam.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.beexam.user.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @PatchMapping("/me/image")
    public ResponseEntity<Map<String, String>> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            Principal principal
    ) {
        String imageUrl = authService.updateProfileImage(principal.getName(), file);
        return ResponseEntity.ok(Map.of("profileImageUrl", imageUrl));
    }
}