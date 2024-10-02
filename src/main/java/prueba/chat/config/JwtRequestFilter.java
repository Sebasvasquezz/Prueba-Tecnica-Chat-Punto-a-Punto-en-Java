package prueba.chat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import prueba.chat.service.CustomUserDetailsService;
import prueba.chat.util.JwtUtil;
import java.io.IOException;

/**
 * JwtRequestFilter is a custom filter that intercepts each HTTP request 
 * and checks if there is a valid JWT token in the request. 
 * If a valid token is found, it extracts the user details and 
 * sets the authentication in the SecurityContext.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * This method filters each HTTP request to check for a valid JWT token in cookies or the authorization header.
     * If a valid token is found, it extracts the username, loads user details, and sets the authentication in the SecurityContext.
     * 
     * @param request The HTTP request being filtered.
     * @param response The HTTP response being filtered.
     * @param chain The filter chain to continue the request.
     * @throws ServletException In case of servlet-related issues.
     * @throws IOException In case of input/output errors.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String jwt = null;
        String username = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    System.out.println("JWT token found in cookies: " + jwt);
                    break;
                }
            }
        }

        if (jwt == null) {
            final String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                System.out.println("Authorization header found: " + jwt);
            } else {
                System.out.println("No Authorization header found or it doesn't start with 'Bearer '");
            }
        }

        if (jwt != null) {
            try {
                username = jwtUtil.extractUsername(jwt);
                System.out.println("Username extracted from JWT: " + username);
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Username is not null and the user is not authenticated yet");
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            System.out.println("UserDetails loaded: " + userDetails.getUsername());

            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                System.out.println("JWT Token is valid");
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                System.out.println("User authenticated: " + username);
            } else {
                System.out.println("JWT Token is not valid");
            }
        }

        chain.doFilter(request, response);
    }
}
