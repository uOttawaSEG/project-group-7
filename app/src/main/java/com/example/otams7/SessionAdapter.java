package com.example.otams7;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {

    private List<Map<String, Object>> sessions = new ArrayList<>();
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public SessionAdapter(Context context) {
        this.context = context;
    }

    public void setSessions(List<Map<String, Object>> newSessions) {
        this.sessions = newSessions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        Map<String, Object> session = sessions.get(position);

        holder.txtStudentName.setText((String) session.get("studentName"));
        holder.txtDate.setText((String) session.get("date"));

        String start = (String) session.get("startTime");
        String end = (String) session.get("endTime");
        holder.txtTime.setText(start + " - " + end);

        String status = (String) session.get("status");
        holder.txtStatus.setText(status);

        holder.itemView.setOnClickListener(v -> showSessionDialog(session));
    }

    private void showSessionDialog(Map<String, Object> session) {
        String studentName = (String) session.get("studentName");
        String status = (String) session.get("status");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Session with " + studentName);
        builder.setMessage("Date: " + session.get("date") + "\nTime: " + session.get("startTime") + " - " + session.get("endTime") +
                "\nStatus: " + status);

        if (status.equals("PENDING")) {
            builder.setPositiveButton("Approve", (dialog, which) -> updateStatus(session, "APPROVED"));
            builder.setNegativeButton("Reject", (dialog, which) -> updateStatus(session, "REJECTED"));
        } else if (status.equals("APPROVED")) {
            builder.setPositiveButton("Cancel", (dialog, which) -> updateStatus(session, "CANCELLED"));
        }

        builder.setNeutralButton("Close", null);
        builder.show();
    }

    private void updateStatus(Map<String, Object> session, String newStatus) {
        String docId = (String) session.get("id"); // Must be set in TutorActivity

        if (docId == null) {
            Toast.makeText(context, "Session ID missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("sessionRequests").document(docId)
                .update("status", newStatus)
                .addOnSuccessListener(a -> Toast.makeText(context, "Session " + newStatus, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView txtStudentName, txtDate, txtTime, txtStatus;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStudentName = itemView.findViewById(R.id.txtStudentName);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}
