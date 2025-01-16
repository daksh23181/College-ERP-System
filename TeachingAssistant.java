import java.util.*;

public class TeachingAssistant extends Student {
    private String assignedCourse;

    public TeachingAssistant(String email, String password, String name, String phone) {
        super(email, password, name, phone);
        this.assignedCourse = null;
    }

    public void assignToCourse(String code) {
        this.assignedCourse = code;
    }

    @Override
    public void login(String email, String password) throws InvalidLoginException {
        if (!this.email.equals(email) || !this.password.equals(password)) {
            throw new InvalidLoginException("Invalid email or password.");
        }
        System.out.println("Logged in successfully as Teaching Assistant (TA).");
        System.out.println("Welcome " + email + " !!");
    }

    @Override
    public void userMode(Scanner scanner) {
        while (true) {
            System.out.println("\nSelect Mode: \n1) Student Mode \n2) TA Mode \n3) Logout");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                super.userMode(scanner);
            }
            else if (choice == 2) {
                if (assignedCourse == null) {
                    System.out.println("\nYou are not assigned as TA to any course.");
                }
                else {
                    TAMode(scanner);
                }
            }
            else if (choice == 3) {
                System.out.println("Logged out.");
                break;
            }
            else {
                System.out.println("Invalid choice");
            }
        }
    }

    private void TAMode(Scanner scanner) {
        while (true) {
            System.out.println("\nTA Mode:");
            System.out.println("1) View Assigned course details");
            System.out.println("2) View Grades of Students \n3) Update Grades of Students \n4) Logout");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                CourseManager.Course course = course_manager.getCourseByCode(assignedCourse);
                if (course == null) {
                    System.out.println("Course not found.");
                }
                else {
                    System.out.println();
                    System.out.println(course);
                    System.out.println("Professor: " + (course.getProfessor() != null ? course.getProfessor().getEmail() : "TBA"));
                    System.out.println("Pre-requisite: " + course.getPrerequisite());
                    System.out.println("Timings: " + course.getTimings());
                    System.out.println("Syllabus: " + course.getSyllabus());
                    System.out.println("Enrollment Limit: " + course.getLimit());
                    System.out.println("Office Hours: " + course.getOfficeHours());
                }
            }
            else if (choice == 2) {
                viewGrades(Main.students);
            }
            else if (choice == 3) {
                updateGrades(scanner, Main.students);
            }
            else if (choice == 4) {
                System.out.println("Logged out from TA Mode.");
                break;
            }
            else {
                System.out.println("Invalid choice");
            }
        }
    }

    public void viewGrades(Map<String, Student> students) {
        System.out.println("\nGrades of students who has completed course: " + assignedCourse);
        for (Map.Entry<String, Student> entry : students.entrySet()) {
            Student student = entry.getValue();

            if (student.getCompletedCourses().contains(assignedCourse)) {
                String grade = student.getGradeForCourse(assignedCourse);
                System.out.println(student.getName() + " - Grade: " + grade);
            }
        }
    }

    public void updateGrades(Scanner scanner,Map<String, Student> students) {
        System.out.println("\nAssigned Course : " + assignedCourse);
        System.out.println("\nEnter student email to update grade for :" + assignedCourse);
        String email = scanner.nextLine();

        Student student = students.get(email);
        if (student == null) {
            System.out.println("\nNo student found with email: " + email);
            return;
        }
        else {
            System.out.println("Enter updated grade (A, B, C, D, F):");
            String grade = scanner.nextLine();
            student.updateGrade(assignedCourse,grade);
        }
    }

}

