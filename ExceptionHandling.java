
class CourseFullException extends Exception {
    public CourseFullException(String message) {
        super("\nCourseFullException occured !! \n" + message);
    }
}

class InvalidLoginException extends Exception {
    public InvalidLoginException(String message) {
        super("\nInvalidLoginException occured !! \n" + message);
    }
}

class DropDeadlinePassedException extends Exception {
    public DropDeadlinePassedException(String message) {
        super("\nDropDeadlinePassedException occured !! \n" + message);
    }
}
