<bold> DEMO PRESENTATION<bold>


<bold>OTAMS Project

Team Members

<br>Rayyan Omar
<br>Arsh Singh
 <br>Gautam Khosla
<br>Adam Jemmali

 Weekly Google Meet + Google Docs Project Notes
<br>Google Meet: https://meet.google.com/bwg-cort-usr
<br> Google Docs: https://docs.google.com/document/d/1up98V6YdBWjPNzX8ZuNKuVOxdJLtv_BvlobuyC1qdcM/edit?tab=t.0



SEG2105 Project – Backend Authentication Module
📌 Overview

This project is the backend layer of our SEG2105 application. It handles user registration, authentication, and role management without any user interface. The UI team can directly connect to this backend to enable login, registration, and role-based access in the app.

The backend is built using pure Java and follows a simple, modular structure that makes it easy to maintain and upgrade (e.g., switching to Firebase in the future).

📁 Project Structure
SEG2105-PROJECT/
├─ data/
│  ├─ AuthRepository.java           # Interface defining authentication methods
│  ├─ InMemoryAuthRepository.java   # In-memory implementation of the repository
│  ├─ AdminSeeder.java              # Seeds a default admin user
│  └─ models/
│     ├─ Role.java                  # Enum for user roles
│     ├─ User.java                  # Base class for all users
│     ├─ Student.java               # Student model
│     └─ Tutor.java                 # Tutor model
│
├─ auth/
│  ├─ LoginUseCase.java             # Handles user login
│  ├─ LogoutUseCase.java            # Handles user logout
│  ├─ RegisterStudentUseCase.java   # Registers new students
│  └─ RegisterTutorUseCase.java     # Registers new tutors
│
├─ core/
│  └─ AuthService.java              # Facade connecting UI with backend logic
│
└─ Main.java                        # Test runner for backend without UI

🧠 Features

✅ Register new users (students and tutors)

✅ Login with email and password

✅ Role-based authentication (Admin, Student, Tutor)

✅ Logout and session handling

✅ Admin seeding (from README credentials)

✅ Easy to replace with a Firebase backend in the future

🧪 How to Run (Without UI)

You can run and test the backend directly from the terminal — no Android UI is required.

1. Navigate to the project folder:
cd SEG2105-PROJECT

2. Compile all .java files:
javac data/models/*.java data/*.java auth/*.java core/*.java Main.java

3. Run the backend demo:
java SEG2105_PROJECT.Main

✅ Expected Output:
Admin
Student
Tutor


This output shows that:

The admin account was successfully seeded and logged in.

A student and a tutor were registered and authenticated.

The login() method correctly returns the user role string.

🔄 How It Works

AuthRepository defines the core methods.

InMemoryAuthRepository provides the working implementation (can later be replaced by a Firebase version).

Use-cases handle each specific action (Login, Register, Logout).

AuthService is the bridge the UI will use — making it simple for the front-end to call backend logic.

Main.java is a simple console runner to simulate user interactions.

🧑‍💻 Future Work (Optional)

🔐 Integrate with Firebase or a real database.

🧪 Add unit tests for repository and use-cases.

🧱 Add persistence (save users to a file or JSON).

👤 Author

Backend Developer: Your Name
Course: SEG2105 – Software Engineering
Focus: Backend authentication & role management module

