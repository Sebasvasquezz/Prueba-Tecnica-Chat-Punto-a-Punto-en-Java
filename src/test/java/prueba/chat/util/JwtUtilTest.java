package prueba.chat.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private static final String SECRET_KEY = "mysecretkey123456mysecretkey123456mysecretkey123456";
    private static final String USERNAME = "testUser";
    private static final String TOKEN = "testToken";
    private static final Date EXPIRATION_DATE = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtUtil, "SECRET_KEY", SECRET_KEY);
    }

    @Test
    void testExtractUsername() {
        Claims claims = mock(Claims.class);
        JwtParser jwtParser = mock(JwtParser.class);

        when(claims.getSubject()).thenReturn(USERNAME);

        try (MockedStatic<Jwts> jwtsMockedStatic = mockStatic(Jwts.class)) {
            jwtsMockedStatic.when(Jwts::parser).thenReturn(jwtParser);
            when(jwtParser.setSigningKey(SECRET_KEY)).thenReturn(jwtParser);
            when(jwtParser.parseClaimsJws(TOKEN)).thenReturn(mock(io.jsonwebtoken.Jws.class));
            when(jwtParser.parseClaimsJws(TOKEN).getBody()).thenReturn(claims);

            String extractedUsername = jwtUtil.extractUsername(TOKEN);

            assertEquals(USERNAME, extractedUsername);
        }
    }

    @Test
    void testExtractExpiration() {
        Claims claims = mock(Claims.class);
        JwtParser jwtParser = mock(JwtParser.class);

        when(claims.getExpiration()).thenReturn(EXPIRATION_DATE);

        try (MockedStatic<Jwts> jwtsMockedStatic = mockStatic(Jwts.class)) {
            jwtsMockedStatic.when(Jwts::parser).thenReturn(jwtParser);
            when(jwtParser.setSigningKey(SECRET_KEY)).thenReturn(jwtParser);
            when(jwtParser.parseClaimsJws(TOKEN)).thenReturn(mock(io.jsonwebtoken.Jws.class));
            when(jwtParser.parseClaimsJws(TOKEN).getBody()).thenReturn(claims);

            Date extractedExpiration = jwtUtil.extractExpiration(TOKEN);

            assertEquals(EXPIRATION_DATE, extractedExpiration);
        }
    }

    @Test
    void testGenerateToken() {
        try (MockedStatic<Jwts> jwtsMockedStatic = mockStatic(Jwts.class)) {
            jwtsMockedStatic.when(Jwts::builder).thenReturn(mock(JwtBuilder.class));

            JwtBuilder jwtBuilderMock = mock(JwtBuilder.class);
            jwtsMockedStatic.when(Jwts::builder).thenReturn(jwtBuilderMock);

            when(jwtBuilderMock.setClaims(anyMap())).thenReturn(jwtBuilderMock);
            when(jwtBuilderMock.setSubject(USERNAME)).thenReturn(jwtBuilderMock);
            when(jwtBuilderMock.setIssuedAt(any(Date.class))).thenReturn(jwtBuilderMock);
            when(jwtBuilderMock.setExpiration(any(Date.class))).thenReturn(jwtBuilderMock);
            when(jwtBuilderMock.signWith(eq(SignatureAlgorithm.HS256), eq(SECRET_KEY))).thenReturn(jwtBuilderMock);
            when(jwtBuilderMock.compact()).thenReturn(TOKEN);

            String token = jwtUtil.generateToken(USERNAME);

            assertNotNull(token); 
            assertEquals(TOKEN, token);  

            verify(jwtBuilderMock).setClaims(anyMap());
            verify(jwtBuilderMock).setSubject(eq(USERNAME));
            verify(jwtBuilderMock).setIssuedAt(any(Date.class));
            verify(jwtBuilderMock).setExpiration(any(Date.class));
            verify(jwtBuilderMock).signWith(eq(SignatureAlgorithm.HS256), eq(SECRET_KEY));
            verify(jwtBuilderMock).compact();
        }
    }

    @Test
    void testValidateToken() {
        Claims claims = mock(Claims.class);
        JwtParser jwtParser = mock(JwtParser.class);

        when(claims.getSubject()).thenReturn(USERNAME);
        when(claims.getExpiration()).thenReturn(EXPIRATION_DATE);

        try (MockedStatic<Jwts> jwtsMockedStatic = mockStatic(Jwts.class)) {
            jwtsMockedStatic.when(Jwts::parser).thenReturn(jwtParser);
            when(jwtParser.setSigningKey(SECRET_KEY)).thenReturn(jwtParser);
            when(jwtParser.parseClaimsJws(TOKEN)).thenReturn(mock(io.jsonwebtoken.Jws.class));
            when(jwtParser.parseClaimsJws(TOKEN).getBody()).thenReturn(claims);

            Boolean isValid = jwtUtil.validateToken(TOKEN, USERNAME);

            assertTrue(isValid);
        }
    }

    @Test
    void testValidateTokenExpired() {
        Claims claims = mock(Claims.class);
        JwtParser jwtParser = mock(JwtParser.class);

        when(claims.getSubject()).thenReturn(USERNAME);
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() - 1000)); 

        try (MockedStatic<Jwts> jwtsMockedStatic = mockStatic(Jwts.class)) {
            jwtsMockedStatic.when(Jwts::parser).thenReturn(jwtParser);
            when(jwtParser.setSigningKey(SECRET_KEY)).thenReturn(jwtParser);
            when(jwtParser.parseClaimsJws(TOKEN)).thenReturn(mock(io.jsonwebtoken.Jws.class));
            when(jwtParser.parseClaimsJws(TOKEN).getBody()).thenReturn(claims);

            Boolean isValid = jwtUtil.validateToken(TOKEN, USERNAME);

            assertFalse(isValid);
        }
    }
}
