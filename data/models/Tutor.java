package data.models;
public class Tutor extends User {
    private final String firstName;
    private final String lastName;

    public Tutor(String email, String password, String firstName, String lastName) {
        super(email, password, Role.Tutor);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
}
