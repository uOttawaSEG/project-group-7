package com.example.otams7.classes;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otams7.R;
import com.example.otams7.SessionAdapter;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

public class TutorActivity extends AppCompatActivity {

    FirebaseAuth auth= FirebaseAuth.getInstance();
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    private SessionAdapter upcomingAdapter, pastAdapter, requestAdapter;
    private ListenerRegistration upcomingListener,pastListener,requestListener;
    private String tutorID;
    private boolean autoApprove= false;

    private RecyclerView rvUpcoming, rvPast, rvRequests;

//    The Tutor can create new availability slots (date, start time,
//                                                 end time).
//            10%
//    The Tutor cannot select a past date or overlapping slot. 5%
//    The Tutor can choose whether requests require manual
//    approval or not.
//10%
//    The Tutor can view a list of upcoming sessions. 5%
//    The Tutor can view a list of past sessions. 5%
//    The Tutor can view a list pending session requests from
//    Students.
//5%
//    The Tutor can see the information of a Student that made a
//    session request.
//            5%
//    The Tutor can approve, reject, or cancel sessions. 10%
//    The Tutor can delete availability slots they created. 10%
//    All fields are validated, with clear error messages. 10%
//
//
//
// NOTES TO
//



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tutor);

        rvUpcoming = findViewById(R.id.rvUpcomingSessions);
        rvPast = findViewById(R.id.rvPastSessions);
        rvRequests = findViewById(R.id.rvSessionRequests);

        rvUpcoming.setLayoutManager(new LinearLayoutManager(this));
        rvPast.setLayoutManager(new LinearLayoutManager(this));
        rvRequests.setLayoutManager(new LinearLayoutManager(this));

        upcomingAdapter = new SessionAdapter(this);
        pastAdapter = new SessionAdapter(this);
        requestAdapter = new SessionAdapter(this);



        rvUpcoming.setAdapter(upcomingAdapter);
        rvPast.setAdapter(pastAdapter);
        rvRequests.setAdapter(requestAdapter);

        TextView newTV = findViewById(R.id.txtWelcome);
        Button logOut = findViewById(R.id.btnLogout);

        Switch switchAuto= findViewById(R.id.switchAutoApprove);
        autoApprove = switchAuto.isChecked();
        switchAuto.setOnCheckedChangeListener((buttonView, isChecked) -> {
            autoApprove=isChecked;  //
            if(!isChecked){
                String msg= "Auto Approve switch is OFF";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

            }
            else{
                String msg1= "Auto Approve switch is on";
                Toast.makeText(this, msg1, Toast.LENGTH_SHORT).show();

            }
            if (auth.getCurrentUser() != null) {
                String tutorId = auth.getCurrentUser().getUid();
                db.collection("sessionRequests")
                        .whereEqualTo("tutorId", tutorId)
                        .whereIn("status", List.of("PENDING", "APPROVED")) //so it toggles
                        .get()
                        .addOnSuccessListener(query -> {
                            for (DocumentSnapshot doc : query.getDocuments()) {
                                doc.getReference().update("status", autoApprove ? "APPROVED" : "PENDING");
                            }
                            Toast.makeText(this, "Existing pending sessions updated!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Failed to update sessions: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

    });





                String uid=auth.getCurrentUser().getUid();
//        if(auth.getCurrentUser()!= null) {
//            uid=auth.getCurrentUser().getUid();
//        }
        if(uid!=null){
            db.collection("users").document(uid).get().addOnSuccessListener(
                    documentSnapshot -> {
                        if(documentSnapshot.exists()) {
                            String firstName = documentSnapshot.getString("firstName");
                            String lastName = documentSnapshot.getString("lastName");
                            String email = documentSnapshot.getString("email");
                            String role= documentSnapshot.getString("role");

                            newTV.setText("Welcome!" +firstName + " " + lastName+ " You are logged in as "+ role);

                            generateSessionRequestsForStudents(uid);

                            //call methods
                            listenForUpcomingSessions(uid);
                            listenForPastSessions(uid);
                            listenForPendingRequests(uid);


                        }

                    })
                    .addOnFailureListener(
                            e ->
                                    Toast.makeText(this, "Failed to fetch user info: " + e.getMessage(), Toast.LENGTH_SHORT).show());



        }
        if(uid==null){
            newTV.setText("Welcome! User not logged in.");
        }









        logOut.setOnClickListener(v -> {
            Intent newLogOut = new Intent(this, Landing_Screen.class);
            newLogOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(newLogOut);
        });
        Button btnManageAvailability= findViewById(R.id.btnManageAvailability);
        btnManageAvailability.setOnClickListener(v -> showAvailabilityDialog());



    }

    private void listenForUpcomingSessions(String tutorId) {
        // Listen for sessions where tutorId = current tutor and status = "PENDING" or "APPROVED"
        upcomingListener = db.collection("sessionRequests")
                .whereEqualTo("tutorId", tutorId)
                .whereIn("status", List.of("PENDING", "APPROVED")) //bcs pending(requested) and approved are accepted to be seen in upcoming
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Toast.makeText(this, "Error loading sessions: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (querySnapshot == null || querySnapshot.isEmpty()) {
                        upcomingAdapter.setSessions(new ArrayList<>()); // Clear list if no data
                        return;
                    }

                    List<Map<String, Object>> sessions = new ArrayList<>();

                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Map<String, Object> session = new HashMap<>(doc.getData());
                        session.put("id", doc.getId()); // add Firestore document ID
                        sessions.add(session);
                    }


                    // Update adapter data
                    upcomingAdapter.setSessions(sessions);
                });
    }

    private void listenForPastSessions(String tutorId) {
        pastListener = db.collection("sessionRequests")
                .whereEqualTo("tutorId", tutorId)
                .whereEqualTo("status", "CANCELLED") // STAY REJECTED
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Toast.makeText(this, "Error loading past sessions: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<Map<String, Object>> sessions = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Map<String, Object> session = new HashMap<>(doc.getData());
                        session.put("id", doc.getId());
                        sessions.add(session);
                    }

                    pastAdapter.setSessions(sessions);
                });
    }

    private void listenForPendingRequests(String tutorId) {
        requestListener = db.collection("sessionRequests")
                .whereEqualTo("tutorId", tutorId)
                .whereEqualTo("status", "PENDING")
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Toast.makeText(this, "Error loading requests: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<Map<String, Object>> sessions = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Map<String, Object> session = new HashMap<>(doc.getData());
                        session.put("id", doc.getId());
                        sessions.add(session);
                    }

                    requestAdapter.setSessions(sessions);
                });
    }




    //INBUILD IN CALENDAR
    private void showAvailabilityDialog() {
        Calendar calendar = Calendar.getInstance();



        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);


                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            this,
                            (timeView, hourOfDay, minute) -> {
                                Calendar startTime = Calendar.getInstance();
                                startTime.set(year, month, dayOfMonth, hourOfDay, minute);


                                Calendar endTime = (Calendar) startTime.clone();
                                endTime.add(Calendar.MINUTE, 30);

                                //onlt slots for 30 mins checked


                                if (startTime.before(Calendar.getInstance())) {
                                    Toast.makeText(this, "Cannot select a past date/time!", Toast.LENGTH_SHORT).show();
                                    return;
                                }

//                                saveAvailabilitySlot(selectedDate, startTime, endTime);
                                showAddOrDeleteSlotDialog(selectedDate, startTime);


                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    );
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }




    private void saveAvailabilitySlot(Calendar date, Calendar startTime, Calendar endTime) {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String tutorId = auth.getCurrentUser().getUid();

        Map<String, Object> slot = new HashMap<>();
        slot.put("tutorId", tutorId);

        //use formate function for databse input in hashmaps
        //

        String formattedDate = String.format("%04d-%02d-%02d",
                date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH));
        slot.put("date", formattedDate);

        String startFormatted = String.format("%02d:%02d",
                startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE));
        slot.put("startTime", startFormatted);

        String endFormatted = String.format("%02d:%02d",
                endTime.get(Calendar.HOUR_OF_DAY), endTime.get(Calendar.MINUTE));
        slot.put("endTime", endFormatted);

        // Fetch existing slots to check for overlap
        db.collection("availability")
                .whereEqualTo("tutorId", tutorId)
                .whereEqualTo("date", formattedDate)
                .get()
                .addOnSuccessListener(query -> {
                    boolean overlaps = false;

                    for (var doc : query.getDocuments()) {
                        String existingStart = doc.getString("startTime");
                        String existingEnd = doc.getString("endTime");
                        if (timeOverlap(existingStart, existingEnd, startFormatted, endFormatted)) {
                            overlaps = true;
                            break;
                        }
                    }

                    if (overlaps) {
                        Toast.makeText(this, "Slot overlaps with an existing one!", Toast.LENGTH_SHORT).show();
                    } else {
                        db.collection("availability").add(slot)
                                .addOnSuccessListener(a -> {
                                    Toast.makeText(this, " Availability slot added!", Toast.LENGTH_SHORT).show();

                                    //add switch after added i thinkk




                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, " Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                //error must add firebase rule for Firetsore availabitlity
                .addOnFailureListener(e ->
                        Toast.makeText(this, " Error checking existing slots: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void showAddOrDeleteSlotDialog(Calendar date, Calendar startTime) {
        String formattedDate = String.format("%04d-%02d-%02d",
                date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH));
        String startFormatted = String.format("%02d:%02d",
                startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE));
        //like admin pop dialog

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Manage Slot")
                .setMessage("Do you want to ADD or DELETE this slot?" +
                        "Date: " + formattedDate + "\nTime: " + startFormatted)
                .setPositiveButton("Add", (dialog, which) -> {
                    Calendar endTime = (Calendar) startTime.clone();


                    endTime.add(Calendar.MINUTE, 30);
                    saveAvailabilitySlot(date, startTime, endTime); //call method to save it
                })
                .setNegativeButton("Delete", (dialog, which) -> {
                    deleteAvailabilitySlot(formattedDate, startFormatted);//to delete
                })
                .setNeutralButton("Cancel", null)
                .show();
    }
    private void deleteAvailabilitySlot(String date, String startTime) {
        if (auth.getCurrentUser() == null) {

            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String tutorId = auth.getCurrentUser().getUid();

        db.collection("availability")
                .whereEqualTo("tutorId", tutorId)
                .whereEqualTo("date", date)
                .whereEqualTo("startTime", startTime)
                .get()

                .addOnSuccessListener(query -> {
                    if (query.isEmpty()) {
                        Toast.makeText(this, "No matching slot found to delete", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (DocumentSnapshot doc : query.getDocuments()) {
                        db.collection("availability").document(doc.getId())
                                .delete()
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(this, "Slot deleted successfully!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Failed to delete slot: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error fetching slots: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }



    private boolean timeOverlap(String existingStart, String existingEnd, String startFormatted, String endFormatted) {
        int existingstart= Integer.parseInt(existingStart.replace(":", ""));
        int existingend= Integer.parseInt(existingEnd.replace(":", ""));
        int startformatted= Integer.parseInt(startFormatted.replace(":", ""));
        int endformatted= Integer.parseInt(endFormatted.replace(":", ""));
        return (existingstart <endformatted);


    }
    //create session FROM AVAIABLY firestore

    private void generateSessionRequestsForStudents(String tutorId) {
        // FETCH from avaiablity collection
        db.collection("availability")
                .whereEqualTo("tutorId", tutorId)
                .get()
                .addOnSuccessListener(availabilityQuery -> {
                    if (availabilityQuery.isEmpty()) {
                        Toast.makeText(this, "No availability slots found for tutor!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // fetch from ALL STUDENTS
                    db.collection("users")
                            .whereEqualTo("role", "Student")
                            .get()
                            .addOnSuccessListener(studentQuery -> {
                                //loopp thogut students
                                for (DocumentSnapshot studentDoc : studentQuery.getDocuments()) {
                                    String studentId = studentDoc.getId();
                                    String firstName = studentDoc.getString("firstName");
                                    String lastName = studentDoc.getString("lastName");
                                    String studentName = firstName + " " + lastName;

                                    // For each student, loop through availability slots
                                    for (DocumentSnapshot slotDoc : availabilityQuery.getDocuments()) {
                                        String date = slotDoc.getString("date");
                                        String startTime = slotDoc.getString("startTime");
                                        String endTime = slotDoc.getString("endTime");

                                        // CREATE NEW COLLECITON
                                        db.collection("sessionRequests")
                                                .whereEqualTo("studentId", studentId)
                                                .whereEqualTo("tutorId", tutorId)
                                                .whereEqualTo("date", date)
                                                .whereEqualTo("startTime", startTime)
                                                .get()
                                                .addOnSuccessListener(existing -> {
                                                    if (existing.isEmpty()) {
                                                        Map<String, Object> newRequest = new HashMap<>();
                                                        newRequest.put("studentId", studentId);
                                                        newRequest.put("studentName", studentName);
                                                        newRequest.put("tutorId", tutorId);
                                                        newRequest.put("date", date);
                                                        newRequest.put("startTime", startTime);
                                                        newRequest.put("endTime", endTime);


                                                        newRequest.put("status", "PENDING");
                                                        newRequest.put("course", ""); // DO THIS FOR deliv 4

                                                        db.collection("sessionRequests").add(newRequest)
                                                                .addOnSuccessListener(ref ->
                                                                        Toast.makeText(this, "Session request created for " + studentName , Toast.LENGTH_SHORT).show())
                                                                .addOnFailureListener(e ->
                                                                        Toast.makeText(this, "Failed to create request: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                                    }
                                                    else{
                                                        //for autoapprove
                                                        for (DocumentSnapshot existingDoc : existing.getDocuments()) {
                                                            // Update the status based on autoApprove
                                                            existingDoc.getReference().update("status", autoApprove ? "APPROVED" : "PENDING")
                                                                    .addOnSuccessListener(aVoid ->
                                                                            Toast.makeText(this, "Session updated for " + studentName, Toast.LENGTH_SHORT).show())
                                                                    .addOnFailureListener(e ->
                                                                            Toast.makeText(this, "Failed to update session: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                                        }

                                                    }
                                                });
                                    }
                                }
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Error fetching students: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error fetching availability: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (upcomingListener != null) upcomingListener.remove();
        if (pastListener != null) pastListener.remove();
        if (requestListener != null) requestListener.remove();
    }



}