package com.login.user.config;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.login.user.domain.exception.UnauthorizedException;
import com.login.user.domain.model.User;
import com.login.user.repository.UserRepository;
import com.login.user.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@AllArgsConstructor
@Component
public class SecurityFilter extends OncePerRequestFilter {

    TokenService tokenService;

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
