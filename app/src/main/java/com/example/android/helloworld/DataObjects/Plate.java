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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.min;

final public class Plate implements Serializable, Comparable<Plate>  {

    static private final FirebaseDatabase dataBase = FirebaseDatabase.getInstance();

    static public String RESTAURANTS = "Restaurants";
    static public String TAGS = "Tags";

    static public Integer USER_LEVEL_1 = 5;
    static public Integer USER_LEVEL_2 = 15;
    static public Integer USER_LEVEL_3 = 30;

    private String OwnerId;
    private String PlateName;
    private String RestName;
    private Integer Rating;
    private List<Review> Reviews;
    private Map<String, Integer> Tags;
    private List<String> Urls;

    public Plate() {
        // Default constructor required for calls to DataSnapshot.getValue
    }

    public Plate(String Name, String RestName, List<String> tags,  List<String> urls, Review review) {
        this.OwnerId = review.getOwnerId();
        this.PlateName = Name;
        this.RestName = RestName;
        this.Rating = review.getRating();
        this.Tags = new HashMap<>();
        this.Reviews = new ArrayList<>();
        this.Urls = new ArrayList<>();

        for (String tag : tags) {
            this.Tags.put(tag, 1);
        }

        for (String url : urls)
        {
            this.Urls.add(url);
        }

        this.Reviews.add(review);
    }

    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put("OwnerId", OwnerId);
        result.put("PlateName", PlateName);
        result.put("RestName", RestName);
        result.put("Rating", Rating);
        result.put("Tags", Tags);
        result.put("Reviews", Reviews);
        result.put("Urls", Urls);
        return result;
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(String ownerId) {
        this.OwnerId = ownerId;
    }

    public Integer getRating() {
        return Rating;
    }

    public void setRating(Integer rating) {
        Rating = rating;
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

    public List<String> getUrls() {
        return Urls;
    }

    public void setUrls(List<String> urls) {
        Urls = urls;
    }

    @Override
    public int compareTo(@NonNull Plate other) {
        return this.Rating.compareTo(other.Rating);
    }

    public String getPlateKeyInTags()
    {
        return this.RestName + ", " + this.PlateName;
    }

    public void insertToTags() {
        DatabaseReference tagsRef = dataBase.getReference(TAGS);
        final String thisPlateKey = getPlateKeyInTags();
        final Plate thisPlate = this;

        for (final String tag : this.Tags.keySet()) {
            tagsRef.child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataBase.getReference(TAGS).child(tag).child(thisPlateKey).setValue(thisPlate);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println(databaseError.getMessage());
                }
            });
        }
    }

    public void updateUrlsTagsAndReviews(List<String> urls, List<String> tags, Review review) {

        for (String tag : tags) {
            Integer currentTagCount = 1;

            if (this.Tags.containsKey(tag)) {
                Integer prevTagCount = this.Tags.get(tag);
                currentTagCount = ++prevTagCount;
            }

            this.Tags.put(tag, currentTagCount);
        }

        this.Reviews.add(review);
        this.Rating = calcNewRating();

        for (String url : urls)
        {
            this.Urls.add(url);
        }
    }

    public Integer calcNewRating() {
        Integer currentRating = 0;

        for (Review review : this.Reviews) {
            currentRating += review.getRating();
        }

        currentRating = currentRating / this.Reviews.size();
        return currentRating;
    }

    /********STATIC FUNCTIONS*******/

    public static List<String> getAllRestPlates(final String restName) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(RESTAURANTS).child(restName);
        final List<String> plateNames = new ArrayList<>();

        ref.runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.hasChildren()) {
                    Iterator<MutableData> it = mutableData.getChildren().iterator();

                    while (it.hasNext()) {
                        MutableData currData = it.next();
                        plateNames.add(currData.getKey());
                    }
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

        try {
            Thread.sleep(2000);
        } catch (java.lang.InterruptedException e) {}

        return plateNames;
    }

    public static void addToDB(final String PlateName, final String RestName, final List<String> Tags, final List<String> Urls, final Review review) {

        DatabaseReference restRef = dataBase.getReference().child(RESTAURANTS).child(RestName);

        restRef.runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(MutableData mutableData) {
                Plate plate = mutableData.child(PlateName).getValue(Plate.class);
                if (plate == null) {
                    plate = new Plate(PlateName, RestName, Tags, Urls, review);
                } else {
                    plate.updateUrlsTagsAndReviews(Urls, Tags, review);
                }

                mutableData.child(PlateName).setValue(plate.toMap());
                plate.insertToTags();

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


    public static List<Plate> getAllMatchingPlates(final List<String> tags, final Integer userPoints) {
        final List<Plate> matchingPlates = new ArrayList<>();

        DatabaseReference ref = dataBase.getReference("Tags");

        ref.runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(MutableData mutableData) {

                Map<String, List<String>> platesToTagMap = new HashMap<>();

                for (String tag : tags)
                {
                    MutableData currTagData = mutableData.child(tag);
                    List<String> currPlateKeys = new ArrayList<>();

                    if (currTagData != null && currTagData.hasChildren())
                    {
                        Iterable<MutableData> iterableData = currTagData.getChildren();
                        for (MutableData child : iterableData) {
                            currPlateKeys.add(child.getKey());
                        }
                    }

                    platesToTagMap.put(tag, currPlateKeys);
                }

                List<Plate> allMatchingPlates = new ArrayList<>();

                String firstTag = tags.get(0);
                List<String> currPlates = platesToTagMap.get(firstTag);

                for (String currPlateKey : currPlates)
                {
                    boolean shouldInsertToFinalList = true;

                    for (int i = 1; i < tags.size(); i++)
                    {
                        if (!platesToTagMap.get(tags.get(i)).contains(currPlateKey))
                        {
                            shouldInsertToFinalList = false;
                            break;
                        }
                    }

                    if (shouldInsertToFinalList)
                    {
                        allMatchingPlates.add(mutableData.child(firstTag).child(currPlateKey).getValue(Plate.class));
                    }
                }

                if (!allMatchingPlates.isEmpty())
                {
                    Collections.sort(allMatchingPlates, Collections.<Plate>reverseOrder());
                    Integer numOfPlatesToShow;

                    if (userPoints < USER_LEVEL_1)
                    {
                        numOfPlatesToShow = min(2, allMatchingPlates.size());
                    }
                    else if (userPoints < USER_LEVEL_2)
                    {
                        numOfPlatesToShow = min(5, allMatchingPlates.size());
                    }
                    else if (userPoints < USER_LEVEL_3)
                    {
                        numOfPlatesToShow = min(10, allMatchingPlates.size());
                    }
                    else
                    {
                        numOfPlatesToShow = min(20, allMatchingPlates.size());
                    }

                    for (int i = 0; i < numOfPlatesToShow; i++)
                    {
                        matchingPlates.add(allMatchingPlates.get(i));
                    }
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

        try {
            Thread.sleep(2000);
        } catch (java.lang.InterruptedException e) {}

        return matchingPlates;
    }

}
