import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Administrator extends User {

    private CourseManager courseManager;
    private List<Professor> professors;
    private static List<Complaint> complaints = new ArrayList<>();
    private static Map<String, Student> students = new HashMap<>();

    public Administrator(String email, String password, List<Professor> professors) {
        super(email, password);
        this.professors = professors;
    }

    // Setters
    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }
    public void add_Professor(Professor prof) {
        professors.add(prof);
    }
    public static void setComplaints(List<Complaint> complaints) {
        Administrator.complaints = complaints;
    }
    public static void setStudentRecords(Map<String, Student> students) {
        Administrator.students = students;
    }

    @Override
    public void login(String email, String password) throws InvalidLoginException {
        if (!this.email.equals(email) || !this.password.equals(password)) {
            throw new InvalidLoginException("Invalid email or password.");
        }
        System.out.println("Logged in successfully as Admin.");
        System.out.println("Welcome " + email + " !!");
    }

    @Override
    public void signUp() {
    }

    @Override
    public void userMode(Scanner scanner) {
        while (true) {
            System.out.println("\nAdministrator Mode:");
            System.out.println("1) Manage Course Catalog \n2) Manage Student Records");
            System.out.println("3) Assign Professors to Courses \n4) Handle Complaints \n5) Logout");
            int option = scanner.nextInt();
            scanner.nextLine();
            if (option == 5) {
                System.out.println("Logged out from Admin mode.");
                break;
            } 
            else if (option == 1) {
                Manage_Courses(scanner);
            }
            else if (option == 2) {
                Manage_Student_Records(scanner);
            } 
            else if (option == 3) {
                Assign_Professors(scanner);
            }
            else if (option == 4) {
                Handle_Complaints(scanner);
            }
            else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private void Manage_Courses(Scanner scanner) {
        while (true) {
            System.out.println("\nManage Course Catalog:");
            System.out.println("1) View Courses \n2) Add Course \n3) Delete Course ");
            System.out.println("4) Set Deadline for CourseDrop \n5) Back to Main Menu");
            int option = scanner.nextInt();
            scanner.nextLine();
            if (option == 5) {
                break;
            } 
            else if (option == 1) {
                courseManager.viewCourses();
            } 
            else if (option == 2) {
                System.out.println("Enter course code:");
                String courseCode = scanner.nextLine();
                
                System.out.println("Enter course name:");
                String courseName = scanner.nextLine();
                
                System.out.println("Enter credits:");
                int credits = scanner.nextInt();
                scanner.nextLine();
                
                System.out.println("Enter semester:");
                int semester = scanner.nextInt();
                scanner.nextLine();
                
                System.out.println("Enter Pre-requisites:");
                String pre = scanner.nextLine();
                
                System.out.println("Enter course Timings:");
                String timing = scanner.nextLine();
                
                System.out.println("Enter Enrollment-limit:");
                int limit = scanner.nextInt();
                scanner.nextLine();
                
                courseManager.addCourse(courseCode, courseName, credits, semester, pre, timing,"",limit,"");
                System.out.println("Course added successfully.");
            } 
            else if (option == 3) {
                System.out.println("Enter course code to delete:");
                String courseCode = scanner.nextLine();
                courseManager.deleteCourse(courseCode);
            } 
            else if (option == 4) {
                setDropDate(scanner);
            }
            else {
                System.out.println("Invalid option.");
            }
        }
    }

    public void setDropDate(Scanner scanner) {
        System.out.println("\nEnter drop deadline in (YYYY-MM-DD) : ");
        String deadline = scanner.nextLine();
        LocalDate dropDate = LocalDate.parse(deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Student.setDropDate(dropDate);
        System.out.println("\nDrop deadline successfully set to " + dropDate);
    }

    private void Assign_Professors(Scanner scanner) {
        System.out.println("Available Professors:");
        for (int i = 0; i < professors.size(); i++) {
            System.out.println((i + 1) + ") " + professors.get(i).getEmail());
        }
    
        System.out.println("\nEnter professor number to assign:");
        int professorIndex = scanner.nextInt() - 1;
        scanner.nextLine();
    
        if (professorIndex < 0 || professorIndex >= professors.size()) {
            System.out.println("Invalid professor selection.");
            return;
        }
    
        Professor selectedProfessor = professors.get(professorIndex);
        if (!selectedProfessor.isAvailable()) {
            System.out.println(selectedProfessor.getEmail() + " is already assigned to the course: " + selectedProfessor.getAssignedCourse() + ". \nTry assigning another professor.");
            return;
        }
    
        System.out.println("\nAvailable Courses:");
        courseManager.viewCourses();
        System.out.println("\nEnter course code to assign to " + selectedProfessor.getEmail() + ":");
        String courseCode = scanner.nextLine();
    
        CourseManager.Course course = courseManager.getCourseByCode(courseCode);
        if (course != null) {
            selectedProfessor.assignToCourse(courseCode);
            course.setProfessor(selectedProfessor);
            System.out.println(selectedProfessor.getEmail() + " has been assigned to " + course.getCourseName() + ".");
        } 
        else {
            System.out.println("Course not found.");
        }
    }


    private void Manage_Student_Records(Scanner scanner) {
        while (true) {
            System.out.println("\nManage Student Records:");
            System.out.println("1) View Student Records \n2) Update Student Records \n3) Back to Main Menu");
            int option = scanner.nextInt();
            scanner.nextLine();
            if (option == 3) {
                break;
            } 
            else if (option == 1) {
                viewStudentRecords(scanner);
            } 
            else if (option == 2) {
                updateStudentRecords(scanner);
            } 
            else {
                System.out.println("Invalid option.");
            }
        }
    }
    
    private void viewStudentRecords(Scanner scanner) {
        System.out.println("\nEnter student email to view records:");
        String email = scanner.nextLine();
        
        Student student = students.get(email);
        if (student == null) {
            System.out.println("\nNo student found with email: " + email);
            return;
        }
        
        System.out.println("\nStudent Records for " + email + ":");
        System.out.println("Current Semester: " + student.getCurrentSemester());
        System.out.println("Registered Credits: " + student.getRegisteredCredits());
        System.out.println("Completed Courses: " + student.getCompletedCourses());
        System.out.println("Registered Courses: " + student.getRegisteredCourses());
    }
    
    private void updateStudentRecords(Scanner scanner) {
        System.out.println("Enter student email to update records:");
        String email = scanner.nextLine();
        
        Student student = students.get(email);
        if (student == null) {
            System.out.println("No student found with email: " + email);
            return;
        }
        else {
            while (true) {
                System.out.println("\nUpdate Student Records:");
                System.out.println("1) Update Personal Information  \n2) Assign Grades to Courses");
                System.out.println("3) Complete Semester for Students \n4) Back to Manage Student Records Menu");
                int option = scanner.nextInt();
                scanner.nextLine();
                
                if (option == 4) {
                    break;
                }
                else if (option == 1) {
                    updatePersonalDetails(scanner);
                }
                else if (option == 2) {
                    System.out.println("Enter course code:");
                    String code = scanner.nextLine();
                    System.out.println("Enter grade (A, B, C, D, F):");
                    String grade = scanner.nextLine();

                    student.setGrade(code, grade);
                }
                else if (option == 3) {
                    student.completeSemester();
                    System.out.println("\nSemester completed successfully for "+ email);
                }
                else {
                    System.out.println("Invalid option.");
                }
            }
        }
    }

    private void updatePersonalDetails(Scanner scanner) {
        System.out.println("Enter student's email:");
        String email = scanner.nextLine();
        Student student = students.get(email);
        if (student == null) {
            System.out.println("\nStudent not found.");
            return;
        }

        System.out.println("\nUpdate details:  \n1) Email  \n2) Password");
        int option = scanner.nextInt();
        scanner.nextLine();

        if (option == 1) {
            System.out.println("Enter new email:");
            String newEmail = scanner.nextLine();
            students.remove(email);
            student.setEmail(newEmail);
            students.put(newEmail, student);
            System.out.println("\nEmail updated successfully.");
        } 
        else if (option == 2) {
            System.out.println("Enter new password:");
            String newPassword = scanner.nextLine();
            student.setPassword(newPassword);
            System.out.println("\nPassword updated successfully.");
        } 
        else {
            System.out.println("Invalid option.");
        }
    }


    private void Handle_Complaints(Scanner scanner) {
        while (true) {
            System.out.println("\nHandle Complaints:");
            System.out.println("1) View All Complaints \n2) Update Complaint Status \n3) Filter Complaints \n4) Back to Main Menu");
            int option = scanner.nextInt();
            scanner.nextLine();
            if (option == 4) {
                break;
            } 
            else if (option == 1) {
                viewComplaints();
            } 
            else if (option == 2) {
                updateComplaintStatus(scanner);
            } 
            else if (option == 3) {
                filterComplaints(scanner);
            } 
            else {
                System.out.println("Invalid option.");
            }
        }
    }

    private void viewComplaints() {
        if (complaints.isEmpty()) {
            System.out.println("\nNo complaints available.\n");
        } 
        else {
            for (Complaint c : complaints) {
                System.out.println(c);
                System.out.println();
            }
        }
    }

    private void updateComplaintStatus(Scanner scanner) {
        System.out.println("Enter complaint ID to update:");
        String id = scanner.nextLine();

        Complaint complaint = complaints.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);

        if (complaint == null) {
            System.out.println("Complaint ID not found.");
            return;
        }

        System.out.println("Current status: " + complaint.getStatus());
        System.out.println("Enter new status (Pending/Resolved):");
        String status = scanner.nextLine();
        if (!status.equals("Pending") && !status.equals("Resolved")) {
            System.out.println("Invalid status.");
            return;
        }
        complaint.setStatus(status);

        if (status.equals("Resolved")) {
            System.out.println("Enter resolution details:");
            String details = scanner.nextLine();
            complaint.setResolutionDetails(details);
        }

        System.out.println("Complaint status updated successfully.");
    }

    private void filterComplaints(Scanner scanner) {
        System.out.println("Filter complaints by:\n1) Status\n2) Date");
        int filterOption = scanner.nextInt();
        scanner.nextLine();

        if (filterOption == 1) {
            System.out.println("Enter status to filter (Pending/Resolved):");
            String status = scanner.nextLine();
            List<Complaint> filteredComplaints = complaints.stream().filter(c -> c.getStatus().equals(status)).collect(Collectors.toList());
            if (filteredComplaints.isEmpty()) {
                System.out.println("No complaints found with status: " + status);
            } else {
                for (Complaint c : filteredComplaints) {
                    System.out.println(c);
                    System.out.println();
                }
                System.out.println();
            }
        } 
        else if (filterOption == 2) {
            System.out.println("Enter date to filter (YYYY-MM-DD):");
            LocalDate date = LocalDate.parse(scanner.nextLine());
            List<Complaint> filteredComplaints = complaints.stream().filter(c -> c.getDate().isEqual(date)).collect(Collectors.toList());
            if (filteredComplaints.isEmpty()) {
                System.out.println("No complaints found on date: " + date);
            } 
            else {
                for (Complaint c : filteredComplaints) {
                    System.out.println(c);
                    System.out.println();
                }
            }
        } 
        else {
            System.out.println("Invalid filter option.");
        }
    }
}
