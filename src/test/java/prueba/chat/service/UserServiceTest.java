package prueba.chat.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import prueba.chat.model.User;
import prueba.chat.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "plainPassword";
    private static final String ENCODED_PASSWORD = "encodedPassword";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUserSuccess() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(ENCODED_PASSWORD);

        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.registerUser(USERNAME, PASSWORD);

        assertEquals(USERNAME, registeredUser.getUsername());
        assertEquals(ENCODED_PASSWORD, registeredUser.getPassword());

        verify(passwordEncoder, times(1)).encode(PASSWORD);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindByUsernameSuccess() {
        User user = new User();
        user.setUsername(USERNAME);

        when(userRepository.findByUsername(USERNAME)).thenReturn(user);

        User foundUser = userService.findByUsername(USERNAME);

        assertEquals(USERNAME, foundUser.getUsername());

        verify(userRepository, times(1)).findByUsername(USERNAME);
    }

    @Test
    void testFindByUsernameNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(null);

        User foundUser = userService.findByUsername(USERNAME);

        assertNull(foundUser);

        verify(userRepository, times(1)).findByUsername(USERNAME);
    }

    @Test
    void testFindAllUsersSuccess() {
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");

        List<User> userList = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> allUsers = userService.findAllUsers();

        assertEquals(2, allUsers.size());
        assertEquals("user1", allUsers.get(0).getUsername());
        assertEquals("user2", allUsers.get(1).getUsername());

        verify(userRepository, times(1)).findAll();
    }
}

