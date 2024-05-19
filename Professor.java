public class Professor extends User {
    private String name;
    
    //Constructor
    public Professor(int userID, String username, String password, String role) {
        super(userID, username, password, role);
    }

    public Professor(String name) {
        super(0, "", "", ""); // You may want to replace these with actual values
        this.name = name;
    }

    // Getter and setter methods for all attributes
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

