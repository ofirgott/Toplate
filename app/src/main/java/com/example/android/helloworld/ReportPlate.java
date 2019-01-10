package com.example.android.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.helloworld.DataObjects.Plate;

public class ReportPlate extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_plate);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        Intent me = getIntent();
        final String plateName = me.getStringExtra("plateName");
        final String restaurantName = me.getStringExtra("restaurantName");

        Button sendReportPlate = (Button)findViewById(R.id.sendReportPlate);
        Button cancelReportPlate = (Button)findViewById(R.id.cancelReportPlate);
        cancelReportPlate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                onBackPressed();
            }
        });
        sendReportPlate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Plate.reportPlate(plateName,restaurantName);
                System.out.println("plate: "+plateName);
                System.out.println("rest: "+restaurantName);
                onBackPressed();
            }
        });
    }
}
