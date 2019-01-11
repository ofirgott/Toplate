package com.example.android.helloworld;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.helloworld.DataObjects.CameraUpload;
import com.example.android.helloworld.DataObjects.Plate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.hootsuite.nachos.NachoTextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameActivity extends Fragment {

    public  TextView _plateName;
    public  TextView _restaurantName;
    private ArrayList<String> mImageUrls = new ArrayList<>();
    NachoTextView nachoTextView;
    Button submitButton;
    Button skipButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =inflater.inflate(R.layout.activity_game,container,false);
        //slideHolder = (WebView) root.findViewById(R.id.gameImage);
        final Plate randomPlate;
        randomPlate = Plate.getRandomPlate();
        Random r = new Random();
        Integer randomPic = r.nextInt((int)randomPlate.getUrls().size());
        //slideHolder.loadUrl(randomPlate.getUrls().get(restRand));
        _plateName = (TextView) root.findViewById(R.id.gamePlateName);
        _plateName.setText(randomPlate.getPlateName());
        _restaurantName = (TextView) root.findViewById(R.id.gameRestaurantName);
        _restaurantName.setText(randomPlate.getRestName());
        //System.out.println(randomPlate.getPlateName());
        ArrayAdapter<String> tagsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, Plate.AppTags);
        nachoTextView = (NachoTextView)root.findViewById(R.id.nacho_text_view_game);
        nachoTextView.setAdapter(tagsAdapter);
        submitButton = (Button)root.findViewById(R.id.submitButton);
        skipButton = (Button)root.findViewById(R.id.skipButton);

        mImageUrls = new ArrayList<String>(randomPlate.getUrls());
        for (int i = 0; i < mImageUrls.size(); i++){
            mImageUrls.set(i,CameraUpload.getUrl(mImageUrls.get(i)));
        }
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/toplate-85a31.appspot.com/o/Images%2Ftoplate_img_1546792459152.jpg?alt=media&token=768d5a2d-42ca-44de-a084-089526957e77");
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/toplate-85a31.appspot.com/o/Images%2Ftoplate_img_1546792459152.jpg?alt=media&token=768d5a2d-42ca-44de-a084-089526957e77");
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/toplate-85a31.appspot.com/o/Images%2Ftoplate_img_1546792459152.jpg?alt=media&token=768d5a2d-42ca-44de-a084-089526957e77");

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewGame);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), mImageUrls,1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                List<String> tags = nachoTextView.getChipValues();
                randomPlate.insertNewTags(tags);
                Search.UpdatePointsLevel();
                replaceGamePlate();
            }
        });
        skipButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                replaceGamePlate();
            }
        });

        return root;
    }



    void replaceGamePlate(){
        //Plate newPlate = Plate.getRandomPlate();
        //_plateName.setText(newPlate.getPlateName());
        //_restaurantName.setText(newPlate.getRestName());
        //Random r = new Random();
        //Integer randomPic = r.nextInt((int)newPlate.getUrls().size());
        final List<Plate> getPlate = new ArrayList<>();
        Plate.dataBase.getReference(Plate.RESTAURANTS).child("Dabush").child("Shachar Rat Kaparaalea").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Plate plate = mutableData.getValue(Plate.class);
                if (plate != null)
                {
                    getPlate.add(plate);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });


        try {
            Thread.sleep(2000);
        } catch (java.lang.InterruptedException e) {}

        Plate newPlate = getPlate.get(0);
        System.out.println(CameraUpload.getUrl(newPlate.getUrls().get(0)));
        //gameImage.setImageURI(Uri.parse(CameraUpload.getUrl(newPlate.getUrls().get(0))));
        //URL url = new URL(newPlate.getUrls().get(0));
        //Bitmap bmp = BitmapFactory.decodeStream(.openConnection().getInputStream());
        //imageView.setImageBitmap(bmp);
        //_plateName.setText(newPlate.getPlateName());
        //_restaurantName.setText(newPlate.getRestName());
        //gameImage.setImageURI(Uri.parse(CameraUpload.getUrl(newPlate.getUrls().get(randomPic))));
    }




}
