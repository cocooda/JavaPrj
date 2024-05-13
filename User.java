import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class User {
    private int userID;
    private String username;
    private String password; 
    private String role;
    private boolean isLoggedIn = false;

    public User(int userID, String username, String password, String role) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters
    public int getUserId() {
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

    // Other methods
    public static Map<String, User> readUsersFromDatabase() {
        Map<String, User> users = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Assume each line in the text file represents a user
                // and the attributes are separated by commas
                int userID = Integer.parseInt(parts[0]);
                String username = parts[1];
                String password = parts[2];
                String role = parts[3];
                switch (role) {
                    case "admin":
                        users.put(username, new Admin(userID, username, password, role));
                        break;
                    case "student":
                        users.put(username, new Student(userID, username, password, role));
                        break;
                    case "professor":
                        users.put(username, new Professor(userID, username, password, role));
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
