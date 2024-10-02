package prueba.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig is responsible for configuring Spring Security settings,
 * including authentication, session management, and the integration of the JWT filter.
 */
@Configuration
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    /**
     * Constructor for SecurityConfig that injects the JwtRequestFilter dependency.
     * 
     * @param jwtRequestFilter Custom filter to validate JWT tokens in each request.
     */
    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    /**
     * Configures the security filter chain for the application, setting up authorization rules, 
     * session management, and exception handling for redirection when access is denied.
     * 
     * This method defines:
     * - Publicly accessible endpoints that do not require authentication.
     * - Endpoints that require authentication to access.
     * - Stateless session management to ensure no session is created or maintained.
     * - Custom handling of authentication errors, redirecting unauthorized users to the login page.
     * 
     * @param http The HttpSecurity object used to configure security features.
     * @return SecurityFilterChain A configured security filter chain for the application.
     * @throws Exception Throws a general exception if any configuration fails.
     */
    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/users/register", "/login", "/js/jwt-interceptor.js","/register","/authenticate/login", "/static/**", "/css/**", "/js/**","/images/**").permitAll() 
                .requestMatchers("/users/all", "/chat/**", "/chat-websocket/**").authenticated()  
            )
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
            .and()
            .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendRedirect("/login");
                })
            .and()
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); 

    return http.build();
    }

    /**
     * Defines the PasswordEncoder bean, which will be used for encoding and verifying passwords.
     * BCrypt is a strong hashing algorithm commonly used for password encryption.
     * 
     * @return PasswordEncoder A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines the AuthenticationManager bean, which is responsible for processing authentication requests.
     * This is used in the application to authenticate users during login.
     * 
     * @param authConfig The authentication configuration provided by Spring Security.
     * @return AuthenticationManager The authentication manager configured for the application.
     * @throws Exception Throws an exception if the authentication manager cannot be created.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
