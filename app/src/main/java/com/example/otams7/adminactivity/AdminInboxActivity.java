package com.example.otams7.adminactivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otams7.R;
import com.example.otams7.UserAdapter;
import com.example.otams7.UserRepository;
import com.example.otams7.classes.AnyUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminInboxActivity extends AppCompatActivity {
    private Button btnPending, btnRejected;
    private RecyclerView rv;
    private UserAdapter adapter;
    private UserRepository repo;
    private ListenerRegistration pendinglistener;
    private ListenerRegistration rejectedListener;
    private String currentMode = "PENDING";
    private static final String TAG = "AdminInbox";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admininbox);

        btnPending = findViewById(R.id.btnPending);
        btnRejected = findViewById(R.id.btnRejected);
        rv = findViewById(R.id.recyclerUsers);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter();
        repo = new UserRepository();
        rv.setAdapter(adapter);


        btnPending.setOnClickListener(v -> switchTo("PENDING"));
        btnRejected.setOnClickListener(v -> switchTo("REJECTED"));

        adapter.setClick((userId, user) -> showDetailDialog(userId, user));

        attachPendingRej();
        switchTo("PENDING");
    }

    private void attachPendingRej() {

        FirebaseFirestore db= FirebaseFirestore.getInstance();
        pendinglistener= db.collection("users")
                .whereEqualTo("status","PENDING").addSnapshotListener((snapshots, e) ->{
                    if (e != null) {
                        Log.e(TAG, "PENDING listener error", e);
                        Toast.makeText(AdminInboxActivity.this, "Error loading pending users", Toast.LENGTH_SHORT).show();
                        return;
                }
                    if ("PENDING".equals(currentMode)) {
                        updateAdapter(snapshots);
                    }
                });
        rejectedListener = db.collection("users")
                .whereEqualTo("status", "REJECTED")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e(TAG, "REJECTED listener error", e);
                        Toast.makeText(AdminInboxActivity.this, "Error loading rejected users", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if ("REJECTED".equals(currentMode)) {
                        updateAdapter(snapshots);
                    }
                });
    }
    private void updateAdapter(QuerySnapshot snapshots) {
        List<UserAdapter.Item> list = new ArrayList<>();
        if (snapshots != null) {
            for (DocumentSnapshot d : snapshots.getDocuments()) {
                AnyUser u = d.toObject(AnyUser.class);
                if (u == null || "Administrator".equalsIgnoreCase(u.getRole())) continue;
                list.add(new UserAdapter.Item(d.getId(), u));
            }
        }
        adapter.setItems(list);
        adapter.notifyDataSetChanged();
    }

    private void switchTo(String mode) {
        if (mode.equals(currentMode)) return;
        currentMode = mode;


        btnPending.setEnabled(!"PENDING".equals(mode));
        btnRejected.setEnabled(!"REJECTED".equals(mode));
        attachPendingRej();


    }


    private void showDetailDialog(String uid, AnyUser user) {
        String message = "Name: " + user.getFirstName() + " " + user.getLastName() + "\n"
                + "Email: " + user.getEmail() + "\n"
                + "Phone: " + user.getPhoneNumber() + "\n"
                + "Role: " + user.getRole();

        //USE IN BUILD context dialog when

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("User Details");
        b.setMessage(message);


        if ("PENDING".equals(currentMode)) {
            b.setPositiveButton("Approve", (d, w) -> {
                repo.updateStatus(uid, "APPROVED");
                Toast.makeText(this, "Request approved.", Toast.LENGTH_SHORT).show();








            });
            b.setNegativeButton("Reject", (d, w) -> {
                repo.updateStatus(uid, "REJECTED");
                Toast.makeText(this, "Request rejected.", Toast.LENGTH_SHORT).show();
                switchTo("REJECTED");





            });
        } else if ("REJECTED".equals(currentMode)) {
            b.setPositiveButton("Approve", (d, w) -> {
                repo.updateStatus(uid, "APPROVED");
                Toast.makeText(this, "Request approved.", Toast.LENGTH_SHORT).show();



            });
        }

        b.setNeutralButton("Close", null);
        b.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pendinglistener != null) pendinglistener.remove();
        if (rejectedListener != null) rejectedListener.remove();
    }
}

