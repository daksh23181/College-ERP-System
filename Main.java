import java.util.*;
import java.time.*;

public class Main {

    public static Map<String, Student> students = new HashMap<>();
    private static Map<String, Professor> professors = new HashMap<>();
    private static List<Complaint> complaints = new ArrayList<>();
    private static Administrator admin;
    private static CourseManager courseManager = new CourseManager();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SampleData();

        while (true) {
            System.out.println("\nWelcome to the IIITD Course Registration System \n");
            System.out.println("1) Enter the Application \n2) Exit the Application ");
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 2) {
                System.out.println("\nExiting app !!\n");
                break;
            } 
            else if (option == 1) {
                while (true) {
                    System.out.println("\nLogin as: \n1) Student \n2) Professor \n3) Administrator \n4) TA \n5) Exit");
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    if (choice == 1) {
                        Handle_Student(scanner);
                    } 1
                    else if (choice == 2) {
                        Handle_Professor(scanner);
                    } 
                    else if (choice == 3) {
                        // Admin login
                        System.out.println("\nLogin as ADMIN \n");
                        System.out.println("Enter email:");
                        String loginEmail = scanner.nextLine();
                        System.out.println("Enter password:");
                        String loginPassword = scanner.nextLine();

                        try {
                            admin.login(loginEmail, loginPassword);
                            admin.userMode(scanner);
                        } 
                        catch (InvalidLoginException e) {
                            System.out.println(e.getMessage());
                        }
                        catch (ClassCastException k) {
                            System.out.println("Login credentials is of Student type.");
                        }
                    }
                    else if (choice == 4) {
                        Handle_TA(scanner);
                    }
                    else if (choice == 5) {
                        break;
                    } 
                    else {
                        System.out.println("Invalid choice.");
                    }
                }
            } 
            else {
                System.out.println("Invalid input, please try again.");
            }
        }
        scanner.close();
    }

    private static void Handle_Student(Scanner scanner) {
        System.out.println("\n1) Login \n2) Signup");
        int option = scanner.nextInt();
        scanner.nextLine();

        if (option == 1) {
            // Student login
            System.out.println("Enter email:");
            String email = scanner.nextLine();
            System.out.println("Enter password:");
            String password = scanner.nextLine();

            Student student = students.get(email);
            try{
                if (student != null) {
                    student.login(email, password);
                    student.userMode(scanner);
                }
                else {
                    System.out.println("Account not found. SignUp !");
                }
            } 
            catch (InvalidLoginException e) {
                System.out.println(e.getMessage());
            }

        } 
        else if (option == 2) {
            // Student signup
            System.out.println("Enter email:");
            String email = scanner.nextLine();
            System.out.println("Enter Name:");
            String name = scanner.nextLine();
            System.out.println("Enter Contact Detail:");
            String phone = scanner.nextLine();
            System.out.println("Create password:");
            String password = scanner.nextLine();

            if (students.containsKey(email)) {
                System.out.println("Student already exists. Please login.");
            } 
            else {
                Student student = new Student(email, password, name, phone);
                student.signUp();
                student.setCourseManager(courseManager);
                students.put(email, student);
                System.out.println("Student registered successfully.");
            }
        } 
        else {
            System.out.println("Invalid option.");
        }
    }

    private static void Handle_Professor(Scanner scanner) {
        System.out.println("1) Login \n2) Signup");
        int option = scanner.nextInt();
        scanner.nextLine();

        if (option == 1) {
            // Professor login
            System.out.println("Enter email:");
            String email = scanner.nextLine();
            System.out.println("Enter password:");
            String password = scanner.nextLine();

            Professor professor = professors.get(email);
            try{
                if (professor != null) {
                    professor.login(email, password);
                    professor.userMode(scanner);
                }
                else {
                    System.out.println("Account not found. SignUp !");
                }
            } 
            catch (InvalidLoginException e) {
                System.out.println(e.getMessage());
            }
        } 
        else if (option == 2) {
            // Professor signup
            System.out.println("Enter email:");
            String email = scanner.nextLine();
            System.out.println("Create password:");
            String password = scanner.nextLine();

            if (professors.containsKey(email)) {
                System.out.println("Professor already exists. Please login.");
            } 
            else {
                Professor professor = new Professor(email, password);
                professor.setCourseManager(courseManager);
                professor.signUp();
                admin.add_Professor(professor);
                professors.put(email, professor);
                System.out.println("Professor registered successfully.");
            }
        } 
        else {
            System.out.println("Invalid option.");
        }
    }

    private static void Handle_TA(Scanner scanner) {
        System.out.println("1) Login \n2) Signup");
        int option = scanner.nextInt();
        scanner.nextLine();

        if (option == 1) {
            // TA login
            System.out.println("Enter email:");
            String email = scanner.nextLine();
            System.out.println("Enter password:");
            String password = scanner.nextLine();

            TeachingAssistant ta = (TeachingAssistant) students.get(email);
            try {
                if (ta != null) {
                    ta.login(email, password);
                    ta.userMode(scanner);
                }
                else {
                    System.out.println("Account not found. SignUp!");
                }
            } catch (InvalidLoginException e) {
                System.out.println(e.getMessage());
            }
        }
        else if (option == 2) {
            // TA signup
            System.out.println("Enter email:");
            String email = scanner.nextLine();
            System.out.println("Enter Name:");
            String name = scanner.nextLine();
            System.out.println("Enter Contact Detail:");
            String phone = scanner.nextLine();
            System.out.println("Create password:");
            String password = scanner.nextLine();

            if (students.containsKey(email)) {
                System.out.println("TA already exists. Please login.");
            } else {
                TeachingAssistant TA = new TeachingAssistant(email, password, name, phone);
                TA.setCourseManager(courseManager);
                students.put(email, TA);
                System.out.println("TA registered successfully.");
            }
        }
        else {
            System.out.println("Invalid option.");
        }
    }

    private static void SampleData() {
        // Some sample students for login
        Student daksh = new Student("daksh23181", "123", "Daksh Singh", "9985641254");
        daksh.setCourseManager(courseManager);
        students.put("daksh23181", daksh);
        
        Student ak = new Student("akshay23066", "123", "Akshay Chauhan", "8845612795");
        ak.setCourseManager(courseManager);
        students.put("akshay23066", ak);

        Student abhi = new Student("abhishek23029", "123", "Abhishek Rao", "7784365124");
        abhi.setCourseManager(courseManager);
        students.put("abhishek23029", abhi);

        Student aryan = new Student("aryan23044", "123", "Aryan Dahiya", "9997754612");
        aryan.setCourseManager(courseManager);
        students.put("aryan23044", aryan);

        Student kax = new Student("kavya23280", "123", "Kavya", "9856432178");
        kax.setCourseManager(courseManager);
        students.put("kavya23280", kax);

        Student.setComplaints(complaints);

        // Some Predefined courses
        courseManager.addCourse("CSE101", "Introduction to Programming", 4, 1, "None", "1-2 PM","",250,"");
        courseManager.addCourse("MTH101", "Linear Alzebra", 4, 1, "None", "8.30-9.30 AM","",250,"");
        courseManager.addCourse("ECE101", "Digital Circuits", 4, 1, "None", "9.30-11 AM","",300,"");
        courseManager.addCourse("COM101", "Communication Skills", 4, 1, "None", "3-6 PM","",350,"");
        
        courseManager.addCourse("CSE112", "Computer Organization", 4, 2, "None", "3-4 PM","",250,"");
        courseManager.addCourse("CSE102", "Data Structure $ Algorithm", 4, 2, "CSE101", "8.30-9.30 AM","",300,"");
        courseManager.addCourse("MTH102", "Discrete Maths", 4, 2, "MTH101", "1-2 PM","",80,"");
        courseManager.addCourse("ECE250", "Signal and Systems", 4, 3, "ECE101", "11-12.30 PM","",200,"");
        
        // Assigning professor1 to course
        Professor ojaswa = new Professor("ojaswa@iiitd", "12345");
        ojaswa.setCourseManager(courseManager);
        professors.put("ojaswa@iiitd", ojaswa);
        ojaswa.assignToCourse("CSE101");
        CourseManager.Course c1 = courseManager.getCourseByCode("CSE101");
        if (c1 != null) {
            c1.setProfessor(ojaswa); 
        }

        // Assigning professor2 to course
        Professor sujay = new Professor("sujay@iiitd", "12345");
        sujay.setCourseManager(courseManager);
        professors.put("sujay@iiitd", sujay);
        sujay.assignToCourse("ECE101");
        CourseManager.Course c2 = courseManager.getCourseByCode("ECE101");
        if (c2 != null) {
            c2.setProfessor(sujay); 
        }

        // Assigning professor3 to course
        Professor subhajit = new Professor("subhajit@iiitd", "12345");
        subhajit.setCourseManager(courseManager);
        professors.put("subhajit@iiitd", subhajit);
        subhajit.assignToCourse("MTH101");
        CourseManager.Course c3 = courseManager.getCourseByCode("MTH101");
        if (c3 != null) {
            c3.setProfessor(subhajit);
        }

        // Preadded Additional professors
        Professor sam = new Professor("sam@iiitd", "12345");
        sam.setCourseManager(courseManager);
        professors.put("sam@iiitd", sam);

        Professor bnj = new Professor("bnjain@iiitd", "12345");
        bnj.setCourseManager(courseManager);
        professors.put("bnjain@iiitd", bnj);


        // Some Sample TA
        TeachingAssistant Ma = new TeachingAssistant("mayank@ta", "123", "Mayank Baisla", "9876543210");
        Ma.setCourseManager(courseManager);
        students.put("mayank@ta", Ma);

        TeachingAssistant Ha = new TeachingAssistant("harshita@ta", "123", "Harshita Saini", "9876545620");
        Ha.setCourseManager(courseManager);
        students.put("harshita@ta", Ha);

        TeachingAssistant aman = new TeachingAssistant("aman@ta", "123", "Aman Sher", "8866545620");
        aman.setCourseManager(courseManager);
        students.put("aman@ta", aman);


        // Sample complaints data
        complaints.add(new Complaint("C1", "Course registration issue", LocalDate.now().minusDays(1)));
        complaints.add(new Complaint("C2", "Course clashing with other course", LocalDate.now().minusDays(2)));
        complaints.add(new Complaint("C3", "AC not working in LHC-102", LocalDate.now().minusDays(1)));
    
        // Admin data
        admin = new Administrator("admin@iiitd.ac.in", "admin123", new ArrayList<>(professors.values()));
        admin.setCourseManager(courseManager);
        Administrator.setComplaints(complaints);
        Administrator.setStudentRecords(students);
    }
}

