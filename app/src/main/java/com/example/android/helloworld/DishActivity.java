package com.example.android.helloworld;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import com.example.android.helloworld.DataObjects.CameraUpload;

import java.util.ArrayList;


public class DishActivity extends AppCompatActivity {
    private TextView _plateName;
    private TextView _restaurantName;
    private TextView _restaurantAddress;
    private TextView _plateTags;
    private RatingBar _ratingbar;
    private String[] _reviewers_names;
    private String[] _reviewsContent;
    private float[] _ratings;
    private ArrayList<String> mImageUrls = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);



        _plateName = findViewById(R.id.plateName);
        _restaurantName = findViewById(R.id.restaurantName);
        _restaurantAddress = findViewById(R.id.restaurantAddress);
        _ratingbar = findViewById(R.id.ratingBar);
        _plateTags = findViewById(R.id.plateTags);


        final Bundle b = getIntent().getExtras().getBundle("arguments");
        _plateName.setText(b.getString("plateName"));
        _restaurantName.setText(b.getString("restaurantName"));
        _restaurantAddress.setText(b.getString("restaurantAddress"));
        _ratingbar.setRating(b.getFloat("numStars"));
        _reviewers_names = b.getStringArray("reviewersNames");
        _reviewsContent = b.getStringArray("reviewsContent");
        _ratings = b.getFloatArray("ratings");
        mImageUrls = b.getStringArrayList("Urls");
        for (int i = 0; i < mImageUrls.size(); i++){
            mImageUrls.set(i,CameraUpload.getUrl(mImageUrls.get(i)));
        }
        String[] plateTagsArray = b.getStringArray("plateTags");
        String tagsString = "Tags: ";
        for (int i = 0; i < plateTagsArray.length; i++) {
            tagsString += plateTagsArray[i];
            if (i < plateTagsArray.length - 1) {
                tagsString += ", ";
            }
        }
        _plateTags.setText(tagsString);

        ImageButton reportPlate = (ImageButton) findViewById(R.id.reportPlate);
        reportPlate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReportPlate.class);
                intent.putExtra("plateName", b.getString("plateName"));
                intent.putExtra("restaurantName", b.getString("restaurantName"));
                startActivity(intent);
            }
        });


        ListView reviewsList = (ListView) findViewById(R.id.reviewsList);
        DishActivity.ReviewsAdapter revAdapter = new DishActivity.ReviewsAdapter();
        reviewsList.setAdapter(revAdapter);
        initImageBitmaps();

    }

    class ReviewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return _reviewsContent.length;
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
        public View getView(final int position, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.review_item, null);

            TextView textview_reviewer_name = (TextView) view.findViewById(R.id.textView_reviewer_name);
            TextView textview_review_content = (TextView) view.findViewById(R.id.textView_review_content);
            RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar5);
            ImageButton reportReview = (ImageButton) view.findViewById(R.id.reportReview);
            reportReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), reportReview.class);
                    intent.putExtra("reviewIndex", position);
                    startActivity(intent);
                }
            });


            textview_reviewer_name.setText(_reviewers_names[position]);
            textview_review_content.setText(_reviewsContent[position]);
            ratingBar.setRating(_ratings[position]);
            return view;
        }
    }


    private void initImageBitmaps() {
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/toplate-85a31.appspot.com/o/Images%2Ftoplate_img_1546792459152.jpg?alt=media&token=768d5a2d-42ca-44de-a084-089526957e77");
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/toplate-85a31.appspot.com/o/Images%2Ftoplate_img_1546792459152.jpg?alt=media&token=768d5a2d-42ca-44de-a084-089526957e77");
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/toplate-85a31.appspot.com/o/Images%2Ftoplate_img_1546792459152.jpg?alt=media&token=768d5a2d-42ca-44de-a084-089526957e77");

        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mImageUrls,0);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

}


