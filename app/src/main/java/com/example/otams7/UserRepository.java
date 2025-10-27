package com.example.otams7;

import com.example.otams7.classes.AnyUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class UserRepository {

    private  FirebaseFirestore db= FirebaseFirestore.getInstance();

    private CollectionReference usersRef= db.collection("users");


    //method for accepting pending status

    //note add snapshot listeners in activity
    public Query listPending(){
        return usersRef.whereEqualTo("status","Pending");

    }
    public Query listRejected(){
        return usersRef.whereEqualTo("status","Rejected");

    }

    //get user by id
    public DocumentReference getbyID(String userid){
        return usersRef.document(userid);
    }
    //we need to approve/reject
    public  void approve(String userid){
        usersRef.document(userid).update("status","Approved");

    }
    public  void reject(String userid){
        usersRef.document(userid).update("status","Rejected");

    } //setter

    public void updateStatus(String userId, String newStatus) {
        db.collection("users")
                .document(userId)
                .update("status", newStatus);
    }
    public void createOrUpdateUser(String userid, AnyUser user){

        usersRef.document(userid).set(user);
    }


    public Query queryByStatus(String status) {
        return db.collection("users")
                .whereEqualTo("status", status);
    }
}
