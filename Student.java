import java.util.*;
import java.time.*;

public class Student extends User {

    private String name;
    private String ContactDetail;
    private int currentSemester;
    private int registeredCredits;    
    public List<String> completedCourses;
    private List<String> registeredCourses;
    private Map<String, String> courseGrades;
    private List<Complaint> submittedComplaints;
    public CourseManager course_manager;
    private static List<Complaint> complaints = new ArrayList<>();
    private static LocalDate dropDate = null;

    public Student(String email, String password, String name, String ContactDetail) {
        super(email, password);
        this.name = name;
        this.ContactDetail = ContactDetail;
        submittedComplaints = new ArrayList<>();
        this.currentSemester = 1;
        this.registeredCredits = 0;
        this.completedCourses = new ArrayList<>();
        this.registeredCourses = new ArrayList<>();
        this.courseGrades = new HashMap<>();
    }

    // Getters
    public int getCurrentSemester() {
        return currentSemester;
    }
    public int getRegisteredCredits() {
        return registeredCredits;
    }
    public String getName() {
        return name;
    }
    public String getContactDetail() {
        return ContactDetail;
    }
    public List<String> getCompletedCourses() {
        return completedCourses;
    }
    public List<String> getRegisteredCourses() {
        return registeredCourses;
    }
    public String getGradeForCourse(String courseCode) {
        return courseGrades.getOrDefault(courseCode, "NA");
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setGrade(String courseCode, String grade) {
        if (registeredCourses.contains(courseCode)) {
            courseGrades.put(courseCode, grade);
            System.out.println("\nGrade set for course " + courseCode + ": " + grade);
            System.out.println("Grade assigned successfully.");
        } 
        else {
            System.out.println("\nCannot set grade. Course not found in registered courses.");
        }
    }
    public void updateGrade(String courseCode, String grade) {
        if (completedCourses.contains(courseCode)) {
            courseGrades.put(courseCode, grade);
            System.out.println("\nGrade updated successfully for course " + courseCode + " to " + grade);
        }
        else {
            System.out.println("\nCannot update grade. Course not found in completed courses.");
        }
    }
    public void setCourseManager(CourseManager courseManager) {
        this.course_manager = courseManager;
    } 
    public static void setComplaints(List<Complaint> complaints) {
        Student.complaints = complaints;
    }
    public static void setDropDate(LocalDate dropDate) {
        Student.dropDate = dropDate;
    }

    @Override
    public void login(String email, String password) throws InvalidLoginException {
        if (!this.email.equals(email) || !this.password.equals(password)) {
            throw new InvalidLoginException("Invalid email or password.");
        }
        System.out.println("Logged in successfully as Student.");
        System.out.println("Welcome " + email + " !!");
    }

    @Override
    public void signUp() {
        System.out.println("Username and password created successfully.");
    }

    @Override
    public void userMode(Scanner scanner) {
        while (true) {
            System.out.println("\nStudent Mode:");
            System.out.println("1) View Available Courses \n2) Register for Courses \n3) View Registered Courses \n4) View Schedule");
            System.out.println("5) Track Academic Progress \n6) Drop Courses \n7) Submit Complaints");
            System.out.println("8) View My Complaints \n9) Give Feedback \n10) Logout");
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 10) {
                System.out.println("Logged out from Student mode.");
                break;
            }
            else if (option == 1) {
                System.out.println("\nAvailable Courses for the Current Semester:");
                course_manager.viewDetailedCourses();
            } 
            else if (option == 2) {
                Register_Courses(scanner);
            }
            else if (option == 3) {
                viewRegisteredCourses();
            }
            else if (option == 4) {
                View_Schedule();
            }
            else if (option == 5) {
                Track_Academic_Progress();
            }
            else if (option == 6) {
                Drop_Course(scanner);
            }
            else if (option == 7) {
                Submit_Complaint(scanner);
            } 
            else if (option == 8) {
                view_Complaints();
            } 
            else if (option == 9) {
                if (completedCourses.isEmpty()) {
                    System.out.println("\nYou have not completed any course yet.");
                }
                else {
                    give_Feedback(scanner);
                }
            }
            else {
                System.out.println("Invalid choice.");
            }
        }
    }

