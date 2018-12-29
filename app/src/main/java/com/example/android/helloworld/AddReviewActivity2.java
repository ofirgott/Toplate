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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;


public class AddReviewActivity2 extends Fragment {

    AutoCompleteTextView platesComplete;
    ArrayList<String> plates = new ArrayList<String>();
    TextView restaurantName;
    RatingBar ratingBar;
    EditText reviewContent;
    ImageButton addPhotoButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_add_review_2, container, false);

        platesComplete = (AutoCompleteTextView) root.findViewById(R.id.PlatesComplete);
        plates.add("Bok Choi Beef Noodles");
        plates.add("Bok Choi Beef Noodles");
        plates.add("Bok Choi Beef Noodles");
        plates.add("Bok Choi Beef Noodles");
        plates.add("Bok Choi Beef Noodles");
        plates.add("Bok Choi Beef Noodles");
        plates.add("Bok Choi Beef Noodles");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, plates);

        platesComplete.setAdapter(adapter);

        restaurantName = (TextView) root.findViewById(R.id.restaurantNameChosen);

        Bundle b = getArguments();
        restaurantName.setText("Restaurant: "+b.getString("restaurantName"));

        ratingBar = (RatingBar)root.findViewById(R.id.ratingBarAdd);
        reviewContent = (EditText)root.findViewById(R.id.reviewTextBox);
        addPhotoButton = (ImageButton)root.findViewById(R.id.addPhotoButton);

        return root;
    }

}