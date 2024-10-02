package prueba.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import prueba.chat.model.User;
import prueba.chat.repository.UserRepository;

/**
 * CustomUserDetailsService is a service class that implements the UserDetailsService interface
 * to retrieve user details from the database for Spring Security authentication.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

     /**
     * Loads a user by their username from the database and returns a UserDetails object
     * that is used by Spring Security for authentication and authorization.
     * 
     * @param username The username of the user to load.
     * @return A UserDetails object containing the user's credentials and authorities.
     * @throws UsernameNotFoundException If no user is found with the given username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER") 
                .build();
    }
}

