package com.example.httpstud.controller;

import com.example.httpstud.model.User;
import com.example.httpstud.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("registrationError", "Username already exists");
            return "register";
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("registrationError", "Email already exists");
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        return "redirect:/login?registered";
    }

    @GetMapping("/profile")
    public String profile(Model model, java.security.Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("user") User updatedUser, BindingResult result, Model model, java.security.Principal principal) {
        if (result.hasErrors()) {
            return "profile";
        }
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        user.setFullName(updatedUser.getFullName());
        user.setCurrencyPreference(updatedUser.getCurrencyPreference());
        user.setAvatarUrl(updatedUser.getAvatarUrl());
        user.setDateFormat(updatedUser.getDateFormat());
        user.setTheme(updatedUser.getTheme());
        userRepository.save(user);
        model.addAttribute("successMessage", "Profile updated successfully");
        return "profile";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, Model model, HttpServletRequest request) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setResetToken(UUID.randomUUID().toString());
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(2));
            userRepository.save(user);
            model.addAttribute("resetToken", user.getResetToken());
        });
        model.addAttribute("message", "If your email is registered, a reset token has been generated.");
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordForm(@RequestParam(required = false) String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String token, @RequestParam String password, Model model) {
        User user = userRepository.findByResetToken(token)
                .filter(u -> u.getResetTokenExpiry() != null && u.getResetTokenExpiry().isAfter(LocalDateTime.now()))
                .orElse(null);
        if (user == null) {
            model.addAttribute("resetError", "Invalid or expired reset token.");
            return "reset-password";
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
        return "redirect:/login?resetSuccess";
    }
}
