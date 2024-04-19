package com.login.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.login.user.domain.dtos.AuthenticationDto;
import com.login.user.domain.exceptions.IncorrectCredentialsException;
import com.login.user.domain.exceptions.UserNotFoundException;
import com.login.user.domain.models.User;

@Service
public class AuthenticateUserService {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    public User authenticateLogin(AuthenticationDto loginData){
        try{
        User user = userService.getUserByLogin(loginData.login());
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginData.login(), loginData.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        if (auth.getPrincipal() instanceof UserDetails) {
            return user;
        }
        } catch (UserNotFoundException exception){
            throw new IncorrectCredentialsException("Login ou senha incorretos");
        } catch (AuthenticationException exception){
            throw new IncorrectCredentialsException("Login ou senha incorretos");
        }
        throw new UserNotFoundException("Não foi possível autenticar esse usuário");
    }
}
