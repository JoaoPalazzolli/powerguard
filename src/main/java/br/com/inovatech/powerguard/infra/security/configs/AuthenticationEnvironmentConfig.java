package br.com.inovatech.powerguard.infra.security.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.authentication")
@Data
public class AuthenticationEnvironmentConfig {

    private String usernameA;
    private String passwordA;
    private String usernameC;
    private String passwordC;
    private String usernameL;
    private String passwordL;

    public String[] getUsernames(){
        return new String[] {usernameA, usernameC, usernameL};
    }

    public String[] getPasswords(){
        return new String[] {passwordA, passwordC, passwordL};
    }
}
