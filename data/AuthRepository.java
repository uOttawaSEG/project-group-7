package  data;
import data.models.Student;
import data.models.Tutor;

public interface AuthRepository {
    void registerStudent(Student student);             // throws if email exists
    void registerTutor(Tutor tutor);                   // throws if email exists
    String login(String email, String password);       // returns "Admin"|"Student"|"Tutor"
    void logout();                                     // clear session
    boolean isLoggedIn();
    String currentRole();                              // null if not logged in
}
