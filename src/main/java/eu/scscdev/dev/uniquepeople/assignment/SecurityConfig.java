package eu.scscdev.dev.uniquepeople.assignment;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

@Component
@EnableGlobalMethodSecurity(securedEnabled = true)
@Log
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.profiles.active}")
    private String profile;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (isDev()) {
            http.csrf().disable();
            log.warning("CSRF disabled due to DEV mode!");
        }
        http
            .httpBasic().and()
            .formLogin().and()
            .authorizeRequests()
            .anyRequest().permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        if (isDev()) {
            auth.inMemoryAuthentication()
                .withUser("admin")
                .password("{noop}most-secret")
                .roles("ADMIN");
        } else {
            super.configure(auth);
        }
    }

    private boolean isDev() {
        return "dev".equals(profile);
    }
}
