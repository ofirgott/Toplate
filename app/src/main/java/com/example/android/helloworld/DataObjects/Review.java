package com.example.android.helloworld.DataObjects;

import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class Review implements Serializable {

    private String OwnerId;
    private Float Rating;
    private String VerbalComment;

    public Review() {
        // Default constructor required for calls to DataSnapshot.getValue
    }

    public Review(String ownerId, Float rating, String verbalComment)
    {
        this.OwnerId = ownerId;
        this.Rating = rating;
        this.VerbalComment = verbalComment;
    }

    public Review(String ownerId, Float rating)
    {
        this.OwnerId = ownerId;
        this.Rating = rating;
        this.VerbalComment = "";
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(String ownerId) {
        OwnerId = ownerId;
    }

    public Float getRating() {
        return Rating;
    }

    public void setRating(Float rating) {
        Rating = rating;
    }

    public String getVerbalComment() {
        return VerbalComment;
    }

    public void setVerbalComment(String verbalComment) {
        VerbalComment = verbalComment;
    }
}
