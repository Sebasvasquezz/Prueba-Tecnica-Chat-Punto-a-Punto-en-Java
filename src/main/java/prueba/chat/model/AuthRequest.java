package prueba.chat.model;

/**
 * AuthRequest is a model class that represents an authentication request.
 * It contains the username and password provided by the user during login.
 */
public class AuthRequest {
    private String username;
    private String password;

    // Getters y Setters
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
