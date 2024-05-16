import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

         // Check if the database exists, if not, create it
        File database = new File("sections.txt");
        if (!database.exists()) {
            try {
                database.createNewFile();
                FileWriter writer = new FileWriter(database);
                // Write some initial data to the database
                writer.write("CS101,Computer Science,John Doe,Fall,Intro to CS,AM,2024\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File usersDatabase = new File("users.txt");
        if (!usersDatabase.exists()) {
            try {
                usersDatabase.createNewFile();
                FileWriter writer = new FileWriter(usersDatabase);
                // Write some initial data to the database
                // The format is: userID,username,password,role
                writer.write("1,student1,password,student\n");
                writer.write("2,professor1,password,professor\n");
                writer.write("3,admin1,password,admin\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Map<String, User> users = User.readUsersFromDatabase();

        Scanner scanner = new Scanner(System.in);
        User currentUser = null;
        while (true) {
            if (currentUser == null) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                User user = users.get(username);
                if (user != null && user.getPassword().equals(password)) {
                    currentUser = user;
                    System.out.println("Logged in successfully as " + user.getRole() + ".");
                } else {
                    System.out.println("Invalid username or password.");
                    continue;
                }
            }

            if (currentUser instanceof Student) {
                // Accessing the student object from database
                Student currentStudent = (Student) currentUser;
                List<Section> foundSections = null;
                System.out.println("1. Search sections by course ID");
                System.out.println("2. Search sections by course name");
                System.out.println("3. Search sections by professor");
                System.out.println("4. Log out");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                switch (choice) {
                    case 1:
                        System.out.print("Enter course ID: ");
                        String courseid = scanner.nextLine();
                        foundSections = currentStudent.searchSectionsByCourseID(courseid);
                        System.out.println("Sections found: ");
                        foundSections.forEach(System.out::println);
                        System.out.println("1. Register for a section");
                        System.out.println("2. Back to main menu");
                        System.out.print("Enter your choice: ");
                        int choice1 = scanner.nextInt();
                        scanner.nextLine(); // consume newline
                        if (choice1 == 1) {
                            System.out.print("Enter section ID to register: ");
                            String sectionID = scanner.nextLine();
                            Section sectionToRegister = foundSections.stream()
                            .filter(s->s.getID().equals(sectionID))
                            .findFirst()
                            .orElse(null);
                            if (sectionToRegister == null) {
                                System.out.println("No section found with the given ID.");
                            }
                            currentStudent.registerSection(sectionToRegister);
                            System.out.println("Registration successful!");
                        }
                        break;
                    case 2:
                        System.out.print("Enter course name: ");
                        String coursename = scanner.nextLine();
                        foundSections = currentStudent.searchSectionsByCourseName(coursename);
                        System.out.println("Sections found: ");
                        foundSections.forEach(System.out::println);
                        System.out.println("1. Register for a section");
                        System.out.println("2. Back to main menu");
                        System.out.print("Enter your choice: ");
                        int choice2 = scanner.nextInt();
                        scanner.nextLine(); // consume newline
                        if (choice2 == 1) {
                            System.out.print("Enter section ID to register: ");
                            String sectionID = scanner.nextLine();
                            Section sectionToRegister = foundSections.stream()
                            .filter(s->s.getID().equals(sectionID))
                            .findFirst()
                            .orElse(null);
                            if (sectionToRegister == null) {
                                System.out.println("No section found with the given ID.");
                            }
                            currentStudent.registerSection(sectionToRegister);
                            System.out.println("Registration successful!");
                        }
                        break;
                    case 3:
                        System.out.print("Enter professor name: ");
                        String name = scanner.nextLine();
                        Professor prof = new Professor(name);
                        foundSections = currentStudent.searchSectionsByProfessor(prof);
                        System.out.println("Sections found: " + foundSections);
                        System.out.println("1. Register for a section");
                        System.out.println("2. Back to main menu");
                        System.out.print("Enter your choice: ");
                        int choice3 = scanner.nextInt();
                        scanner.nextLine(); // consume newline
                        if (choice3 == 1) {
                            System.out.print("Enter section ID to register: ");
                            String sectionID = scanner.nextLine();
                            Section sectionToRegister = foundSections.stream()
                            .filter(s -> s.getID().equals(sectionID))
                            .findFirst()
                            .orElse(null);
                            if (sectionToRegister == null) {
                                System.out.println("No section found with the given ID.");
                            }
                            currentStudent.registerSection(sectionToRegister);
                            System.out.println("Registration successful!");
                        }
                        break;
                    case 4:
                        currentUser.logout();
                        currentUser = null;
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                }
            }
            if (currentUser instanceof Professor) {
                System.out.println("Professor currently has no access to the program. Please log in as student or admin.");
                currentUser.logout();
                currentUser = null;
                continue;
            }
            
            //code for admin
            else {
                    System.out.println("Access denied. Only students can access these options.");
                    currentUser.logout();
                    currentUser = null;
                    continue;
                }
        }
    }
}    

