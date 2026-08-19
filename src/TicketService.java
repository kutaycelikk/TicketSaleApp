import java.util.ArrayList;
import java.util.List;

// TicketService manages inventory sales and ticket queries
public class TicketService {
    public List<Ticket> tickets;
    private int ticketIdCounter;

    // Constructor initializes the ticket database and adds some default tickets
    public TicketService() {
        this.tickets = new ArrayList<>();
        this.ticketIdCounter = 1;

        // Default tickets for testing purposes
        defineTicket("Obsession -Cinema", "20:00", 350.0, 100);
        defineTicket("YE LIVE IN ISTANBUL", "21:00", 7200.0, 50);
        defineTicket("Orduspor-Ayvalıkgücü 3rd League Final Football Match", "17:00", 250.0, 200);
        defineTicket("Theater (1 ticket remaining)", "20:30", 350.0, 1);
    }

    // Define ticket creates a new event and adds it to the available tickets list
    public void defineTicket(String activity, String time, double price, int stock) {
        Ticket newTicket = new Ticket(ticketIdCounter++, activity, time, price, stock);
        this.tickets.add(newTicket);
    }

    // Sales report returns all tickets to display their stock and sold counts
    public List<Ticket> getSalesReports() {
        return this.tickets;
    }

    // Search process finds tickets matching the keyword in their activity name
    public List<Ticket> searchTickets(String keyword) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.activity.toLowerCase().contains(keyword.toLowerCase())) {
                result.add(ticket);
            }
        }
        return result;
    }

    // Purchase process decreases stock increases sold count and assigns ticket to user
    public boolean buyTicket(int ticketId, User user) {
        for (Ticket ticket : tickets) {
            if (ticket.id == ticketId && ticket.stock > 0) {
                ticket.stock--;
                ticket.soldCount++;
                user.purchasedTickets.add(ticket);
                return true;
            }
        }
        return false;
    }

    // Cancellation process removes ticket from user returns stock and updates sold count
    public boolean cancelTicket(int ticketId, User user) {
        for (int i = 0; i < user.purchasedTickets.size(); i++) {
            Ticket ticket = user.purchasedTickets.get(i);
            if (ticket.id == ticketId) {
                ticket.stock++;
                ticket.soldCount--;
                user.purchasedTickets.remove(i);
                return true;
            }
        }
        return false;
    }
}