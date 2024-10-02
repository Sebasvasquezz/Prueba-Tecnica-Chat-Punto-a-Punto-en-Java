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

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String jwt = null;
        String username = null;

        // Busca el token JWT en las cookies
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

        // Si no se encontró el JWT en las cookies, intenta buscarlo en el encabezado Authorization
        if (jwt == null) {
            final String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                System.out.println("Authorization header found: " + jwt);
            } else {
                System.out.println("No Authorization header found or it doesn't start with 'Bearer '");
            }
        }

        // Si se encontró el JWT, intenta extraer el nombre de usuario
        if (jwt != null) {
            try {
                username = jwtUtil.extractUsername(jwt);
                System.out.println("Username extracted from JWT: " + username);
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        }

        // Si se obtuvo un nombre de usuario y aún no se ha autenticado, verifica el token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Username is not null and the user is not authenticated yet");
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            System.out.println("UserDetails loaded: " + userDetails.getUsername());

            // Validar el token JWT
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

        // Continuar con el siguiente filtro
        chain.doFilter(request, response);
    }
}
