package com.example.android.helloworld;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.helloworld.DataObjects.Plate;
import com.example.android.helloworld.DataObjects.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.hootsuite.nachos.NachoTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AddReviewActivity2 extends Fragment {

    AutoCompleteTextView platesComplete;
    ArrayList<String> plates = new ArrayList<>();
    TextView restaurantName;
    TextView restaurantGoogleRating;
    RatingBar ratingBar;
    EditText reviewContent;
    ImageButton addPhotoButton;
    Button sendButton;
    NachoTextView nachoTextView;
    String restaurantAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_add_review_2, container, false);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, plates);

        restaurantName = (TextView) root.findViewById(R.id.restaurantNameChosen);
        restaurantGoogleRating = (TextView) root.findViewById(R.id.restaurantGoogleRating);
        Bundle b = getArguments();
        restaurantName.setText(b.getString("restaurantName"));
        restaurantGoogleRating.setText("Restaurant's Google rating : " + b.getFloat("restaurantGoogleRating"));
        restaurantAddress = b.getString("restaurantAddress");
        platesComplete = (AutoCompleteTextView) root.findViewById(R.id.PlatesComplete);
        List<String> allRestPlates = Plate.getAllRestPlates(restaurantName.getText().toString());
        plates.addAll(allRestPlates);
        platesComplete.setAdapter(adapter);

        ratingBar = (RatingBar) root.findViewById(R.id.ratingBarAdd);
        reviewContent = (EditText) root.findViewById(R.id.reviewTextBox);
        addPhotoButton = (ImageButton) root.findViewById(R.id.addPhotoButton);
        sendButton = (Button) root.findViewById(R.id.addReviewSend);
        ArrayAdapter<String> tagsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, Plate.AppTags);
        nachoTextView = (NachoTextView) root.findViewById(R.id.nacho_text_view_addreview);
        nachoTextView.setAdapter(tagsAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> tags = nachoTextView.getChipValues();
                Plate.addToDB(platesComplete.getText().toString(),
                        restaurantName.getText().toString(),
                        restaurantAddress,
                        tags,
                        Arrays.asList("fakeUrl"),
                        new Review(FirebaseAuth.getInstance().getCurrentUser().getUid(), ratingBar.getRating(), reviewContent.getText().toString()));

                getActivity().onBackPressed();
            }
        });


        return root;
    }

}