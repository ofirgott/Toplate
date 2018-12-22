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

import java.util.ArrayList;
import java.util.Arrays;


public class ResultsActivity extends Fragment {


    final String[] plates = new String[] { "Sorbet","Red Bomb","cake", "Mamas Chocolate-Fudge Donuts with red frosting and gummy bears","mana","mana","mana","mana","mana","mana","mana","mana","mana","mana"};
    final String[] restaurants = new String[] { "res1","res2","res3", "The Original Pancakes House aaaaaa aaaaaaa aaaaaaaaaa ","res","res","res","res","res","res","res","res","res","res"};
    final String[] restaurants_addresses = new String[] { "Tel Aviv","Petah Tikva","Modi'in", "aaaaaaaaa saaaaaaaaaa aaaaaaaaaa aaaaaaaaaa aaaaaaaaa aaaaaaa","address","address","address","address","address","address","address","address","address","address"};
    final float[] ratings = new float[] { 5, 1, 3, (float)3.5, (float)2.8,1,1,1,1,1,1,1,1};

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

        resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //send a request to the server and get information
                final String plateName="Bok Choy Beef Noodles";
                final String restaurantName = "Vong";
                final String restaurantAddress = "27 Rothschild Blvd, Tel Aviv";
                final float numStars = (float)4.5;
                final String[] reviewersNames  = new String[] { "Oz", "Shahar", "Moni", "Ofir", "Chen", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry"};
                final String[] reviewsContent = new String[] { "SO FUCKING DELICIOUS", "This dish is shit, lots of Kusbara", "It's ok, but I wouldn't try it again. But if it's a cold day and you want something that warms you up so you wouldn't feel lonely, then it's ok. Overall it's ok, like really ok", "Yummm", "I wish I could marry Bok Choy <3", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry"};
                final float[] ratings = new float[] { 5, 1, 3, (float)3.5, (float)2.8,1,1,1,1,1,1,1,1};

                DishActivity plateFragment = new DishActivity();
                Bundle arguments = new Bundle();
                arguments.putString("plateName",plateName);
                arguments.putString("restaurantName",restaurantName);
                arguments.putString("restaurantAddress",restaurantAddress);
                arguments.putFloat("numStars",numStars);
                arguments.putStringArray("reviewersNames",reviewersNames);
                arguments.putStringArray("reviewsContent",reviewsContent);
                arguments.putFloatArray("ratings",ratings);

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
            return plates.length;
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


            textview_plate.setText(plates[position]);
            textview_restaurant.setText(restaurants[position]);
            textview_address.setText(restaurants_addresses[position]);
            ratingBar.setRating(ratings[position]);

            return view;
        }
    }
}