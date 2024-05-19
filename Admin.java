import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Admin extends User {
    String temp;
    private static HashMap<String, String> adminCredentials = new HashMap <>();
    private static HashMap<String, Section> sections = new HashMap <>();
    private static HashMap<String, Course> courses = new HashMap <>();
    private static final List<String> VALID_SEMESTERS = Arrays.asList("Spring", "Summer", "Fall");

    static {
        adminCredentials.put("admin", "password");
    }

    //Constructors
    public Admin(String userID, String username, String password, String role, String temp) {
		super(userID, username, password, role);
		this.temp = temp;
	}

    public Admin (String userID, String username, String password, String role){
        super(userID, username, password, role);
    }

    
    // add Users function
    public void addUser(String newUserID, String newUsername, String newPassword, String newRole) {
        if (newRole.equalsIgnoreCase("admin")) {
            System.out.println("Cannot add another admin user.");
            return;
        }

        // Check if the user ID already exists
        if (userExists(newUserID)) {
            System.out.println("User ID '" + newUserID + "' already exists. Please choose a different ID.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            String newUserLine = newUserID + "," + newUsername + "," + newPassword + "," + newRole;
            
            if (newRole.equalsIgnoreCase("student")) {
                System.out.print("Enter student program: ");
                String program = new Scanner(System.in).nextLine();
                newUserLine += "," + program;
            } else if (newRole.equalsIgnoreCase("professor")) {
                System.out.print("Enter professor name: ");
                String name = new Scanner(System.in).nextLine();
                newUserLine += "," + name;
            }
            
            writer.write(newUserLine);
            writer.newLine();
            System.out.println("User added successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean userExists(String userID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1) { // Ensure there's at least one attribute (user ID)
                    String existingUserID = parts[0];
                    if (existingUserID.equals(userID)) {
                        return true; // User ID already exists
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // User ID does not exist
    }
    
    // remove Users function
    public void removeUser(String username) {
        if (username.equals("admin")) {
            System.out.println("Cannot remove admin user.");
            return;
        }

        try {
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String existingUsername = parts[1];
                if (!existingUsername.equals(username)) {
                    lines.add(line);
                }
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"));
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }
            writer.close();

            System.out.println("User removed successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // add Sections function
    public void addSection(Section section){
        if (!sectionExists(section.getID())) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter course name: ");
            String courseName = scanner.nextLine();

            System.out.print("Enter professor name: ");
            String professorName = scanner.nextLine();

            System.out.print("Enter semester (Spring/Summer/Fall): ");
            String semester = scanner.nextLine();
            if (!VALID_SEMESTERS.contains(semester)) {
                System.out.println("Invalid semester. Semester must be one of: " + VALID_SEMESTERS);
                return;
            }

            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            System.out.print("Enter time slot: ");
            String timeSlot = scanner.nextLine();

            System.out.print("Enter school year (e.g., 2023-2024): ");
            String schoolYear = scanner.nextLine();

            // Create a new Section object
            section = new Section(section.getID(), new Course(courseName), new Professor(professorName),
                    semester, description, timeSlot, schoolYear);
            saveSectionToFile(section);
            sections.put(section.getID(), section);
            System.out.println("Section '" + section.getID() + "' added successfully!");
        } else {
            System.out.println("Section '" + section.getID() + "' already exists!");
        }
    }
    // remove Sections function
    public void removeSection(Section section) {
        if (sectionExists(section.getID())) {
            removeSectionFromFile(section);
            sections.remove(section);
            System.out.println("Section '" + section.getID() + "' removed successfully!");
        } else {
            System.out.println("Section '" + section.getID() + "' does not exist!");
        }
    }

    // check whether the sections exist
    private boolean sectionExists(String ID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("sections.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(ID)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    // save the data to new file
    private void saveSectionToFile(Section section) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("sections.txt", true))) {
            writer.write(section.getID() + "," +
                         section.getCourse().getCourseName() + "," +
                         section.getProfessor().getName() + "," +
                         section.getSemester() + "," +
                         section.getDescription() + "," +
                         section.getTimeSlot() + "," +
                         section.getSchoolYear());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // remove Sections from file
    private void removeSectionFromFile(Section section) {
        try (BufferedReader reader = new BufferedReader(new FileReader("sections.txt"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("temp.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals(section)) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Rename the temp file to original file
        File originalFile = new File("sections.txt");
        File tempFile = new File("temp.txt");
        if (!tempFile.renameTo(originalFile)) {
            System.out.println("Error occurred when removing section.");
         }
    }

    public void addCourse(Course course) {
        if (!courseExists(course.getCourseID())) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter course name: ");
            String courseName = scanner.nextLine();
    
            System.out.print("Enter credits: ");
            int credits = scanner.nextInt();
            scanner.nextLine(); // consume new line
    
            System.out.print("Enter course description: ");
            String description = scanner.nextLine();
    
            // Create a new Course object with the provided data
            course = new Course(course.getCourseID(), courseName, credits, description);
            saveCourseToFile(course);
            courses.put(course.getCourseID(), course);
            System.out.println("Course '" + course.getCourseID() + "' added successfully!");
        } else {
            System.out.println("Course '" + course.getCourseID() + "' already exists!");
        }
    }
    

    public void removeCourse(Course course) {
        if (courseExists(course.getCourseName())) {
            removeCourseFromFile(course);
            courses.remove(course);
            System.out.println("Course '" + course.getCourseName() + "' removed successfully!");
        } else {
            System.out.println("Course '" + course.getCourseName() + "' does not exist!");
        }
    }

    private boolean courseExists(String courseName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("courses.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(courseName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveCourseToFile(Course course) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("courses.txt", true))) {
            writer.write(course.getCourseID() + "," +
                         course.getCourseName() + "," +
                         course.getCredits() + "," +
                         course.getCourseDescription());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeCourseFromFile(Course course) {
        try (BufferedReader reader = new BufferedReader(new FileReader("courses.txt"));
             BufferedWriter writer = new BufferedWriter(new FileWriter("temp.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals(course)) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Rename the temp file to original file
        File originalFile = new File("courses.txt");
        File tempFile = new File("temp.txt");
        if (!tempFile.renameTo(originalFile)) {
            System.out.println("Error occurred when removing course.");
        }
    }

}
