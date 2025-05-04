package com.login.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.login.user.domain.dtos.request.LoginRequestDTO;
import com.login.user.domain.exceptions.IncorrectCredentialsException;
import com.login.user.domain.exceptions.UserNotFoundException;
import com.login.user.domain.models.User;

@Service
public class AuthenticateUserService {
 
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    public User authenticateLogin(LoginRequestDTO loginRequestDto){
        try {
            var user = userService.getUserByLogin(loginRequestDto.login());
            var authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.login(), loginRequestDto.password());
            var auth = this.authenticationManager.authenticate(authenticationToken);

            if (auth.getPrincipal() instanceof UserDetails) {
                return user;
            }
        } catch (UserNotFoundException exception){
            throw new IncorrectCredentialsException("Login ou senha incorretos");
        } catch (AuthenticationException exception){
            throw new IncorrectCredentialsException("Login ou senha incorretos");
        }

        throw new UserNotFoundException();
    }
}

