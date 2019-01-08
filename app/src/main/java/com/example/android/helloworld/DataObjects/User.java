package com.example.android.helloworld.DataObjects;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {
    private String uid;
    private String name;
    private int score;
    private List<Review> reviews;
    private List<Plate> platesCreated;
    private int markedAsSpammer = 0;
    private String imgUrl;


    static private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    static private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public User() {
        // Default constructor required for calls to Data  Snapshot.getValue(Post.class)
    }


    public User(String uid, String name, int score, List<Review> reviews, List<Plate> platesCreated, int markedAsSpammer, String imgUrl) {
        this.uid = uid;
        this.name = name;
        this.score = score;
        this.reviews = reviews;
        this.platesCreated = platesCreated;
        this.markedAsSpammer = markedAsSpammer;
        this.imgUrl = imgUrl;
    }

    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put("uid", uid);
        result.put("name", name);
        result.put("score", score);
        result.put("reviews", reviews);
        result.put("platesCreated", platesCreated);
        result.put("markedAsSpammer", markedAsSpammer);
        result.put("imgUrl", imgUrl);

        return result;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<Plate> getPlatesCreated() {
        return platesCreated;
    }

    public int getMarkedAsSpammer() {
        return markedAsSpammer;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public static void addNewUserToDB(User user){
        addNewUserToDB(user.uid, user.name, user.score, user.reviews, user.platesCreated, user.markedAsSpammer, user.imgUrl);
    }

    public static void addNewUserToDB(String uid, String name, int score, List<Review> reviews, List<Plate> platesCreated, int markedAsSpammer, String imgUrl) {

        User user = new User(uid, name, score, reviews, platesCreated, markedAsSpammer, imgUrl);
        database.getReference().child("Users").child(uid).setValue(user , new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.d("AUTH", "User could not be saved.");

                } else {
                    Log.d("AUTH", "New account successfully created.");
                }
            }
        });
    }

    public static void deleteUserFromDB(String uid){
        database.getReference().child("Users").child(uid).setValue(null , new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.d("AUTH", "User could not be deleted from db.");

                } else {
                    Log.d("AUTH", "User deleted from db.");
                }
            }
        });
    }

    public static User getCurrentUserPersonalInfo(){
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        String uid = mFirebaseAuth.getCurrentUser().getUid();
        final User user = new User();
        final DatabaseReference userRef = database.getReference().child("Users").child(uid);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user.name = dataSnapshot.getValue(String.class);
                user.score = dataSnapshot.getValue(int.class);
                user.reviews = dataSnapshot.getValue(List.class);
                user.markedAsSpammer = dataSnapshot.getValue(int.class);
                user.platesCreated = dataSnapshot.getValue(List.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return user;

    }
}
