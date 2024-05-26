package org.springtest.controller.config;

import jakarta.servlet.DispatcherType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springtest.service.service.UserDetailsServiceImpl;

import javax.sql.DataSource;

@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class WebSecurityConfig {

    private final DataSource dataSource;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public void configure(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("select username, password, enabled from users where username=?")
                .authoritiesByUsernameQuery("SELECT r.id, r.name " +
                        "FROM roles r " +
                        "JOIN user_roles ur ON r.id = ur.role_id " +
                        "JOIN users u ON u.id = ur.user_id " +
                        "WHERE u.username = ?")
        ;
        authBuilder.userDetailsService(userDetailsService);
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(
                    (authorize) -> authorize
                            .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                            .requestMatchers("/").permitAll()
//                            .requestMatchers("/note/**").hasAuthority("ADMIN")
                            .anyRequest().authenticated())

            .httpBasic(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .build();
    }
}