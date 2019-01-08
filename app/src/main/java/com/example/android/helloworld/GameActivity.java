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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.helloworld.DataObjects.Plate;
import com.hootsuite.nachos.NachoTextView;

import java.io.InputStream;
import java.net.URL;


public class GameActivity extends Fragment {

    private String imgPath = "https://firebasestorage.googleapis.com/v0/b/toplate-85a31.appspot.com/o/Images%2Ftoplate_img_1546792459152.jpg?alt=media&token=768d5a2d-42ca-44de-a084-089526957e77";
    private WebView slideHolder;
    private TextView _plateName;
    NachoTextView nachoTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =inflater.inflate(R.layout.activity_game,container,false);
        slideHolder = (WebView) root.findViewById(R.id.gameImage);
        slideHolder.loadUrl(imgPath);
        _plateName = (TextView) root.findViewById(R.id.gamePlateName);
        _plateName.setText("Bok Choy Beef Noodles");
        ArrayAdapter<String> tagsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, Plate.AppTags);
        nachoTextView = (NachoTextView)root.findViewById(R.id.nacho_text_view_game);
        nachoTextView.setAdapter(tagsAdapter);
        return root;
    }
}
