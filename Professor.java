import java.util.*;

public class Professor extends User {
    private String assignedCourse;
    private CourseManager courseManager; 

    public Professor(String email, String password) {
        super(email, password);
        this.assignedCourse = null;
    }

    public String getAssignedCourse() {
        return assignedCourse;
    }
    public void assignToCourse(String courseCode) {
        this.assignedCourse = courseCode;
    }
    public boolean isAvailable() {
        return assignedCourse == null;
    }
    public String getEmail() {
        return this.email;
    }
    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    @Override
    public void login(String email, String password) throws InvalidLoginException {
        if (!this.email.equals(email) || !this.password.equals(password)) {
            throw new InvalidLoginException("Invalid email or password.");
        }
        System.out.println("Logged in successfully as Professor.");
        System.out.println("Welcome " + email + " !!");
    }

    @Override
    public void signUp() {
        System.out.println("Username and password created successfully.");
    }

    @Override
    public void userMode(Scanner scanner) {
        while (true) {
            System.out.println("\nProfessor Mode:");
            System.out.println("1) Manage Courses \n2) View Enrolled Students \n3) View Feedback \n4) Assign TA \n5) Logout");
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 5) {
                System.out.println("Logged out from Professor mode.");
                break;
            } 
            else if (option == 1) {
                Manage_Courses(scanner);
            } 
            else if (option == 2){
                View_Enrolled_Students();
            }
            else if (option == 3) {
                viewCourseFeedback(assignedCourse);
            }
            else if (option == 4) {
                assignTA(scanner,Main.students);
            }
            else {
                System.out.println("Invalid option.");
            }
        }
    }

    private void Manage_Courses(Scanner scanner) {
        if (assignedCourse == null) {
            System.out.println("\nYou are not assigned to any course.");
            return;
        }
    
        CourseManager.Course course = courseManager.getCourseByCode(assignedCourse);
        if (course == null || course.getProfessor() == null || !course.getProfessor().equals(this)) {
            System.out.println("\nCourse not found or you are not assigned to this course.");
            return;
        }
    
        System.out.println("\nManaging Course: " + course.getCourseName());
        System.out.println("1) View Course Details \n2) Update Course Details \n3) Back");
        int option = scanner.nextInt();
        scanner.nextLine();
    
        if (option == 1) {
            System.out.println();
            System.out.println(course);
            System.out.println("Pre-requisite: " + course.getPrerequisite());
            System.out.println("Timings: " + course.getTimings());
            System.out.println("Syllabus: " + course.getSyllabus());
            System.out.println("Enrollment Limit: " + course.getLimit());
            System.out.println("Office Hours: " + course.getOfficeHours());
        } 
        else if (option == 2) {
            updateCourseDetails(scanner, course);
        }
    }

    private void updateCourseDetails(Scanner scanner, CourseManager.Course course) {
        System.out.println("\nUpdate Course Details:");
        System.out.println("1) Update Syllabus  \n2) Update Class Timings  \n3) Update Credits ");
        System.out.println("4) Update Prerequisites  \n5) Update Enrollment Limit  \n6) Update Office Hours");
        int k = scanner.nextInt();
        scanner.nextLine();

        switch (k) {
            case 1:
                System.out.println("Enter new syllabus:");
                String newSyllabus = scanner.nextLine();
                course.setSyllabus(newSyllabus);
                System.out.println("\nSyllabus updated.");
                break;
            case 2:
                System.out.println("Enter new class timings:");
                String newTimings = scanner.nextLine();
                course.setTimings(newTimings);
                System.out.println("\nClass timings updated.");
                break;
            case 3:
                System.out.println("Enter new credits:");
                int newCredits = scanner.nextInt();
                scanner.nextLine();
                course.setCredits(newCredits);
                System.out.println("\nCredits updated.");
                break;
            case 4:
                System.out.println("Enter new prerequisites:");
                String newPreReq = scanner.nextLine();
                course.setPrerequisites(newPreReq);
                System.out.println("\nPrerequisites updated.");
                break;
            case 5:
                System.out.println("Enter new enrollment limit:");
                int newLimit = scanner.nextInt();
                scanner.nextLine();
                course.setEnrollmentLimit(newLimit);
                System.out.println("\nEnrollment limit updated.");
                break;
            case 6:
                System.out.println("Enter new office hours:");
                String newOfficeHours = scanner.nextLine();
                course.setOfficeHours(newOfficeHours);
                System.out.println("\nOffice hours updated.");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
    
    
    private void View_Enrolled_Students() {
        if (assignedCourse == null) {
            System.out.println("\nYou are not assigned to any course.");
            return;
        }
    
        CourseManager.Course course = courseManager.getCourseByCode(assignedCourse);
        if (course == null || course.getProfessor() == null || !course.getProfessor().equals(this)) {
            System.out.println("\nCourse not found or you are not assigned to this course.");
            return;
        }
    
        List<Student> enrolledStudents = course.getEnrolledStudents();
        if (enrolledStudents == null || enrolledStudents.isEmpty()) {
            System.out.println("\nNo students are enrolled in your course.");
            return;
        }

        System.out.println("\nEnrolled Students: \n");
        for (Student student : enrolledStudents) {
            System.out.println("Name: " + student.getName());
            System.out.println("Email: " + student.email);
            System.out.println("Contact Details: " + student.getContactDetail());
            System.out.println();
        }
    }

    public void viewCourseFeedback(String code) {
        CourseManager.Course course = courseManager.getCourseByCode(code);
        if (course != null && assignedCourse.contains(code)) {
            System.out.println("\nFeedback for course: " + code + "\n");
            for (Feedback<?> feedback : course.getFeedbackList()) {
                System.out.println(feedback.toString());
            }
        } 
        else {
            System.out.println("You are not assigned to any course or no feedback available.");
        }
    }

    public void assignTA(Scanner scanner, Map<String, Student> students) {
        if (assignedCourse == null) {
            System.out.println("\nYou are not assigned to any course.");
            return;
        }

        System.out.println("\nAvailable TA's :");
        int i=1;
        for (Map.Entry<String, Student> entry : students.entrySet()) {
            String email = entry.getKey();
            if (email.endsWith("@ta")) {
                System.out.println(i + ") " + email);
                i++;
            }
        }
        System.out.println("\nEnter the email to assign as TA:");
        String taEmail = scanner.nextLine();

        Student student = students.get(taEmail);
        if (student instanceof TeachingAssistant ta) {
            ta.assignToCourse(assignedCourse);
            System.out.println("\nTA " + ta.getName() + " has been assigned to course " + assignedCourse);
        }
        else {
            System.out.println("\nNo such TA exists.");
        }
    }

}
