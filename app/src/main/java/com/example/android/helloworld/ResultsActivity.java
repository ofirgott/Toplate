package com.example.android.helloworld;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import com.example.android.helloworld.DataObjects.Plate;
import com.example.android.helloworld.DataObjects.Review;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;


public class ResultsActivity extends Fragment {

    List<Plate> matchingPlates=null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_results,container,false);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView resultsList = (ListView)getView().findViewById(R.id.resultsList);

        Bundle b = getArguments();
        String[] tagsChosen = b.getStringArray("tagsList");
        int userPoints = 10;
        matchingPlates = Plate.getAllMatchingPlates(Arrays.asList(tagsChosen), userPoints);


        resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //send a request to the server and get information

                Plate plate = matchingPlates.get(position);

                DishActivity plateFragment = new DishActivity();
                Bundle arguments = new Bundle();
                arguments.putString("plateName",plate.getPlateName());
                arguments.putString("restaurantName",plate.getRestName());
                arguments.putString("restaurantAddress",Plate.getAddress(plate.getRestName()));
                arguments.putFloat("numStars",plate.getRating());

                List<Review> reviews = plate.getReviews();
                String[] reviewersNames  = new String[reviews.size()];
                String[] reviewsContent = new String[reviews.size()];
                float[] ratings = new float[reviews.size()];

                for(int i=0; i<reviews.size();i++){
                    if(reviews.get(i).Valid()) {
                        reviewersNames[i] = reviews.get(i).getOwnerId();
                        reviewsContent[i] = reviews.get(i).getVerbalComment();
                        ratings[i] = reviews.get(i).getRating();
                    }
                }
                arguments.putStringArray("reviewersNames",reviewersNames);
                arguments.putStringArray("reviewsContent",reviewsContent);
                arguments.putFloatArray("ratings",ratings);


                List<Map.Entry<String,Integer>> tagsMap= plate.orderedTags();
                System.out.println("size of sorted set : "+tagsMap.size());
                System.out.println("size of map: "+plate.getTags().size());

                int numOfTags = Math.min(7,tagsMap.size());
                int counter = 0;
                String[] shownTags = new String[numOfTags];
                for (Map.Entry<String, Integer> entry : tagsMap)
                {
                    shownTags[counter] = entry.getKey();
                    counter++;
                    if (counter>=numOfTags)
                        break;
                }
                arguments.putStringArray("plateTags",shownTags);

                plateFragment.setArguments(arguments);


                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container,plateFragment);
                ft.addToBackStack("dish");
                ft.commit();
            }
        });

        resultsAdapter resAdapter = new resultsAdapter();
        resultsList.setAdapter(resAdapter);
    }


    class resultsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return matchingPlates.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view =  getLayoutInflater().inflate(R.layout.result_item,null);


            TextView textview_plate = (TextView)view.findViewById(R.id.textView_plate);
            TextView textview_restaurant = (TextView)view.findViewById(R.id.textView_restaurant);
            TextView textview_address = (TextView)view.findViewById(R.id.textView_address);
            RatingBar ratingBar = (RatingBar)view.findViewById(R.id.ratingBar4);

            Plate plate = matchingPlates.get(position);
            textview_plate.setText(plate.getPlateName());
            textview_restaurant.setText(plate.getRestName());
            textview_address.setText(Plate.getAddress(plate.getRestName()));
            ratingBar.setRating((float)(plate.getRating()));

            return view;
        }
    }
}