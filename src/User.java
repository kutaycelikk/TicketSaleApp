import java.util.ArrayList;
import java.util.List;

// User model represents both admin and standard users in the system
public class User {
    public String username;
    public String password;
    public boolean isAdmin;
    public List<Ticket> purchasedTickets;

    // Constructor creates a user and initializes an empty list for their tickets
    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.purchasedTickets = new ArrayList<>();
    }
}