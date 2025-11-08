package com.ampta.resume_api.service.impl;

import com.ampta.resume_api.document.User;
import com.ampta.resume_api.exception.ResourceExistsException;
import com.ampta.resume_api.repository.UserRepository;
import com.ampta.resume_api.dto.AuthResponse;
import com.ampta.resume_api.dto.RegisterRequest;
import com.ampta.resume_api.service.AuthService;
import com.ampta.resume_api.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${app.base.url:http://localhost:8080}")
    private String appBaseUrl;

    public AuthResponse register(RegisterRequest request){
        log.info("Inside AuthService: register() {}", request);
        if(userRepository.existsByEmail(request.getEmail())){
            throw new ResourceExistsException("User already exists with this email");
        }

        User newUser = toDocument(request);
        userRepository.save(newUser);
        log.info("User register successfully: {}", newUser);

        // TODO: send verification email
        sendVerificationEmail(newUser);

        return toResponse(newUser);
    }

    @Override
    public void verifyEmail(String token) {
        log.info("Attempting to verify email with token={}", token);
        User user = userRepository.findByVerificationToken(token)
                 .orElseThrow(() -> new RuntimeException("Invalid or expired verification token"));

        if(user.getVerificationExpires() != null && user.getVerificationExpires().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Verification token has expired. Please request new one.");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationExpires(null);
        userRepository.save(user);
        log.info("Email verification SUCCESS userId={} email={}", user.getId(), user.getEmail());
    }

    private void sendVerificationEmail(User newUser) {
        try{
            log.info("Dispatching verification email to userId={} email={}", newUser.getId(), newUser.getEmail());

            String link = appBaseUrl + "/api/auth/verify-email?token=" + newUser.getVerificationToken();

            String newHtml = "<!DOCTYPE html>"
                    + "<html lang='en'>"
                    + "<head>"
                    + "  <meta charset='UTF-8'>"
                    + "  <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "  <title>Verify Your Email</title>"
                    + "  <style>" // CSS for better client rendering (especially mobile/webmail)
                    + "    body { margin: 0; padding: 0; background-color: #f4f4f4; }"
                    + "    table { border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; }"
                    + "  </style>"
                    + "</head>"
                    + "<body style='margin: 0; padding: 0; font-family: sans-serif; background-color: #f4f4f4;'>"
                    + "  <table role='presentation' border='0' cellpadding='0' cellspacing='0' width='100%'>"
                    + "    <tr>"
                    + "      <td align='center' style='padding: 20px 0 30px 0;'>"
                    // Main content container (max-width for readability)
                    + "        <table role='presentation' border='0' cellpadding='0' cellspacing='0' width='600' style='border-collapse: collapse; border: 1px solid #cccccc; background-color: #ffffff;'>"
                    + "          <tr>"
                    + "            <td style='padding: 40px 30px 40px 30px;'>"
                    // Header
                    + "              <h2 style='font-size: 24px; margin: 0 0 20px 0; color: #333333; text-align: center;'>Confirm Your Email Address</h2>"
                    // Body Text
                    + "              <p style='margin: 0 0 15px 0; font-size: 16px; line-height: 24px; color: #555555;'>Hello **" + newUser.getName() + "**,</p>"
                    + "              <p style='margin: 0 0 25px 0; font-size: 16px; line-height: 24px; color: #555555;'>Thank you for signing up! Please click the button below to confirm your email and activate your account.</p>"
                    // Button Container (using a nested table for reliable centering)
                    + "              <table role='presentation' border='0' cellpadding='0' cellspacing='0' style='width: 100%;'>"
                    + "                <tr>"
                    + "                  <td align='center' style='padding: 0 0 30px 0;'>"
                    // The Button Link
                    + "                    <a href='" + link + "' style='display: inline-block; padding: 12px 24px; background-color: #6366f1; color: #ffffff; border-radius: 6px; text-decoration: none; font-weight: bold; font-size: 16px; border: 1px solid #6366f1;'>"
                    + "                      Verify Email"
                    + "                    </a>"
                    + "                  </td>"
                    + "                </tr>"
                    + "              </table>"
                    // Fallback Link and Expiry
                    + "              <p style='margin: 0 0 10px 0; font-size: 14px; line-height: 20px; color: #888888;'>If the button above doesn't work, please copy and paste the following link into your web browser:</p>"
                    + "              <p style='margin: 0 0 20px 0; font-size: 14px; line-height: 20px; color: #333333; word-break: break-all;'>**" + link + "**</p>"
                    + "              <p style='margin: 0; font-size: 14px; line-height: 20px; color: #e11d48;'>**Note:** This link will expire in 24 hours.</p>"
                    + "            </td>"
                    + "          </tr>"
                    // Footer Section
                    + "          <tr>"
                    + "            <td style='padding: 20px 30px 20px 30px; background-color: #f0f0f0;'>"
                    + "              <p style='margin: 0; font-size: 12px; line-height: 18px; color: #999999; text-align: center;'>&copy; 2025 [https://github.com/Ampta]. All rights reserved.</p>"
                    + "            </td>"
                    + "          </tr>"
                    + "        </table>"
                    + "      </td>"
                    + "    </tr>"
                    + "  </table>"
                    + "</body>"
                    + "</html>";

            emailService.sendHtmlEmail(newUser.getEmail(), "Verify your email", newHtml);
        } catch (Exception ex) {
            log.error("FAILED sending verification email -> userId={} email={}", newUser.getId(), newUser.getEmail(), ex);
            throw new RuntimeException("Failed to send verification email: " + ex.getMessage());
        }
    }

    private AuthResponse toResponse(User newUser){
        return AuthResponse.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .email(newUser.getEmail())
                .profileImageUrl(newUser.getProfileImageUrl())
                .emailVerified(newUser.getEmailVerified())
                .subscriptionPlan(newUser.getSubscriptionPlan())
                .createdAt(newUser.getCreatedAt())
                .updatedAt(newUser.getUpdatedAt())
                .build();
    }

    private User toDocument(RegisterRequest request){
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .profileImageUrl(request.getProfileImageUrl())
                .subscriptionPlan("Basic")
                .emailVerified(false)
                .verificationToken(UUID.randomUUID().toString())
                .verificationExpires(LocalDateTime.now().plusHours(24))
                .build();
    }
}
