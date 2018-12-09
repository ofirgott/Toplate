package com.example.android.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class DishActivity extends AppCompatActivity {

    final String[] reviewers_names = new String[] { "Oz", "Shahar", "Moni", "Ofir", "Chen", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry"};
    final String[] values = new String[] { "SO FUCKING DELICIOUS", "This dish is shit, lots of Kusbara", "It's ok, but I wouldn't try it again. But if it's a cold day and you want something that warms you up so you wouldn't feel lonely, then it's ok. Overall it's ok, like really ok", "Yummm", "I wish I could marry Bok Choy <3", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry"};
    final float[] ratings = new float[] { 5, 1, 3, (float)3.5, (float)2.8,1,1,1,1,1,1,1,1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);

        ListView reviewsList = (ListView)findViewById(R.id.reviewsList);

        DishActivity.ReviewsAdapter revAdapter = new DishActivity.ReviewsAdapter();

        reviewsList.setAdapter(revAdapter);
    }

    class ReviewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return values.length;
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
            view =  getLayoutInflater().inflate(R.layout.review_item,null);

            TextView textview_reviewer_name = (TextView)view.findViewById(R.id.textView_reviewer_name);
            TextView textview_review_content = (TextView)view.findViewById(R.id.textView_review_content);
            RatingBar ratingBar = (RatingBar)view.findViewById(R.id.ratingBar5);


            textview_reviewer_name.setText(reviewers_names[position]);
            textview_review_content.setText(values[position]);
            ratingBar.setRating(ratings[position]);

            return view;
        }
    }

}
