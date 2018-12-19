package com.example.android.helloworld.DataObjects;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    static final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private String OwnerId;
    private String PlateName;
    private String RestName;
    private List<Review> Reviews;
    private Map<String, Integer> Tags;

    public Plate() {
        // Default constructor required for calls to DataSnapshot.getValue
    }

    public Plate(String Name, String RestName, List<String> tags, Review review) {
        this.OwnerId = review.getOwnerId();
        this.PlateName = Name;
        this.RestName = RestName;
        this.Tags = new HashMap<>();
        this.Reviews = new ArrayList<>();

        for (String tag : tags)
        {
            this.Tags.put(tag, 1);
        }

        this.Reviews.add(review);
    }

    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put("OwnerId", OwnerId);
        result.put("PlateName", PlateName);
        result.put("RestName", RestName);
        result.put("Tags", Tags);
        result.put("Reviews", Reviews);
        return result;
    }

    public Map<String, Object> toUpdatesMap()
    {
        Map<String, Object> updatesMap = new HashMap<>();
        updatesMap.put("Tags", Tags);
        updatesMap.put("Reviews", Reviews);

        return updatesMap;
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

    public void insertToTags()
    {
        DatabaseReference tagsRef = database.getReference("Tags");
        final String thisPlateName = this.PlateName;
        final Plate thisPlate = this;

        for (final String tag: this.Tags.keySet())
        {
            tagsRef.child(tag).addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Plate.database.getReference("Tags").child(tag).child(thisPlateName).setValue(thisPlate);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println(databaseError.getMessage());
                }
            });
        }
    }


    /********STATIC FUNCTIONS*******/

    public static List<String> getAllRestPlates(final String restName)
    {
        DatabaseReference ref = database.getReference("Restaurants");
        final List<String> plateNames = new ArrayList<>();

        ref.orderByChild("RestName").equalTo(restName).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.getChildrenCount() > 1)
                    {
                        System.out.println("more than one restaurant with the same name is illegal");
                        return;
                    }

                    DataSnapshot restDs = dataSnapshot.child(restName);
                    for (DataSnapshot ds : restDs.getChildren())
                    {
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

    public static final void updateExistingPlateInDB(DataSnapshot plateSnap, List<String> Tags, Review review)
    {
        System.out.println("in existing");
        String plateKey = plateSnap.getKey();
        Plate plate = plateSnap.getValue(Plate.class);

        for (String tag: Tags)
        {
            Integer currentTagCount = 1;

            if (plate.Tags.containsKey(tag))
            {
                Integer prevTagCount = plate.Tags.get(tag);
                currentTagCount = ++prevTagCount;
            }

            plate.Tags.put(tag, currentTagCount);
        }

        plate.Reviews.add(review);

        System.out.println(plateKey);
        DatabaseReference plateRef = plateSnap.child(plateKey).getRef(); //TODO
        plateRef.child("Tags").setValue(plate.Tags);
        plateRef.child("Reviews").setValue(plate.Reviews);

        plate.insertToTags();
    }


    public static void addToDB(final String PlateName, final String RestName, final List<String> Tags, final Review review) {

        DatabaseReference ref = database.getReference();
        ref.child("Restaurants").child(RestName).orderByChild("PlateName").equalTo(PlateName).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.getChildrenCount() > 1)
                    {
                        System.out.println("More than one plate with the same name at the same restaurant");
                    }
                    else
                    {
                        Plate.updateExistingPlateInDB(dataSnapshot.getChildren().iterator().next(), Tags, review);
                    }
                }
                else
                {
                    Plate currPlate = new Plate(PlateName, RestName, Tags, review);

                    DatabaseReference currRef = Plate.database.getReference("Restaurants");

                    String key = currRef.child(RestName).push().getKey();

                    Map<String, Object> plateValues = currPlate.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();

                    childUpdates.put("/" + RestName +  "/"  + key, plateValues);
                    currRef.updateChildren(childUpdates);
                    currPlate.insertToTags();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        }
        );
    }


}

