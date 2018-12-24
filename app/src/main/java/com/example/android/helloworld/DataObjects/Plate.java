package com.example.android.helloworld.DataObjects;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

final public class Plate implements Serializable {

    static final FirebaseDatabase dataBase = FirebaseDatabase.getInstance();

    static Integer NextId = 0;

    private Integer Id;
    private String OwnerId;
    private String PlateName;
    private String RestName;
    private List<Review> Reviews;
    private Map<String, Integer> Tags;

    public Plate() {
        // Default constructor required for calls to DataSnapshot.getValue
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Plate(String Name, String RestName, List<String> tags, Review review) {
        this.Id = ++NextId;
        this.OwnerId = review.getOwnerId();
        this.PlateName = Name;
        this.RestName = RestName;
        this.Tags = new HashMap<>();
        this.Reviews = new ArrayList<>();

        for (String tag : tags) {
            this.Tags.put(tag, 1);
        }

        this.Reviews.add(review);
    }

    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put("Id", Id);
        result.put("OwnerId", OwnerId);
        result.put("PlateName", PlateName);
        result.put("RestName", RestName);
        result.put("Tags", Tags);
        result.put("Reviews", Reviews);
        return result;
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(String ownerId) {
        this.OwnerId = ownerId;
    }

    public String getPlateName() {
        return PlateName;
    }

    public void setPlateName(String name) {
        this.PlateName = name;
    }

    public String getRestName() {
        return RestName;
    }

    public void setRestName(String restName) {
        this.RestName = restName;
    }

    public Map<String, Integer> getTags() {
        return Tags;
    }

    public void setTags(Map<String, Integer> Tags) {
        this.Tags = Tags;
    }

    public List<Review> getReviews() {
        return Reviews;
    }

    public void setReviews(List<Review> reviews) {
        Reviews = reviews;
    }

    public void insertToTags() {
        DatabaseReference tagsRef = dataBase.getReference("Tags");
        final String thisPlateId = Integer.toString(this.Id);
        final Plate thisPlate = this;

        for (final String tag : this.Tags.keySet()) {
            tagsRef.child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataBase.getReference("Tags").child(tag).child(thisPlateId).setValue(thisPlate);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println(databaseError.getMessage());
                }
            });
        }
    }

    public void updateTagsAndReviews(List<String> tags, Review review) {

        for (String tag : tags) {
            Integer currentTagCount = 1;

            if (this.Tags.containsKey(tag)) {
                Integer prevTagCount = this.Tags.get(tag);
                currentTagCount = ++prevTagCount;
            }

            this.Tags.put(tag, currentTagCount);
        }

        this.Reviews.add(review);
    }

    /********STATIC FUNCTIONS*******/

    public static List<String> getAllRestPlates(final String restName) { //TODO
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Restaurants");
        final List<String> plateNames = new ArrayList<>();

        ref.orderByChild("RestName").equalTo(restName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getChildrenCount() > 1) {
                        System.out.println("more than one restaurant with the same name is illegal");
                        return;
                    }

                    DataSnapshot restDs = dataSnapshot.child(restName);
                    for (DataSnapshot ds : restDs.getChildren()) {
                        plateNames.add(ds.child("PlateName").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });

        return plateNames;
    }

    public static void addToDB(final String PlateName, final String RestName, final List<String> Tags, final Review review) {

        DatabaseReference restRef = dataBase.getReference().child("Restaurants").child(RestName);

        restRef.runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(MutableData mutableData) {
                Plate plate = mutableData.child(PlateName).getValue(Plate.class);
                if (plate == null)
                {
                    plate = new Plate(PlateName, RestName, Tags, review);
                }
                else
                {
                    plate.updateTagsAndReviews(Tags, review);
                }

                mutableData.child(PlateName).setValue(plate.toMap());
                plate.insertToTags();

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {

                System.out.println("transaction completed");

            }
        });


    }

}