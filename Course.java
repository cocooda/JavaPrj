public class Course {
    private String courseID;
    private String courseName;
    private int credits;
    private String courseDescription;

    // Constructor
    public Course (){
        this.courseID = " ";
        this.courseName = " ";
        this.credits = 0;
        this.courseDescription = " ";
    }

    public Course(String courseId, String courseName, int credits, String courseDescription) {
        this.courseID = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.courseDescription = courseDescription;
    }

    public Course(String courseName) {
        this.courseName = courseName;
    }
    
    // Getters
    public String getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public int getCredits(){
        return credits;
    }

    // Setters
    public void setCourseId(String courseID) {
        this.courseID = courseID;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public void setCredits(int credits){
        this.credits = credits;
    }
}
