package com.ampta.resume_api.controller;

import com.ampta.resume_api.dto.AuthResponse;
import com.ampta.resume_api.dto.RegisterRequest;
import com.ampta.resume_api.service.AuthService;
import com.ampta.resume_api.service.AuthServiceImpl;
import com.ampta.resume_api.util.Endpoints;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.ampta.resume_api.util.Endpoints.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(AUTH_CONTROLLER)
public class AuthController {

    private final AuthService authService;

    @PostMapping(REGISTER)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){

        log.info("Register request received: {}", request);

        AuthResponse response = authService.register(request);

        log.info("Returning from Register controller Response: {}", response);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping(VERIFY_EMAIL)
    public ResponseEntity<?> verifyEmail(@RequestParam String token){

        log.info("Verify email request received: {}", token);

        authService.verifyEmail(token);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Email verified successfully."));
    }
}
