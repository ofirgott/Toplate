package com.example.android.helloworld.DataObjects;

import android.support.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.Comparator;

public class Review implements Serializable {

    static public Integer MAX_ALLOWED_REPORTS = 5;

    private String OwnerId;
    private Float Rating;
    private Integer ReportsCounter;
    private String VerbalComment;
    private String Name;
    private Integer ScoreOfOwner;

    public Review() {
        // Default constructor required for calls to DataSnapshot.getValue
    }

    public Review(String ownerId, String name, Float rating, String verbalComment, Integer scoreOfOwner)
    {
        this.OwnerId = ownerId;
        this.Rating = rating;
        this.VerbalComment = verbalComment;
        this.ReportsCounter = 0;
        this.Name = name;
        this.ScoreOfOwner = scoreOfOwner;
    }

    public Review(String ownerId, String name, Float rating, Integer scoreOfOwner)
    {
        this.OwnerId = ownerId;
        this.Rating = rating;
        this.VerbalComment = "";
        this.ReportsCounter = 0;
        this.Name = name;
        this.ScoreOfOwner = scoreOfOwner;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setScoreOfOwner(Integer scoreOfOwner) {
         ScoreOfOwner = scoreOfOwner;
    }

    public Integer getScoreOfOwner() {
        return this.ScoreOfOwner;
    }

    public Integer getReportsCounter() {
        return ReportsCounter;
    }

    public String getOwnerId() {
        return OwnerId;
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



