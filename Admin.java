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

    public Admin (String userID, String username, String password, String role){
        super(userID, username, password, role);
    }

    public Admin(String userID, String username, String password, String role, String temp) {
		super(userID, username, password, role);
		this.temp = temp;
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

        boolean userFound = false;
        try {
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String existingUsername = parts[1];
                if (existingUsername.equals(username)) {
                    userFound = true; // found, don't add it to the updated list
                } else {
                    lines.add(line); // Add other courses to the updated list
                }
            }
            reader.close();

            if (!userFound) {
                System.out.println("No user found with the username: " + username);
                return;
            }

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
            if (!courseExists(courseName)) {
                System.out.println("Course '" + courseName + "' does not exist. Cannot add section.");
                return;
            }

            System.out.print("Enter professor name: ");
            String professorName = scanner.nextLine();
            if (!findProfessorByName(professorName)) {
                System.out.println("Professor '" + professorName + "' does not exist. Cannot add section.");
                return;
            }
            
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

    private boolean findProfessorByName(String professorName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[4].trim().equalsIgnoreCase(professorName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // remove Section method
    public void removeSection(String sectiontoremove) {
        boolean sectionFound = false;
        try {
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader("sections.txt"));
            String line; 
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String existingSection = parts[0].trim().toLowerCase();
                if (existingSection.equals(sectiontoremove.replaceAll("\\s", "").toLowerCase())) {
                    sectionFound = true; // Course found, don't add it to the updated list
                } else {
                    lines.add(line); // Add other courses to the updated list
                }
            }
            reader.close();

            if (!sectionFound) {
                System.out.println("No section found with the ID: " + sectiontoremove);
                return;
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter("sections.txt"));
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }
            writer.close();

            System.out.println("Section removed successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // check whether the sections exist
    private boolean sectionExists(String sectionID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("sections.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].trim().equalsIgnoreCase(sectionID)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // save the Section to new file
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
    
    //add Course method
    public void addCourse(Course course) {
        if (!courseExists(course.getCourseName())) {
            Scanner scanner = new Scanner(System.in);
    
            System.out.print("Enter credits: ");
            int credits = scanner.nextInt();
            scanner.nextLine(); // consume new line
    
            System.out.print("Enter course description: ");
            String description = scanner.nextLine();
    
            // Create a new Course object with the provided data
            course = new Course(course.getCourseName(), credits, description);
            saveCourseToFile(course);
            courses.put(course.getCourseName(), course);
            System.out.println("Course '" + course.getCourseName() + "' added successfully!");
        } else {
            System.out.println("Course '" + course.getCourseName() + "' already exists!");
        }
    }
    
    private boolean courseExists(String courseName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("courses.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].trim().equalsIgnoreCase(courseName)) {
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
            writer.write(course.getCourseName() + "," +
                         course.getCredits() + "," +
                         course.getCourseDescription());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // remove Courses function
    public void removeCourse(String courseName) {
        boolean courseFound = false;
        try {
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader("courses.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String existingCourse = parts[0].trim().toLowerCase();
                if (existingCourse.equals(courseName.replaceAll("\\s", "").toLowerCase())) {
                    courseFound = true; // Course found, don't add it to the updated list
                } else {
                    lines.add(line); // Add other courses to the updated list
                }
            }
            reader.close();
    
            if (!courseFound) {
                System.out.println("No course found with the name: " + courseName);
                return;
            }
    
            BufferedWriter writer = new BufferedWriter(new FileWriter("courses.txt"));
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }
            writer.close();
    
            System.out.println("Course removed successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
