package com.login.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.security.token")
public record TokenServiceConfig (
    String jwtSecret,
    Long jwtExpirationInSeconds,
    String jwtIssuer
) {}
