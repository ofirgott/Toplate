package com.example.android.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;



    public class DishActivity extends Fragment {
        private TextView _plateName;
        private TextView _restaurantName;
        private TextView _restaurantAddress;
        private TextView  _plateTags;
        private RatingBar _ratingbar;
        private String[] _reviewers_names;
        private String[] _reviewsContent;
        private float[] _ratings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_dish,container,false);
        _plateName = root.findViewById(R.id.plateName);
        _restaurantName = root.findViewById(R.id.restaurantName);
        _restaurantAddress = root.findViewById(R.id.restaurantAddress);
        _ratingbar = root.findViewById(R.id.ratingBar);
        _plateTags = root.findViewById(R.id.plateTags);


        Bundle b = getArguments();
        _plateName.setText(b.getString("plateName"));
        _restaurantName.setText(b.getString("restaurantName"));
        _restaurantAddress.setText(b.getString("restaurantAddress"));
        _ratingbar.setRating(b.getFloat("numStars"));
        _reviewers_names = b.getStringArray("reviewersNames");
        _reviewsContent = b.getStringArray("reviewsContent");
        _ratings = b.getFloatArray("ratings");

        String[] plateTagsArray = b.getStringArray("plateTags");
        String tagsString = "Tags: ";
        for (int i=0; i<plateTagsArray.length; i++){
            tagsString+=plateTagsArray[i];
            if(i<plateTagsArray.length-1) {
                tagsString+=", ";
            }
        }
        _plateTags.setText(tagsString);



        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView reviewsList = (ListView)getView().findViewById(R.id.reviewsList);
        DishActivity.ReviewsAdapter revAdapter = new DishActivity.ReviewsAdapter();
        reviewsList.setAdapter(revAdapter);
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
            view =  getLayoutInflater().inflate(R.layout.review_item,null);

            TextView textview_reviewer_name = (TextView)view.findViewById(R.id.textView_reviewer_name);
            TextView textview_review_content = (TextView)view.findViewById(R.id.textView_review_content);
            RatingBar ratingBar = (RatingBar)view.findViewById(R.id.ratingBar5);
            ImageButton reportReview = (ImageButton)view.findViewById(R.id.reportReview);


            reportReview.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(getActivity(), reportReview.class);
                    intent.putExtra("reviewIndex",Integer.toString(position));
                    intent.putExtra("plateId","2222");
                    intent.putExtra("userId","3333");
                    startActivity(intent);
                }
            });


            textview_reviewer_name.setText(_reviewers_names[position]);
            textview_review_content.setText(_reviewsContent[position]);
            ratingBar.setRating(_ratings[position]);

            return view;
        }
    }

}
