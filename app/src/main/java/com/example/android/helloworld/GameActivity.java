package com.example.android.helloworld;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.helloworld.DataObjects.Plate;
import com.hootsuite.nachos.NachoTextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Random;


public class GameActivity extends Fragment {

    private String imgPath = "https://firebasestorage.googleapis.com/v0/b/toplate-85a31.appspot.com/o/Images%2Ftoplate_img_1546792459152.jpg?alt=media&token=768d5a2d-42ca-44de-a084-089526957e77";
    private WebView slideHolder;
    private TextView _plateName;
    private TextView _restaurantName;
    NachoTextView nachoTextView;
    Button submitButton;
    Button skipButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =inflater.inflate(R.layout.activity_game,container,false);
        slideHolder = (WebView) root.findViewById(R.id.gameImage);
        final Plate randomPlate;
        randomPlate = Plate.getRandomPlate();
        Random r = new Random();
        Integer restRand = r.nextInt((int)randomPlate.getUrls().size());
        slideHolder.loadUrl(randomPlate.getUrls().get(restRand));
        _plateName = (TextView) root.findViewById(R.id.gamePlateName);
        _plateName.setText(randomPlate.getPlateName());
        _restaurantName = (TextView) root.findViewById(R.id.gameRestaurantName);
        _restaurantName.setText(randomPlate.getRestName());
        System.out.println(randomPlate.getPlateName());
        ArrayAdapter<String> tagsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, Plate.AppTags);
        nachoTextView = (NachoTextView)root.findViewById(R.id.nacho_text_view_game);
        nachoTextView.setAdapter(tagsAdapter);
        submitButton = (Button)root.findViewById(R.id.submitButton);
        skipButton = (Button)root.findViewById(R.id.skipButton);
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                List<String> tags = nachoTextView.getChipValues();
                randomPlate.insertNewTags(tags);

                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new GameActivity());
                ft.addToBackStack("game");
                ft.commit();
            }
        });
        skipButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new GameActivity());
                ft.addToBackStack("game");
                ft.commit();
            }
        });
        return root;
    }
}
