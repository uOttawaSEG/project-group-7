import core.AuthService;

public class Main {
    public static void main(String[] args) {
        // Create backend service with seeded admin credentials
        AuthService auth = AuthService.createDefaultSeeded("admin@example.com", "changeme");

        // Register a few users
        auth.registerStudent("sally@student.ca", "sally123", "Sally", "Ng");
        auth.registerTutor("tom@tutor.ca", "tom123", "Tom", "Lee");

        // Login as Admin
        String adminRole = auth.login("admin@example.com", "changeme");
        System.out.println("Logged in as: " + adminRole); // Admin
        auth.logout();

        //  Login as Student
        String studentRole = auth.login("sally@student.ca", "sally123");
        System.out.println("Logged in as: " + studentRole); // Student
        auth.logout();

        //  Login as Tutor
        String tutorRole = auth.login("tom@tutor.ca", "tom123");
        System.out.println("Logged in as: " + tutorRole); // Tutor
    }
}
