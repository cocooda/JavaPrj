public class Professor extends User {
    private String name;
    
    //Constructor
    public Professor(String userID, String username, String password, String role, String name) {
        super(userID, username, password, role);
        this.name =name;
    }

    public Professor(String name) {
        super("", "","", "");
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

