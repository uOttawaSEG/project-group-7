package com.example.otams7;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otams7.classes.AnyUser;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.VH> {

    public interface Click {
        void onItem(String userid, AnyUser user);
    }

    private final List<Item> items = new ArrayList<>();
    private Click click;

    public static class Item {
        public String userid;
        public AnyUser user;
        public Item(String userid, AnyUser user) {
            this.userid = userid;
            this.user = user;
        }
    }

    public void setClick(Click c) {
        this.click = c;
    }

    // replace list and refresh
    public void setItems(List<Item> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // its not the adminbinox its item user
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new VH(view);
    }

        @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Item it = items.get(position);
        AnyUser u = it.user;


        String name = (u.getFirstName() == null ? "" : u.getFirstName()) + " " + (u.getLastName() == null ? "" : u.getLastName());
        holder.txtName.setText(name.trim());
        holder.txtRole.setText(u.getRole() == null ? "" : u.getRole());
        holder.txtEmail.setText(u.getEmail() == null ? "" : u.getEmail());



        holder.itemView.setOnClickListener(v -> {
            if (click != null) click.onItem(it.userid, it.user);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView txtName, txtRole, txtEmail;
        VH(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtUserName);
            txtRole = itemView.findViewById(R.id.txtUserRole);
            txtEmail=itemView.findViewById(R.id.txtUserEmail);

        }
    }
}
