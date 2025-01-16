import java.util.*;

public abstract class User {
    protected String email;
    protected String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public abstract void login(String email, String password) throws InvalidLoginException;
    public abstract void signUp();
    public abstract void userMode(Scanner scanner);
}

