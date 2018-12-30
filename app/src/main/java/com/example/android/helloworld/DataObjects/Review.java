package com.example.android.helloworld.DataObjects;

import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class Review implements Serializable {

    static public Integer MAX_ALLOWED_REPORTS = 5;

    private String OwnerId;
    private Float Rating;
    private Integer ReportsCounter;
    private String VerbalComment;

    public Review() {
        // Default constructor required for calls to DataSnapshot.getValue
    }

    public Review(String ownerId, Float rating, String verbalComment)
    {
        this.OwnerId = ownerId;
        this.Rating = rating;
        this.VerbalComment = verbalComment;
        this.ReportsCounter = 0;
    }

    public Review(String ownerId, Float rating)
    {
        this.OwnerId = ownerId;
        this.Rating = rating;
        this.VerbalComment = "";
        this.ReportsCounter = 0;
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public Integer getReportsCounter() {
        return ReportsCounter;
    }

    public void setReportsCounter(Integer reportsCounter) {
        ReportsCounter = reportsCounter;
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

    public boolean Valid() { return ReportsCounter < MAX_ALLOWED_REPORTS; }
}
