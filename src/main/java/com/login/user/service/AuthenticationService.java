package com.login.user.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.login.user.domain.dto.request.LoginRequestDTO;
import com.login.user.domain.exception.*;
import com.login.user.domain.model.User;

@AllArgsConstructor
@Service
public class AuthenticationService {
 
    private AuthenticationManager authenticationManager;

    private UserService userService;

    private EmailService emailService;

    public User authenticateLogin(LoginRequestDTO loginRequestDto){
        try {
            var user = userService.getUserByEmail(loginRequestDto.email());
            isUserActivated(user);
            var authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password());
            var auth = this.authenticationManager.authenticate(authenticationToken);

            if (auth.getPrincipal() instanceof UserDetails) {
                return user;
            }
        } catch (UserNotFoundException | AuthenticationException exception){
            throw new IncorrectCredentialsException("Login ou senha incorretos");
        }

        throw new UserNotFoundException();
    }

    public void isUserActivated(User user) {
        if (!user.isEnabled()) {
            throw new UserNotActivatedException();
        }
    }

    public void activateRedefinePassword(String email) {
        var user = userService.getUserByEmail(email);
        var otpCode = generateRandomFourDigitsNumber();
        user.setOtpCode(otpCode);
        user.setOtpTimestamp(Instant.now());
        userService.save(user);

        emailService.sendRedefinePasswordEmail(user.getEmail(), otpCode);
    }

    public String generateRandomFourDigitsNumber() {
        var random = new Random();
        var randomFourDigitsNumber = 1000 + random.nextInt(9000);

        return Integer.toString(randomFourDigitsNumber);
    }

    public void redefinePassword(String otpCode, String newPassword, String email) {
        var user = userService.getUserByEmail(email);
        isRedefinePasswordAuthorized(user, otpCode);
 
        user.setPassword(userService.encodePassword(newPassword));
        user.setOtpCode(null);
        user.setOtpTimestamp(null);
        userService.save(user);
    }

    public void isRedefinePasswordAuthorized(User user, String incomingOtpCode) {
        var otpExpiry = user.getOtpTimestamp().plus(Duration.ofMinutes(5));

        if (isOtpCodeValid(user.getOtpCode(), incomingOtpCode, otpExpiry)) {
            throw new UnauthorizedException("Redefinição de senha não autorizada");
        }
    }

    public boolean isOtpCodeValid(String userOtpCode, String incomingOtpCode, Instant otpExpiry) {
        return userOtpCode == null || !userOtpCode.equals(incomingOtpCode) || Instant.now().isAfter(otpExpiry);
    }
}
