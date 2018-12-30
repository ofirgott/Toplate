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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AddReviewActivity2 extends Fragment {

    AutoCompleteTextView platesComplete;
    ArrayList<String> plates = new ArrayList<>();
    TextView restaurantName;
    RatingBar ratingBar;
    EditText reviewContent;
    ImageButton addPhotoButton;
    Button sendButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_add_review_2, container, false);

        /*plates.add("Bok Choi Beef Noodles");
        plates.add("Bok Choi Beef Noodles");
        plates.add("Bok Choi Beef Noodles");
        plates.add("Bok Choi Beef Noodles");
        plates.add("Bok Choi Beef Noodles");
        plates.add("Bok Choi Beef Noodles");
        plates.add("Bok Choi Beef Noodles"); */


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, plates);

        restaurantName = (TextView) root.findViewById(R.id.restaurantNameChosen);
        Bundle b = getArguments();
        restaurantName.setText(b.getString("restaurantName"));

        platesComplete = (AutoCompleteTextView) root.findViewById(R.id.PlatesComplete);
        List<String> allRestPlates = Plate.getAllRestPlates(restaurantName.getText().toString());
        System.out.println(restaurantName.getText().toString());
        System.out.println(allRestPlates);
        plates.addAll(allRestPlates);
        System.out.println(plates);
        platesComplete.setAdapter(adapter);

        ratingBar = (RatingBar)root.findViewById(R.id.ratingBarAdd);
        reviewContent = (EditText)root.findViewById(R.id.reviewTextBox);
        addPhotoButton = (ImageButton)root.findViewById(R.id.addPhotoButton);
        sendButton = (Button)root.findViewById(R.id.addReviewSend);

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Plate.addToDB("Napolitana",
                               restaurantName.getText().toString(),
                               Arrays.asList("Mozarella", "Tomato"),
                               Arrays.asList("fake1"),
                               new Review("owner1", ratingBar.getRating(), reviewContent.getText().toString()));

                getActivity().onBackPressed();
            }
        });


        return root;
    }

}