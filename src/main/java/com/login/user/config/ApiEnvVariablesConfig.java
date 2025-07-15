package com.login.user.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api")
public record ApiEnvVariablesConfig(
        @NotNull String baseUrl
) { }
