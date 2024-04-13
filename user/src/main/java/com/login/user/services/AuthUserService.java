package com.login.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.login.user.dtos.AuthenticationDto;
import com.login.user.dtos.UserDto;
import com.login.user.exceptions.UserNotFoundException;

@Service
public class AuthUserService {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    public UserDto authenticateLogin(AuthenticationDto data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
            
        if (auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            UserDto userDto = userService.getUserByLogin(userDetails.getUsername());
            return userDto;
        }
        throw new UserNotFoundException("Não foi possível autenticar esse usuário");
    }
}
