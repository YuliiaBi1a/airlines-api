package com.yuliia.airlines_api.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final String key;

    @Value("${api-endpoint}")
    String endpoint;

    public SecurityConfig(UserDetailsService userDetailsService, String key) {
        this.userDetailsService = userDetailsService;
        this.key = key;
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
                        .requestMatchers(endpoint + "/auth/token").permitAll()
                        .requestMatchers(endpoint + "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, endpoint+ "/register/**").permitAll()
                        .requestMatchers(HttpMethod.GET,endpoint + "/public/**").hasAnyRole("CLIENT", "ADMIN")
                        .requestMatchers(endpoint + "/public/**").hasRole("CLIENT")
                        .requestMatchers(endpoint + "/private/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .userDetailsService(userDetailsService)
                .httpBasic(withDefaults())
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.decoder(jwtDecoder())))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));


        http.headers(header -> header.frameOptions(frame -> frame.sameOrigin())); //para h2

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public JwtEncoder jwtEncoder() {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
    }
    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] bytes = key.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(bytes, 0, bytes.length, "RSA");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS512).build();
    }

    @Bean
    CorsConfigurationSource corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

/*    @Bean
    UserDetailsService userDetailsServiceJwt() {
        return new InMemoryUserDetailsManager(
                User.withUsername("yuli")
                        .password("{noop}yuli")
                        .authorities("READ", "ROLE_ADMIN")
                        .build());
    }*/
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