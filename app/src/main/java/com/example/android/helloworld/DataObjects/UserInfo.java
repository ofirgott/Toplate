package com.example.android.helloworld.DataObjects;

import java.io.Serializable;
import java.util.List;

public class UserInfo implements Serializable {
    private String uid;
    private String name;
    private int score;
    private List<Review> reviews;
    private List<Plate> platesCreated;
    private int markedAsSpammer = 0;
    private String img;

    public UserInfo() {
        // Default constructor required for calls to Data  Snapshot.getValue(Post.class)
    }


    public UserInfo(String uid, String name, int score, List<Review> reviews, List<Plate> platesCreated, int markedAsSpammer, String img) {
        this.uid = uid;
        this.name = name;
        this.score = score;
        this.reviews = reviews;
        this.platesCreated = platesCreated;
        this.markedAsSpammer = markedAsSpammer;
        this.img = img;
    }
}