    public void Register_Courses(Scanner scanner) {
        System.out.println("Select your current semester: ");
        int semester = scanner.nextInt();
        scanner.nextLine();

        if (semester != this.currentSemester) {
            System.out.println("\nYou can only register for courses in your current semester: " + this.currentSemester);
            return;
        }

        System.out.println("\nAvailable courses for Semester " + semester + ":\n");
        List<CourseManager.Course> availableCourses = getCoursesForSemester(semester);

        if (availableCourses.isEmpty()) {
            System.out.println("\nNo courses available for this semester.");
            return;
        }
        for (CourseManager.Course course : availableCourses) {
            System.out.println(course);
        }
        System.out.println("\nEnter course code to register : ");
        String courseCode = scanner.nextLine();

        CourseManager.Course selectedCourse = course_manager.getCourseByCode(courseCode);

        if (selectedCourse == null) {
            System.out.println("\nInvalid course code.");
            return;
        }
        if (registeredCourses.contains(courseCode)) {
            System.out.println("\nYou are already registered for this course.");
            return;
        }

        if (!CheckPrerequisites(selectedCourse)) {
            System.out.println("\nPrerequisites not met for this course.");
            return;
        }
        if (registeredCredits + selectedCourse.getCredits() > 20) {
            System.out.println("\nCannot register for this course. Credit limit exceeded.");
            return;
        }

        try {
            selectedCourse.enrollStudent(this);
            registeredCourses.add(courseCode);
            registeredCredits += selectedCourse.getCredits();
            System.out.println("\nSuccessfully registered for " + selectedCourse.getCourseName() + "."); 
        } 
        catch (CourseFullException e) {
            System.out.println(e.getMessage());
        }
    }

    private List<CourseManager.Course> getCoursesForSemester(int semester) {
        List<CourseManager.Course> coursesForSemester = new ArrayList<>();
        for (CourseManager.Course course : course_manager.getCoursesList()) {
            if (course.getSemester() == semester) {
                coursesForSemester.add(course);
            }
        }
        return coursesForSemester;
    }

    private boolean CheckPrerequisites(CourseManager.Course course) {
        String prerequisite = course.getPrerequisite();
        if (prerequisite.equals("None")) {
            return true;
        }
        else if (completedCourses.contains(prerequisite)) {
            return true;
        }
        return false;
    }
    
    public void viewRegisteredCourses() {
        if (registeredCourses.isEmpty()) {
            System.out.println("\nYou are not registered for any courses in this semester.");
        } 
        else {
            System.out.println("\nRegistered Courses for Semester " + currentSemester + ":\n");
            for (String courseCode : registeredCourses) {
                CourseManager.Course course = course_manager.getCourseByCode(courseCode);
                if (course != null) {
                    System.out.println("Course Code: " + course.getCourseCode());
                    System.out.println("Course Name: " + course.getCourseName());
                    System.out.println("Credits: " + course.getCredits());
                    System.out.println("Timings: " + course.getTimings());
                    System.out.println("Professor: " + (course.getProfessor() != null ? course.getProfessor().getEmail() : "TBA"));
                    System.out.println("Syllabus: " + course.getSyllabus());
                    System.out.println("Office Hours: " + course.getOfficeHours());
                    System.out.println();
                } 
                else {
                    System.out.println("\nCourse with code " + courseCode + " not found.");
                }
            }
        }
    }

    public void View_Schedule() {
        if (registeredCourses.isEmpty()) {
            System.out.println("\nYou are not registered for any courses in this semester.");
        } 
        else {
            System.out.println("\nYour Weekly Course Schedule: \n");
            for (String Code : registeredCourses) {
                CourseManager.Course course = course_manager.getCourseByCode(Code);
                if (course != null) {
                    System.out.println(course.getCourseCode() + ": " + course.getCourseName());
                    System.out.println("Professor: " + (course.getProfessor() != null ? course.getProfessor().getEmail() : "TBA"));
                    System.out.println("Timings: " + course.getTimings());
                    System.out.println();
                } 
                else {
                System.out.println("\nCourse with code " + Code + " not found.");
                }
            }
        }
    }

    public void Track_Academic_Progress() {
        System.out.println("\nAcademic Progress:");
        if (completedCourses.isEmpty()) {
            System.out.println("No completed courses yet.");
            return;
        }

        System.out.println("Completed Courses and Grades: \n");
        for (String courseCode : completedCourses) {
            CourseManager.Course course = course_manager.getCourseByCode(courseCode);
            String grade = courseGrades.get(courseCode);
            if (course != null || grade != null) {
                System.out.println("Course: " + course.getCourseCode() + " - " + course.getCourseName());
                System.out.println("Grade: " + grade);
                System.out.println();
            }
        }

        float SGPA = calculateSGPA();
        float CGPA = calculateCGPA();
        System.out.println("SGPA for Semester " + (currentSemester-1) + ": " + SGPA);
        System.out.println("CGPA : " + CGPA);
    }

