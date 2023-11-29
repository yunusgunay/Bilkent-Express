package com.example.registerandmaps.Database;

import com.example.registerandmaps.Models.User;
import com.example.registerandmaps.Models.UserCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class UserDatabase {
    private FirebaseFirestore db;

    public UserDatabase() {
        db = FirebaseFirestore.getInstance();
    }

    // Method to add a user to Firestore
    public void addUser(User user, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection("users").document(user.getUid())
                .set(user)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    // Method to remove a user from Firestore
    public void removeUser(String uid, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection("users").document(uid)
                .delete()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }
    // Method to get user from Firestore
    public void getUser(String uid, final UserCallback callback) {
        db.collection("users").document(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    User user = document.toObject(User.class);
                    callback.onCallback(user);
                } else {
                    callback.onError(new Exception("No User found with UID: " + uid));
                }
            } else {
                callback.onError(task.getException());
            }
        });
    }
    public void updateUser(User user, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection("users").document(user.getUid())
                .set(user)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public void updateUserFields(String uid, Map<String, Object> updates, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection("users").document(uid)
                .update(updates)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public void addPointsToUser(String uid, int pointsToAdd, OnCompleteListener onCompleteListener) {
        // Reference to the user's document
        DocumentReference userDocRef = db.collection("users").document(uid);

        // Run a transaction to ensure that the read and write operations are atomic
        db.runTransaction((transaction) -> {
                    // Read the current points of the user
                    DocumentSnapshot snapshot = transaction.get(userDocRef);
                    if (snapshot.exists() && snapshot.contains("points")) {
                        // Cast to Number in case your points are stored as Long in Firestore
                        Number currentPoints = snapshot.getLong("points");
                        int newPoints = currentPoints.intValue() + pointsToAdd;

                        // Update the points of the user
                        transaction.update(userDocRef, "points", newPoints);
                    }
                    return null;
                }).addOnCompleteListener(onCompleteListener);
    }

}
