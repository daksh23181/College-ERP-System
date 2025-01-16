import java.util.*;

public class CourseManager {
    private List<Course> courses;

    public CourseManager() {
        courses = new ArrayList<>();
    }

    public void addCourse(String courseCode, String courseName, int credits, int semester, String prerequisites, String timings, String syllabus, int enrollmentLimit, String officeHours) {
        Course newCourse = new Course(courseCode, courseName, credits, semester, prerequisites, timings, syllabus, enrollmentLimit, officeHours);
        courses.add(newCourse);
    }

    public void deleteCourse(String courseCode) {
        boolean found = false;
        for (Course c : courses) {
            if (c.getCourseCode().equals(courseCode)) {
                courses.remove(c);
                System.out.println("\nCourse deleted.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("\nCourse with code " + courseCode + " not found.");
        }
    }

    public List<Course> getCoursesList() {
        return courses;
    }

    public void viewCourses() {
        if (courses.isEmpty()) {
            System.out.println("\nNo courses available.\n");
        } 
        else {
            System.out.println();
            for (Course i : courses) {
                System.out.println(i);
            }
        }
    }

    public void viewDetailedCourses() {
        if (courses.isEmpty()) {
            System.out.println("\nNo courses available.\n");
        } 
        else {
            System.out.println();
            for (Course i : courses) {
                System.out.println(i);
                System.out.println("Professor: " + (i.getProfessor() != null ? i.getProfessor().getEmail() : "None"));
                System.out.println("Pre-requisite: " + i.getPrerequisite());
                System.out.println("Timings: " + i.getTimings() + "\n");
            }
        }
    }

    public Course getCourseByCode(String courseCode) {
        for (Course c : courses) {
            if (c.getCourseCode().equals(courseCode)) {
                return c;
            }
        }
        return null;
    }

    
    public class Course {
        private String courseCode;
        private String courseName;
        private int credits;
        private int semester;
        private Professor professor;
        private String prerequisites;
        private String timings;
        private String syllabus;
        private int enrollmentLimit;
        private String officeHours;
        private List<Student> enrolledStudents;
        private List<Feedback<?>> feedbackList = new ArrayList<>();


        public Course(String courseCode, String courseName, int credits, int semester, String prerequisites, String timings, String syllabus, int enrollmentLimit, String officeHours) {
            this.courseCode = courseCode;
            this.courseName = courseName;
            this.credits = credits;
            this.semester = semester;
            this.professor = null;
            this.prerequisites = prerequisites;
            this.timings = timings;
            this.syllabus = syllabus;
            this.enrollmentLimit = enrollmentLimit;
            this.officeHours = officeHours;
            this.enrolledStudents = new ArrayList<>();
        }

        // Getters
        public String getCourseCode() {
            return courseCode;
        }
        public String getCourseName() {
            return courseName;
        }
        public int getCredits() {
            return credits;
        }
        public int getSemester() {
            return semester;
        }
        public Professor getProfessor() {
            return professor;
        }
        public String getPrerequisite() {
            return prerequisites;
        }
        public String getTimings() {
            return timings;
        }
        public String getSyllabus() {
            return syllabus;
        }
        public int getLimit() {
            return enrollmentLimit;
        }
        public String getOfficeHours() {
            return officeHours;
        }
        public List<Student> getEnrolledStudents() {
            return enrolledStudents;
        }

        // Setters
        public void setProfessor(Professor professor) {
            this.professor = professor;
        }
        public void setSyllabus(String syllabus) {
            this.syllabus = syllabus;
        }
        public void setTimings(String timings) {
            this.timings = timings;
        }
        public void setCredits(int credits) {
            this.credits = credits;
        }
        public void setPrerequisites(String prerequisites) {
            this.prerequisites = prerequisites;
        }
        public void setEnrollmentLimit(int enrollmentLimit) {
            this.enrollmentLimit = enrollmentLimit;
        }
        public void setOfficeHours(String officeHours) {
            this.officeHours = officeHours;
        }

        public void enrollStudent(Student student) throws CourseFullException {
            if (enrolledStudents.size() < enrollmentLimit) {
                enrolledStudents.add(student);
            } 
            else {
                throw new CourseFullException("Enrollment limit reached for " + courseName);
            }
        }

        public void addFeedback(Feedback<?> feedback) {
            feedbackList.add(feedback);
        }
        public List<Feedback<?>> getFeedbackList() {
            return feedbackList;
        }

        @Override
        public String toString() {
            return courseCode + ": " + courseName + " (" + credits + " credits, Semester: " + semester + ")";
        }
    }
}

