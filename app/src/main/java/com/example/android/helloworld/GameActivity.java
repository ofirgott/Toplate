package com.example.android.helloworld;

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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Queue;


public class GameActivity extends Fragment {

    private String imgPath = "https://mt-backend-familycircle-environment-contentbucket-1qlli1qnqqj9z.s3.amazonaws.com/s3fs-public/styles/398x398/public/recipe/550_RU232402.jpg";
    private WebView slideHolder;
    private TextView _plateName;
    private Queue<String> images;
    private ArrayList<String> optional_tags;
    static boolean first = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =inflater.inflate(R.layout.activity_game,container,false);
        slideHolder = (WebView) root.findViewById(R.id.gameImage);
        slideHolder.loadUrl(imgPath);
        _plateName = (TextView) root.findViewById(R.id.gamePlateName);
        _plateName.setText("Bok Choy Beef Noodles");

        return root;
    }
}
