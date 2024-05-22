package com.hs.languagelearningapi.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app-config")
public class ApplicationConfigData {
    private String jwtSecret;
    private Long jwtExpiryDurationInMinutes;
    private Long passwordResetTokenExpiryDurationInMinutes;
}
