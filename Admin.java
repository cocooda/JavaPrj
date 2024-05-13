public class Admin extends User {
    private String adminRole;

    // Constructor

    public Admin(int userID, String username, String password, String role) {
        super(userID, username, password, role);
    }
    ABCDEX
    // Getter
    public String getAdminRole() {
        return adminRole;
    }

    // Setter
    public void setAdminRole(String adminRole) {
        this.adminRole = adminRole;
    }

    // Other methods
    
}