    private float calculateCGPA() {
        int totalCredits = 0;
        int totalPoints = 0;

        for (String courseCode : completedCourses) {
            CourseManager.Course course = course_manager.getCourseByCode(courseCode);
            String grade = courseGrades.get(courseCode);
            if (course != null && grade != null) {
                int credit = course.getCredits();
                totalCredits += credit;

                int point = convert_points(grade);
                totalPoints += point*credit;
            }
        }
        return (float) totalPoints / totalCredits;
    }

    private float calculateSGPA() {
        int totalCredits = 0;
        int totalPoints = 0;

        for (String courseCode : completedCourses) {
            CourseManager.Course course = course_manager.getCourseByCode(courseCode);
            if ((course.getSemester())==(currentSemester-1)){
                String grade = courseGrades.get(courseCode);
                if (grade != null) {
                    int credit = course.getCredits();
                    totalCredits += credit;

                    int point = convert_points(grade);
                    totalPoints += point*credit;
                }
            }
            
        }
        return (float) totalPoints / totalCredits;
    }

    private int convert_points(String grade) {
        int point = 0;
        if (grade.equals("A"))
            point = 10;
        else if (grade.equals("B"))
            point = 8;
        else if (grade.equals("C"))
            point = 6;
        else if (grade.equals("D"))
            point = 4;
        else if (grade.equals("F"))
            point = 2;
        return point;
    }
    
    public void Drop_Course(Scanner scanner) {
        if (registeredCourses.isEmpty()) {
            System.out.println("\nYou are not registered for any courses in this semester.");
            return;
        }
        else {
            LocalDate today = LocalDate.now();
            try {
                if (dropDate != null && today.isAfter(dropDate)) {
                    throw new DropDeadlinePassedException("Drop deadline has passed. You can't drop the course now.");
                }
                else {
                    Drop_Course_Main(scanner);
                }
            }
            catch (DropDeadlinePassedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void Drop_Course_Main(Scanner scanner) {
        System.out.println("\nRegistered Courses:");
        for (String courseCode : registeredCourses) {
            CourseManager.Course course = course_manager.getCourseByCode(courseCode);
            if (course != null) {
                System.out.println(course);
            }
        }

        System.out.println("\nEnter course code to drop: ");
        String courseCode = scanner.nextLine();
        if (!registeredCourses.contains(courseCode)) {
            System.out.println("\nYou are not registered for this course.");
            return;
        }
        CourseManager.Course course = course_manager.getCourseByCode(courseCode);
        if (course != null) {
            registeredCourses.remove(courseCode);
            registeredCredits -= course.getCredits();
            System.out.println("\nSuccessfully dropped " + course.getCourseName() + ".");
        }
        else {
            System.out.println("\nInvalid course code.");
        }
    }

    public void Submit_Complaint(Scanner scanner) {
        System.out.println("Enter complaint description:");
        String description = scanner.nextLine();
        System.out.println("Enter complaint ID:");
        String id = scanner.nextLine();
       
        Complaint complaint = new Complaint(id, description, LocalDate.now());
        submittedComplaints.add(complaint);
        complaints.add(complaint);
        System.out.println("\nComplaint submitted successfully.");
    }

    public void view_Complaints() {
        if (submittedComplaints.isEmpty()) {
            System.out.println("\nNo complaints submitted.\n");
        } 
        else {
            for (Complaint c : submittedComplaints) {
                System.out.println(c);
                System.out.println();
            }
        }
    }

    public void give_Feedback(Scanner scanner) {
        System.out.println("\nEnter the course code to give feedback::");
        String code = scanner.nextLine();
        System.out.println("Enter your feedback \nRating(1-5) or text:");
        Object feedback = scanner.nextLine(); 
                
        CourseManager.Course course = course_manager.getCourseByCode(code);
        if (course != null && completedCourses.contains(code)) {
            Feedback<Object> courseFeedback = new Feedback<>(feedback, this.email);
            course.addFeedback(courseFeedback);
            System.out.println("\nFeedback submitted successfully.");
        } 
        else {
            System.out.println("\nInvalid course code.");
        }
    }

    // This method is called by the admin only at the end of the semester.
    public void completeSemester() {
        for (String courseCode : registeredCourses) {
            if (!completedCourses.contains(courseCode)) {
                completedCourses.add(courseCode); 
            }
        }
        registeredCourses.clear();    
        registeredCredits = 0;   
        this.currentSemester++;
    }
}

