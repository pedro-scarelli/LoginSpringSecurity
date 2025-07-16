package com.login.user;

import com.login.user.config.ApiEnvVariablesConfig;
import com.login.user.config.TokenServiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {TokenServiceConfig.class, ApiEnvVariablesConfig.class})
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}
}
