package com.login.user.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.login.user.domain.models.*;
import com.login.user.repositories.UsersRepository;
import com.login.user.services.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    UsersRepository usersRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var token = this.recoverToken(request);
        if(token != null){
            var login = tokenService.validateToken(token);
            User userToAuthenticate = usersRepository.findByLogin(login);

            if(userToAuthenticate != null){
                var authentication = new UsernamePasswordAuthenticationToken(userToAuthenticate, null, userToAuthenticate.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                if(!authentication.isAuthenticated()){
                    throw new RuntimeException("Usuário não autenticado");
                }
            } else {
                throw new RuntimeException("O usuário dono do token fornecido foi deletado");
            }
        }
        filterChain.doFilter(request, response);
    }
    
    public String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer","").trim();
    }
}
