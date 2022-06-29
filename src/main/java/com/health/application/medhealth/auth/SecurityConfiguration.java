package com.health.application.medhealth.auth;

import com.health.application.medhealth.utils.CustomAuthFilter;
import com.health.application.medhealth.utils.JwtOncePerRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AuthenticationConfiguration configuration;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthFilter customAuthFilter = new CustomAuthFilter(authenticationManager(configuration));
        customAuthFilter.setFilterProcessesUrl("/api/token");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.exceptionHandling()
                .authenticationEntryPoint((req, res, ex) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage()));
        http.authorizeRequests()
                .antMatchers("/api/token").permitAll()
                .antMatchers("/api/register/**").permitAll()
                .antMatchers("/api/patient/**").hasAnyAuthority("ROLE_PATIENT")
                .antMatchers("/api/doctor/**").hasAnyAuthority("ROLE_DOCTOR")
                .anyRequest().authenticated();
        http.addFilter(customAuthFilter);
        http.addFilterBefore(new JwtOncePerRequestFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
