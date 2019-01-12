package com.example.android.helloworld.DataObjects;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;

import java.io.Serializable;
import java.util.ArrayList;
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
        this.reviewsPerRest = new HashMap<>();
    }


    public User(String uid, String name, int score, List<Review> reviews, List<Plate> platesCreated, int markedAsSpammer, String imgUrl, Map<String, Integer> reviewsPerRest) {
        this.uid = uid;
        this.name = name;
        this.score = score;
        this.reviews = reviews;
        this.platesCreated = platesCreated;
        this.markedAsSpammer = markedAsSpammer;
        this.imgUrl = imgUrl;
        this.reviewsPerRest = reviewsPerRest;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void addReview(Review review){
        this.reviews.add(review);
    }

    public void setPlatesCreated(List<Plate> platesCreated) {
        this.platesCreated = platesCreated;
    }

    public void setMarkedAsSpammer(int markedAsSpammer) {
        this.markedAsSpammer = markedAsSpammer;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setReviewsPerRest(Map<String, Integer> reviewsPerRest) {
        this.reviewsPerRest = reviewsPerRest;
    }

    public int getReviewsCounterForRest(String restName){
        if(!reviewsPerRest.containsKey(restName)){
            return 0;
        }
        else{
            return reviewsPerRest.get(restName);
        }
    }

    public void incrementRestReviewsCounter(String restName){
        if(!reviewsPerRest.containsKey(restName)){
            reviewsPerRest.put(restName, 1);
        }
        else{
            reviewsPerRest.put(restName, reviewsPerRest.get(restName) + 1);
        }

        updateCurrentUserInDBOffline(this);
    }

    public void incrementCurrentUserScore(int value){
        score += value;
        updateCurrentUserInDBOffline(this);
    }

    public static void decrementUserScore(final String uid, final int value){
        DatabaseReference userRef = database.getReference().child("Users");
        userRef.runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(MutableData mutableData) {
                User user = mutableData.child(uid).getValue(User.class);
                if (user != null)
                {
                    user.score -= value;
                    mutableData.child(uid).setValue(user.toMap());
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    System.out.println(databaseError.getMessage());
                }
            }
        });
    }

    public static void incrementUserSpammerCounter(final String uid){
        DatabaseReference userRef = database.getReference().child("Users");
        userRef.runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(MutableData mutableData) {
                User user = mutableData.child(uid).getValue(User.class);
                if (user != null)
                {
                    System.out.println(user.getUid());
                    System.out.println(user.getName());

                    user.markedAsSpammer++;
                    mutableData.child(uid).setValue(user.toMap());
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    System.out.println(databaseError.getMessage());
                }
            }
        });
    }

    public Map<String, Integer> getReviewsPerRest() {
        return reviewsPerRest;
    }

    private Map<String, Integer> reviewsPerRest;



    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put("uid", uid);
        result.put("name", name);
        result.put("score", score);
        result.put("reviews", reviews);
        result.put("platesCreated", platesCreated);
        result.put("markedAsSpammer", markedAsSpammer);
        result.put("imgUrl", imgUrl);
        result.put("reviewsPerRest", reviewsPerRest);

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

    public void updateCurrentUserInDBOffline(final User user){
        DatabaseReference userRef = database.getReference().child("Users");
        userRef.runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.child(user.uid).setValue(user.toMap());
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    System.out.println(databaseError.getMessage());
                }
            }
        });

    }

    public static void deleteUserFromDB(final String uid){

        DatabaseReference userRef = database.getReference().child("Users");
        userRef.runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.child(uid).setValue(null);

                return Transaction.success(mutableData);

            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });



    }
}
