package com.example.android.helloworld.DataObjects;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static java.lang.Math.min;

final public class Plate implements Serializable, Comparable<Plate>  {

    static private final FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
    static private final FirebaseStorage storageBase = FirebaseStorage.getInstance();

    static public String RESTAURANTS = "Restaurants";
    static public String TAGS = "Tags";

    static public String[] AppTags = new String[]{"Tortilla Chips", "Melted Cheese", "Salsa", "Guacamole", "Mexico", "Jalapeno","Asian","Cilantro","Pasta"};
    static public String[] AppRestaurants = new String[] {"Arepas","American Diner","American Burger","Azura","Amora Mio", "Alupa", "Vong", "Viva Mia", "Velvet Italiano","Mezcal","Joya","Jiraff"};
    static public String[] AppAddresses = new String[] {"Rothschild 1, Tel Aviv","Rothschild 2, Tel Aviv","Rothschild 3, Tel Aviv","Rothschild 4, Tel Aviv","Rothschild 5, Tel Aviv","Rothschild 6, Tel Aviv","Rothschild 7, Tel Aviv","Rothschild 8, Tel Aviv","Rothschild 9, Tel Aviv","Habait Shel Oz","Habarzel 1","Habarzel 2"};

    static public Integer USER_LEVEL_1 = 150;
    static public Integer USER_LEVEL_2 = 500;

    static public Integer MAX_ALLOWED_REPORTS = 5;

    private String OwnerId;
    private String PlateName;
    private String RestName;
    private String RestAddress;
    private Float Rating;
    private Integer ReportsCounter;
    private List<Review> Reviews;
    private Map<String, Integer> Tags;
    private List<String> Urls;

    public Plate() {
        // Default constructor required for calls to DataSnapshot.getValue
    }

    public Plate(String Name, String RestName, String RestAddress, List<String> tags,  List<String> urls, Review review) {
        this.OwnerId = review.getOwnerId();
        this.PlateName = Name;
        this.RestName = RestName;
        this.RestAddress = RestAddress;
        this.Rating = review.getRating();
        this.ReportsCounter = 0;
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

//    public static String getAddress(String restName){
//        for (int i = 0; i < AppRestaurants.length; i++){
//            if (AppRestaurants[i].equals(restName)){
//                return AppAddresses[i];
//            }
//        }
//        return null;
//    }



    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put("OwnerId", OwnerId);
        result.put("PlateName", PlateName);
        result.put("RestName", RestName);
        result.put("Rating", Rating);
        result.put("Tags", Tags);
        result.put("Reviews", Reviews);
        result.put("Urls", Urls);
        result.put("ReportsCounter", ReportsCounter);
        return result;
    }

    public Integer getReportsCounter() {
        return ReportsCounter;
    }

    public void setReportsCounter(Integer reportsCounter) {
        ReportsCounter = reportsCounter;
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(String ownerId) {
        this.OwnerId = ownerId;
    }

    public Float getRating() {
        return Rating;
    }

    public void setRating(Float rating) {
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

    public String getRestAddress() {
        return RestAddress;
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

    public String PlateKeyInTags()
    {
        return this.RestName + ", " + this.PlateName;
    }

    public void insertToTags()
    {
        DatabaseReference tagsRef = dataBase.getReference(TAGS);
        final String thisPlateKey = PlateKeyInTags();
        final Plate thisPlate = this;

        for (final String tag : this.Tags.keySet()) {
            tagsRef.child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataBase.getReference(TAGS).child(tag).child(thisPlateKey).setValue(thisPlate.toMap());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println(databaseError.getMessage());
                }
            });
        }
    }

