import java.util.ArrayList;
import java.util.List;

// AuthService handles all user authentication and account management operations
public class AuthService {
    public List<User> users;
    public User loggedInUser;

    // Constructor initializes user list and creates the default admin account
    public AuthService() {
        this.users = new ArrayList<>();
        this.loggedInUser = null;
        this.users.add(new User("admin", "admin123", true));
    }

    // Login process checks if username and password match any existing user
    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                this.loggedInUser = user;
                return true;
            }
        }
        return false;
    }

    // Logout process clears the currently logged in user session
    public void logout() {
        this.loggedInUser = null;
    }

    // Registration process creates a new user if the username is not taken
    public boolean registerUser(String username, String password) {
        for (User user : users) {
            if (user.username.equals(username)) {
                return false;
            }
        }
        this.users.add(new User(username, password, false));
        return true;
    }

    // Password change updates the password of the active logged in user
    public boolean changePassword(String newPassword) {
        if (this.loggedInUser != null) {
            this.loggedInUser.password = newPassword;
            return true;
        }
        return false;
    }

    // Account deletion removes the current non admin user from the system
    public boolean deleteAccount() {
        if (this.loggedInUser != null && !this.loggedInUser.isAdmin) {
            this.users.remove(this.loggedInUser);
            this.loggedInUser = null;
            return true;
        }
        return false;
    }
}