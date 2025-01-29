package com.yuliia.airlines_api.config;

import com.yuliia.airlines_api.security.UserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    @Value("${api-endpoint}")
    String endpoint;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .logout(out -> out
                        .logoutUrl(endpoint + "/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        .requestMatchers("/uploads/images/**").permitAll()
                        /*.requestMatchers(endpoint + "/**").permitAll()*/
                        .requestMatchers(endpoint + "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, endpoint+ "/register/**").permitAll()
                        .requestMatchers(HttpMethod.GET,endpoint + "/public/**").hasAnyRole("CLIENT", "ADMIN")
                        .requestMatchers(endpoint + "/public/**").hasRole("CLIENT")
                        .requestMatchers(endpoint + "/private/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .userDetailsService(userDetailsService)
                .httpBasic(withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        http.headers(header -> header.frameOptions(frame -> frame.sameOrigin())); //para h2

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}



/*
 * @Bean
 * public InMemoryUserDetailsManager userDetailsManager() {
 *
 * UserDetails mickey = User.builder()
 * .username("mickey")
 * .password("{noop}mouse")
 * .roles("ADMIN")
 * .build();
 *
 * UserDetails minnie = User.builder()
 * .username("minnie")
 * .password("{noop}mouse")
 * .roles("USER")
 * .build();
 *
 * Collection<UserDetails> users = new ArrayList<>();
 * users.add(mickey);
 * users.add(minnie);
 *
 * return new InMemoryUserDetailsManager(users);
 *
 */