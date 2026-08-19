// Ticket model stores all properties of a single event or activity
public class Ticket {
    public int id;
    public String activity;
    public String time;
    public double price;
    public int stock;
    public int soldCount;

    // Constructor initializes the ticket with given values and sets sold count to zero
    public Ticket(int id, String activity, String time, double price, int stock) {
        this.id = id;
        this.activity = activity;
        this.time = time;
        this.price = price;
        this.stock = stock;
        this.soldCount = 0;
    }
}