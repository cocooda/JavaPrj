import java.util.ArrayList;
import java.util.List;
import java.nio.file.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



public class Student extends User {
    private String program;
    private List<Section> registeredSections;
    

    public Student(String userID, String username, String password, String role, String program) {
        super(userID, username, password, role);
        this.program = program;
        this.registeredSections = new ArrayList<>();
    }

    public Student(String userID, String username, String password, String role) {
        super(userID, username, password, role);
    }


    // Getter and setter methods for all attributes
    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public List<Section> getRegisteredSections() {
        return registeredSections;
    }
    

    // Method to get the current semester
     private String getCurrentSemester() {
        int month = LocalDate.now().getMonthValue();
        if (month >= 2 && month <= 6) {
            return "Spring";
        } else if (month >= 7 && month <= 9) {
            return "Summer";
        } else {
            return "Fall";
        }
    }

    // Method to read sections from a text file database
    private List<Section> readSectionsFromDatabase() {
        List<Section> sections = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("sections.txt"));
            for (String line : lines) {
                String[] parts = line.split(",");
                Course course = new Course(parts[1]); 
                Professor professor = new Professor(parts[2]); 
                sections.add(new Section(parts[0], course, professor, parts[3], parts[4], parts[5], parts[6]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sections;
    }

    // Method to get the order of the semesters
    private int getSemesterOrder(String semester) {
        Map<String, Integer> order = new HashMap<>();
        order.put("Spring", 1);
        order.put("Summer", 2);
        order.put("Fall", 3);
        return order.getOrDefault(semester, 0);
    }

    /*// Method to search sections by course ID
    public List<Section> searchSectionsByCourseID(String courseID) {
        return readSectionsFromDatabase().stream()
            .filter(section -> section.getCourse().getCourseID().equals(courseID))
            .collect(Collectors.toList());
    }
*/
    // Method to search sections by Course Name
    public List<Section> searchSectionsByCourseName(String courseName) {
        List<Section> allSections = readSectionsFromDatabase();
        List<Section> matchingSections = new ArrayList<>();
    
        String cleanedCourseName = courseName.replaceAll("\\s", "").toLowerCase(); // Remove spaces and convert to lowercase
    
        for (Section section : allSections) {
            String sectionCourseName = section.getCourse().getCourseName().replaceAll("\\s", "").toLowerCase();
            if (sectionCourseName.startsWith(cleanedCourseName)) {
                matchingSections.add(section);
            }
        }
    
        return matchingSections;
    }
    
    
    // Method to search sections by professor
    public List<Section> searchSectionsByProfessor(String professorName) {
        List<Section> allSections = readSectionsFromDatabase();
        List<Section> matchingSections = new ArrayList<>();
    
        String cleanedProfessorName = professorName.toLowerCase().replaceAll("\\s", ""); // Remove spaces and convert to lowercase
    
        for (Section section : allSections) {
            String sectionProfessorName = section.getProfessor().getName().toLowerCase().replaceAll("\\s", "");
            if (sectionProfessorName.startsWith(cleanedProfessorName)) {
                matchingSections.add(section);
            }
        }
    
        return matchingSections;
    }
    
    

    // Method to pick a section and register
    public void registerSection(Section section) {
        String currentYear = String.valueOf(Year.now().getValue());
        String currentSemester = getCurrentSemester();
        if (!registeredSections.contains(section) && section.getSchoolYear().equals(currentYear) && getSemesterOrder(section.getSemester()) > getSemesterOrder(currentSemester)) {
            registeredSections.add(section);
            System.out.println("Registered succesfully.");
            // Update the text file database to reflect the new registration
            try {
                String registrationInfo = this.getUserID() + "," + section.getID() + "\n";
                Files.write(Paths.get("registrations.txt"), registrationInfo.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("You have already registered for this section or the section is currently unavailable.");
        }
    }

    //method to change info

	List<Student> List1 = new ArrayList<Student>();

	public void ReadData() {
		String userID, username, password, role, program;
		try {
			BufferedReader br = new BufferedReader(new FileReader("users.txt"));
			String line = br.readLine();
			while (line != null) {
				String[] value = line.split(",");
				userID = value[0];
				username = value[1];
				password = value[2];
				program = value[3];
				role = value[4];
				List1.add(new Student(userID, username, password, role, program));
				line = br.readLine();
			}
			br.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public Student Search(String userID) {
		for (Student x : List1) {
			if (x.getUserID().contains(userID)) {
				return x;
			}
		}
		return null;
	}

	public void changeinfo(String userID, String program, String role) {
		ReadData();
		Student x = Search(userID);
		if (x != null) {

			Scanner sc = new Scanner(System.in);
			System.out.println("Input new username: ");
			String name = sc.nextLine();
			System.out.println("Input new password: ");
			String password = sc.nextLine();
			List1.set(List1.indexOf(x), new Student(userID, name, password, role, program));
		} else {
			System.out.println("Student not found.");
		}
	}

	public void deleteFile() {
		File file = new File("users.txt");
		if (file.exists()) {
			file.delete();
		}
	}

	public void SaveData() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"));
			for (Student x : List1) {
				writer.write(x.getUserID() + "," + x.getUsername() + "," + x.getPassword() + "," + x.getRole() + ","
						+ x.getProgram() );
				writer.newLine();

			}
			writer.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
