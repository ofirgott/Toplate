package com.example.android.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {


    final String[] plates = new String[] { "Sorbet","Red Bomb","cake", "Mamas Chocolate-Fudge Donuts with red frosting and gummy bears","mana","mana","mana","mana","mana","mana","mana","mana","mana","mana"};
    final String[] restaurants = new String[] { "res1","res2","res3", "The Original Pancakes House aaaaaa aaaaaaa aaaaaaaaaa ","res","res","res","res","res","res","res","res","res","res"};
    final String[] restaurants_addresses = new String[] { "Tel Aviv","Petah Tikva","Modi'in", "aaaaaaaaa saaaaaaaaaa aaaaaaaaaa aaaaaaaaaa aaaaaaaaa aaaaaaa","address","address","address","address","address","address","address","address","address","address"};
    final float[] ratings = new float[] { 5, 1, 3, (float)3.5, (float)2.8,1,1,1,1,1,1,1,1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        ListView reviewsList = (ListView)findViewById(R.id.resultsList);

        resultsAdapter resAdapter = new resultsAdapter();

        reviewsList.setAdapter(resAdapter);

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