    public void removeFromTags()
    {
        DatabaseReference tagsRef = dataBase.getReference(TAGS);
        final Plate thisPlate = this;
        for (final String tag: this.Tags.keySet())
        {
            tagsRef.runTransaction(new Transaction.Handler() {
                public Transaction.Result doTransaction(MutableData mutableData) {
                    mutableData.child(tag).child(thisPlate.PlateKeyInTags()).setValue(null);
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
    }

    private void updateTags(List<String> tags)
    {
        for (String tag : tags) {
            Integer currentTagCount = 1;

            if (this.Tags.containsKey(tag)) {
                Integer prevTagCount = this.Tags.get(tag);
                currentTagCount = ++prevTagCount;
            }

            this.Tags.put(tag, currentTagCount);
        }
    }

    public void insertNewTags(final List<String> newTags)
    {
        DatabaseReference ref = dataBase.getReference(RESTAURANTS).child(this.RestName).child(this.PlateName);
        final Plate currPlate = this;
        ref.runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(MutableData mutableData) {
                Plate plate = mutableData.getValue(Plate.class);
                if (plate != null)
                {
                    plate.updateTags(newTags);
                    mutableData.child(TAGS).setValue(plate.Tags);
                    plate.insertToTags();
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

    public void updateUrlsTagsAndReviews(List<String> urls, List<String> tags, Review review) {

        updateTags(tags);

        this.Reviews.add(review);
        this.Rating = calcNewRating();

        for (String url : urls)
        {
            this.Urls.add(url);
        }
    }

    public Float calcNewRating() {
        Float currentRating = 0.f;

        for (Review review : this.Reviews) {
            currentRating += review.getRating();
        }

        currentRating = currentRating / this.Reviews.size();
        return currentRating;
    }

    public void reportReview(final Integer reviewIndex)
    {
        final Plate currPlate = this;
        if (reviewIndex < this.Reviews.size())
        {
            dataBase.getReference(RESTAURANTS).child(currPlate.RestName).runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    Plate plate = mutableData.child(currPlate.PlateName).getValue(Plate.class);
                    if (plate != null)
                    {
                        Review rev = currPlate.Reviews.get(reviewIndex);
                        if (rev.Valid())
                        {
                            rev.setReportsCounter(rev.getReportsCounter() + 1);
                            mutableData.child(currPlate.PlateName).child("Reviews").child(reviewIndex.toString()).setValue(rev);
                            for (String tag: currPlate.Tags.keySet())
                            {
                                dataBase.getReference(TAGS).child(tag).child(currPlate.PlateKeyInTags()).child("Reviews").child(reviewIndex.toString()).setValue(rev);
                            }
                        }
                    }

                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    if (databaseError != null) {
                        System.out.println(databaseError.getMessage());
                    }
                }
            });

        }
    }

    public List<Map.Entry<String,Integer>> orderedTags()
    {
        List<Map.Entry<String,Integer>> tagsList = new ArrayList<>();
        tagsList.addAll(this.Tags.entrySet());

        Collections.sort(tagsList, new Comparator<Map.Entry<String,Integer>>() {
            @Override public int compare(Map.Entry<String,Integer> e1, Map.Entry<String,Integer> e2) {
                int res = e2.getValue().compareTo(e1.getValue());
                return res;
            }
        });

        return tagsList;
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

    public static void addToDB(final String PlateName, final String RestName,final String RestAddress, final List<String> Tags, final List<String> Urls, final Review review) {

        DatabaseReference restRef = dataBase.getReference().child(RESTAURANTS).child(RestName);

        restRef.runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(MutableData mutableData) {
                Plate plate = mutableData.child(PlateName).getValue(Plate.class);
                if (plate == null) {
                    plate = new Plate(PlateName, RestName, RestAddress, Tags, Urls, review);
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
                        numOfPlatesToShow = min(3, allMatchingPlates.size());
                    }
                    else if (userPoints < USER_LEVEL_2)
                    {
                        numOfPlatesToShow = min(7, allMatchingPlates.size());
                    }
                    else
                    {
                        numOfPlatesToShow = min(15, allMatchingPlates.size());
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
            Thread.sleep(3000);
        } catch (java.lang.InterruptedException e) {}

        return matchingPlates;
    }

    static public void reportPlate(final String plateName, final String restName)
    {
        DatabaseReference restRef = dataBase.getReference().child(RESTAURANTS).child(restName);

        restRef.runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(MutableData mutableData) {
                Plate plate = mutableData.child(plateName).getValue(Plate.class);
                if (plate  != null)
                {
                    plate.ReportsCounter++;
                    if (plate.ReportsCounter > MAX_ALLOWED_REPORTS)
                    {
                        mutableData.child(plateName).setValue(null);
                        plate.removeFromTags();
                    }
                    else
                    {
                        mutableData.child(plateName).child("ReportsCounter").setValue(plate.ReportsCounter);
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
    }

    public static Plate getRandomPlate()
    {
        DatabaseReference ref = dataBase.getReference().child(RESTAURANTS);
        final List<Plate> randomPlate = new ArrayList<>();

        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getChildrenCount() > 0) {
                    Random r = new Random();
                    Integer restRand = r.nextInt((int) mutableData.getChildrenCount());
                    System.out.println("rest number: "+restRand);
                    Integer counter = 0;

                    for (MutableData child : mutableData.getChildren())
                    {
                        System.out.println("rest counter: "+counter);
                        if (counter == restRand)
                        {
                            Integer plateRand = r.nextInt((int) child.getChildrenCount());
                            System.out.println("plate number: "+plateRand);
                            int plateCounter = 0;
                            for (MutableData newChild : child.getChildren())
                            {
                                System.out.println("plate counter: "+plateCounter);
                                if (plateCounter == plateRand)
                                {
                                    Plate plate = newChild.getValue(Plate.class);
                                    System.out.println("chosen plate name: "+plate.getPlateName());
                                    randomPlate.add(plate);
                                    break;
                                }

                                plateCounter ++;
                            }

                            break;
                        }

                        counter++;
                    }
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });

        try {
            Thread.sleep(5000);
        } catch (java.lang.InterruptedException e) {}

        if (!randomPlate.isEmpty())
        {
            return randomPlate.get(0);
        }
        else
        {
            return null;
        }
    }



}
