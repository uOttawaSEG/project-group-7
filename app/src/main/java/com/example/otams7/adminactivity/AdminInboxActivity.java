package com.example.otams7.adminactivity;

import android.os.Bundle;
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
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class AdminInboxActivity extends AppCompatActivity {
    private Button btnPending, btnRejected;
    private RecyclerView rv;
    private UserAdapter adapter;
    private UserRepository repo;
    private ListenerRegistration listener;
    private String currentMode = "PENDING"; // "PENDING" or "REJECTED"

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

        // Tab switch buttons
        btnPending.setOnClickListener(v -> switchTo("PENDING"));
        btnRejected.setOnClickListener(v -> switchTo("REJECTED"));

        // Click on a user → show details dialog
        adapter.setClick((userId, user) -> showDetailDialog(userId, user));

        attachListenerFor("PENDING"); // start on pending list
    }

    private void switchTo(String mode) {
        if (mode.equals(currentMode)) return;
        currentMode = mode;

        btnPending.setEnabled(!"PENDING".equals(mode));
        btnRejected.setEnabled(!"REJECTED".equals(mode));

        attachListenerFor(mode);
    }

    private void attachListenerFor(String status) {
        // Stop any previous listener
        if (listener != null) {
            listener.remove();
            listener = null;
        }


        Query q = repo.queryByStatus(status);


        listener = q.addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                Toast.makeText(AdminInboxActivity.this, "Error loading users", Toast.LENGTH_SHORT).show();
                return;
            }
            if (snapshots == null) return;

            List<UserAdapter.Item> list = new ArrayList<>();
            for (DocumentSnapshot d : snapshots.getDocuments()) {
                AnyUser u = d.toObject(AnyUser.class);
                if (u == null) continue;
                if ("Administrator".equals(u.getRole())) continue; // exclude admin
                list.add(new UserAdapter.Item(d.getId(), u));
            }
            adapter.setItems(list);
        });
    }

    private void showDetailDialog(String uid, AnyUser user) {
        String message = "Name: " + user.getFirstName() + " " + user.getLastName() + "\n"
                + "Email: " + user.getEmail() + "\n"
                + "Phone: " + user.getPhoneNumber() + "\n"
                + "Role: " + user.getRole();

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("User Details");
        b.setMessage(message);

        // Buttons depend on which tab you're on
        if ("PENDING".equals(currentMode)) {
            b.setPositiveButton("Approve", (d, w) -> {
                repo.updateStatus(uid, "APPROVED");
                Toast.makeText(this, "Request approved.", Toast.LENGTH_SHORT).show();
            });
            b.setNegativeButton("Reject", (d, w) -> {
                repo.updateStatus(uid, "REJECTED");
                Toast.makeText(this, "Request rejected.", Toast.LENGTH_SHORT).show();
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
        if (listener != null) listener.remove();
    }
}
