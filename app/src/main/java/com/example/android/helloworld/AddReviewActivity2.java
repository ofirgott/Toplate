package com.example.android.helloworld;


import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.example.android.helloworld.DataObjects.CameraUpload;
import com.example.android.helloworld.DataObjects.Plate;
import com.example.android.helloworld.DataObjects.Review;
import com.google.firebase.storage.StorageReference;

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

    CameraUpload camera;
    private static final int CAMERA_REQ_CODE = 0;
    private StorageReference storageRef;
    private boolean took_image;
    private Uri uri;
    private String image_path;
    protected ImageView imgView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_add_review_2, container, false);

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
        imgView = (ImageView)root.findViewById(R.id.capture_img);
        camera = new CameraUpload(this,imgView,addPhotoButton);

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                image_path = camera.uploadImage();

                Plate.addToDB("Napolitana",
                               restaurantName.getText().toString(),
                               Arrays.asList("Mozarella", "Tomato"),
                               Arrays.asList(image_path),
                               new Review("owner1", ratingBar.getRating(), reviewContent.getText().toString()));

                getActivity().onBackPressed();
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.checkPermissions();
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        camera.onActivityResult(requestCode,resultCode,data);
    }
}
