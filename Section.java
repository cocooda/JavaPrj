import java.util.Arrays;
import java.util.List;


public class Section {
    private String ID;
    private Course course; 
    private Professor professor; 
    private String semester; 
    private String description; 
    private String timeSlot; 
    private String schoolYear; 

    private static final List<String> VALID_SEMESTERS = Arrays.asList("Spring", "Summer", "Fall");

    public Section(String ID, Course course, Professor professor, String semester, String description, String timeSlot, String schoolYear) {
        if (!VALID_SEMESTERS.contains(semester)) {
            throw new IllegalArgumentException("Invalid semester. Semester must be one of: " + VALID_SEMESTERS);
        }
        this.ID = ID;
        this.course = course;
        this.professor = professor;
        this.semester = semester;
        this.description = description;
        this.timeSlot = timeSlot;
        this.schoolYear = schoolYear;
    }

    public Section(Course course) {
        this.course = course;
    }

    public Section(String ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return ID;
    }

    // Getter and setter methods for all attributes
    public String getID() {
        return ID;
    }

    public Professor getProfessor() {
        return professor;
    }

    public Course getCourse() {
        return course;
    }


    public String getSemester() {
        return semester;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

}
