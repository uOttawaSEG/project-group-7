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

import java.util.ArrayList;
import java.util.List;

public class AdminInboxActivity extends AppCompatActivity {
    private Button btnPending, btnRejected;
    private RecyclerView rv;
    private UserAdapter adapter;
    private UserRepository repo;
    private ListenerRegistration listener;
    private String currentMode = "PENDING"; // "PENDING" or "REJECTED"
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

        // Tab switch buttons
        btnPending.setOnClickListener(v -> switchTo("PENDING"));
        btnRejected.setOnClickListener(v -> switchTo("REJECTED"));

        adapter.setClick((userId, user) -> showDetailDialog(userId, user));

        attachListenerFor("PENDING"); // start on pending list
    }

    private void switchTo(String mode) {
        if (mode.equals(currentMode)) return;
        currentMode = mode;


        btnPending.setEnabled(!"PENDING".equals(mode));
        btnRejected.setEnabled(!"REJECTED".equals(mode));

        if (listener != null) {
            listener.remove();
            listener = null;
        }

        attachListenerFor(mode);
    }

    private void attachListenerFor(String status) {

        if (listener != null) {
            listener.remove();
            listener = null;
        }


        listener = FirebaseFirestore.getInstance()
                .collection("users").whereEqualTo("status",status)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e(TAG, "collection listener error", e);
                        Toast.makeText(AdminInboxActivity.this, "Error loading users (see Logcat)", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (snapshots == null) {
                        Log.d(TAG, "collection snapshots null");
                        Toast.makeText(AdminInboxActivity.this, "No users found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Log.d(TAG, "collection snapshot size = " + snapshots.size());
                    List<UserAdapter.Item> list = new ArrayList<>();

                    for (DocumentSnapshot d : snapshots.getDocuments()) {
                        Log.d(TAG, "ALL doc: " + d.getId() + " => " + d.getData());
                        AnyUser u = d.toObject(AnyUser.class);
                        if (u == null) {
                            Log.d(TAG, "toObject returned null for doc " + d.getId());
                            continue;
                        }

                        String uRole = u.getRole() == null ? "" : u.getRole();
                        String uStatus = u.getStatus() == null ? "" : u.getStatus();

                        Log.d(TAG, "Mapped user role=" + uRole + " status=" + uStatus);


                        if ("Administrator".equalsIgnoreCase(uRole)) continue;


                        if (uStatus.equalsIgnoreCase(status)) {
                            list.add(new UserAdapter.Item(d.getId(), u));
                        }
                    }

                    adapter.setItems(list);
                    Toast.makeText(AdminInboxActivity.this, "Displayed: " + list.size() + " items", Toast.LENGTH_SHORT).show();
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


        if ("PENDING".equals(currentMode)) {
            b.setPositiveButton("Approve", (d, w) -> {
                repo.updateStatus(uid, "APPROVED");
                Toast.makeText(this, "Request approved.", Toast.LENGTH_SHORT).show();



            });
            b.setNegativeButton("Reject", (d, w) -> {
                repo.updateStatus(uid, "REJECTED");
                Toast.makeText(this, "Request rejected.", Toast.LENGTH_SHORT).show();
                switchTo("REJECTED");
                attachListenerFor("REJECTED");
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
