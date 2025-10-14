package auth;

import data.AuthRepository;
import data.models.Student;

public class RegisterStudentUseCase {
    private final AuthRepository repo;
    public RegisterStudentUseCase(AuthRepository repo) { this.repo = repo; }
    public void execute(Student s) { repo.registerStudent(s); }
}
