import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String userID;
    private String username;
    private String password; 
    private String role;
    private boolean isLoggedIn = false;

    public User(String userID, String username, String password, String role) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters
    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole(){
        return role;
    }

    // Setters
    public void setName(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Other methods
    public static Map<String, User> readUsersFromDatabase() {
        Map<String, User> users = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 4) {
                    System.out.println("Invalid user data format: " + line);
                    continue;
                }
                String userID = parts[0];
                String username = parts[1];
                String password = parts[2];
                String role = parts[3];

                switch (role) {
                    case "admin":
                        users.put(username, new Admin(userID, username, password, role, null));
                        break;
                    case "student":
                        if (parts.length >= 5) {
                            String program = parts[4];
                            users.put(username, new Student(userID, username, password, role, program));
                        } else {
                            System.out.println("Invalid student data format: " + line);
                        }
                        break;
                    case "professor":
                        if (parts.length >= 5) {
                            String name = parts[4];
                            users.put(username, new Professor(userID, username, password, role, name));
                        } else {
                            System.out.println("Invalid professor data format: " + line);
                        }
                        break;
                    default:
                        System.out.println("Unknown role: " + role);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
    

    public boolean login(String username, String password) {
        if (getUsername().equals(username) && getPassword().equals(password)) {
            isLoggedIn = true;
            System.out.println("Logged in successfully.");
            return true;
        } else {
            System.out.println("Invalid username or password.");
            return false;
        }
    }

    public void logout() {
        isLoggedIn = false;
        System.out.println("Logged out successfully.");
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
