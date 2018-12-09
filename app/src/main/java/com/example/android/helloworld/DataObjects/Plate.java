package com.example.android.helloworld.DataObjects;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

final public class Plate implements Serializable {

    static final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private String OwnerId;
    private String PlateName;
    private String RestName;
    //public Map<String, Integer> mTags;
    //public Map<String, String> mReviews; /* map between owner_id to verbal review */

    public Plate() {
        // Default constructor required for calls to DataSnapshot.getValue
    }

    public Plate(String ownerId, String Name, String RestName) {
        this.OwnerId = ownerId;
        this.PlateName = Name;
        this.RestName = RestName;

    }

    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put("OwnerId", OwnerId);
        result.put("PlateName", PlateName);
        result.put("RestName", RestName);
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

    public static void addNewPlateToDB(String ownerId, String PlateName, String RestName) {
        Plate currPlate = new Plate(ownerId, PlateName, RestName);

        DatabaseReference ref = database.getReference();

        String key = ref.child("plates").push().getKey();

        Map<String, Object> plateValues = currPlate.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/plates/" + key, plateValues);
        ref.updateChildren(childUpdates);
    }

    public static void getPlateFromDB(String PlateName)
    {
        DatabaseReference ref = database.getReference("plates");

        ref.orderByChild("PlateName").equalTo(PlateName).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Plate plate = dataSnapshot.getChildren().iterator().next().getValue(Plate.class);
                System.out.println(plate.PlateName);
                //System.out.println(Name.OwnerId);
                //System.out.println(Name.RestName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });

    }


}

