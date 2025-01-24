package com.yuliia.airlines_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${api-endpoint}")
    String endpoint;
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
                        .requestMatchers(endpoint + "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/register/users").permitAll());
                        /*.requestMatchers(endpoint + "/login").hasAnyRole("USER", "ADMIN") // principio de mínimos
                        .requestMatchers(endpoint + "/public").permitAll()
                        .requestMatchers(endpoint + "/private").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, endpoint + "/countries").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, endpoint + "/countries").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .userDetailsService(jpaUserDetailsService)
                .httpBasic(withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
*/
        http.headers(header -> header.frameOptions(frame -> frame.sameOrigin())); //para h2


        return http.build();
    }
}
