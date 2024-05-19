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
                writer.write("1,student1,password,student, CS\n");
                writer.write("2,professor1,password,professor, Professor\n");
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
            
            //Student
            if (currentUser instanceof Student) {
                // Accessing the student object from database
                Student currentStudent = (Student) currentUser;
                List<Section> foundSections = null;
                System.out.println("1. Change info");
                System.out.println("2. Search sections by course name");
                System.out.println("3. Search sections by professor");
                System.out.println("4. Log out");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                switch (choice) {
                    case 1:
                        currentStudent.changeinfo(currentStudent.getUserID(), currentStudent.getProgram(),
                        currentStudent.getRole());
                        currentStudent.deleteFile();
					    currentStudent.SaveData();
					    System.out.println("save data to file");
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
                    case 3:
                        System.out.print("Enter professor name: ");
                        String name = scanner.nextLine();
                        Professor prof = new Professor(name);
                        foundSections = currentStudent.searchSectionsByProfessor(prof);
                        System.out.println("Sections found: " + foundSections);
                        System.out.println("1. Register for a section");
                        System.out.println("2. Back to main menu");
                        System.out.print("Enter your choice: ");
                        int choice2 = scanner.nextInt();
                        scanner.nextLine(); // consume newline
                        if (choice2 == 1) {
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
            if (currentUser instanceof Admin) {
                // Accessing the admin object from database
                Admin currentAdmin = (Admin) currentUser;
                System.out.println("1. Add student account");
                System.out.println("2. Remove student account");
                System.out.println("3. Add professor account");
                System.out.println("4. Remove professor account");
                System.out.println("5. Add sections");
                System.out.println("6. Remove sections");
                System.out.println("7. Add courses");
                System.out.println("8. Remove courses");
                System.out.println("9. Log out");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                switch (choice) {
                    case 1:
                        System.out.print("Enter user ID: ");
                        String studentID = scanner.nextLine();
                        System.out.print("Enter username: ");
                        String studentName = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String studentPassword = scanner.nextLine();
                        currentAdmin.addUser(studentID, studentName, studentPassword, "student");
                        break;
                    case 2:
                        System.out.print("Enter username of student to remove: ");
                        String studentToRemove = scanner.nextLine();
                        currentAdmin.removeUser(studentToRemove);
                        break;
                    case 3:
                        System.out.print("Enter user ID: ");
                        String professorID = scanner.nextLine();
                        scanner.nextLine();
                        System.out.print("Enter username: ");
                        String professorName = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String professorPassword = scanner.nextLine();
                        currentAdmin.addUser(professorID, professorName, professorPassword, "professor");
                        break;
                    case 4:
                        System.out.print("Enter username of professor to remove: ");
                        String professorToRemove = scanner.nextLine();
                        currentAdmin.removeUser(professorToRemove);
                        break;
                    case 5:
                        System.out.print("Enter the ID of the section to add: ");
                        String sectionIDToAdd = scanner.nextLine();
                        currentAdmin.addSection(new Section(sectionIDToAdd));
                        break;
                    case 6:
                        System.out.print("Enter the ID of the section to remove: ");
                        String sectionIDToRemove = scanner.nextLine();
                        currentAdmin.removeSection(new Section(sectionIDToRemove));
                        break;
                    case 7:
                        System.out.print("Enter the name of the course to add: ");
                        String coursenameToAdd = scanner.nextLine();
                        currentAdmin.addCourse(new Course(coursenameToAdd));
                        break;
                    case 8: 
                        System.out.print("Enter the name of the course to remove: ");
                        String courseNameToRemove = scanner.nextLine();
                        currentAdmin.removeCourse(new Course(courseNameToRemove));
                        break;
                    case 9:
                        currentUser.logout();
                        currentUser = null;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                }
            }
        }
    }
}    

