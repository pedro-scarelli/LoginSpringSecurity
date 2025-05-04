package com.login.user.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import com.login.user.domain.exceptions.UnauthorizedException;
import com.login.user.domain.models.User;
import com.login.user.repositories.UserRepository;
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
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
        )
            throws ServletException, IOException {
        var token = this.recoverToken(request);

        if (token != null) {
            var login = tokenService.validateToken(token);

            isUserAuthenticated(userRepository.findByEmail(login));
        }

        filterChain.doFilter(request, response);
    }

    public String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;

        return authHeader.replace("Bearer ","").trim();
    }

    public void isUserAuthenticated(User userToAuthenticate) {
        if (userToAuthenticate == null) {
            throw new UnauthorizedException();
        }

        var authenticationToken = new UsernamePasswordAuthenticationToken(userToAuthenticate, null, userToAuthenticate.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        if (!authenticationToken.isAuthenticated()) {
            throw new UnauthorizedException();
        }
    }
}
