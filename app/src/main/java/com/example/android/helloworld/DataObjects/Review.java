package com.example.android.helloworld.DataObjects;

import java.io.Serializable;

public class Review implements Serializable {

    static Integer NextId = 0;
    private Integer Id;
    private String OwnerId;
    private Integer Rating;
    private String VerbalComment;

    public Review() {
        // Default constructor required for calls to DataSnapshot.getValue
    }

    public Review(String ownerId, Integer rating, String verbalComment)
    {
        this.Id = ++NextId;
        this.OwnerId = ownerId;
        this.Rating = rating;
        this.VerbalComment = verbalComment;
    }

    public Review(String ownerId, Integer rating)
    {
        this.Id = ++NextId;
        this.OwnerId = ownerId;
        this.Rating = rating;
        this.VerbalComment = "";
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(String ownerId) {
        OwnerId = ownerId;
    }

    public Integer getRating() {
        return Rating;
    }

    public void setRating(Integer rating) {
        Rating = rating;
    }

    public String getVerbalComment() {
        return VerbalComment;
    }

    public void setVerbalComment(String verbalComment) {
        VerbalComment = verbalComment;
    }
}
