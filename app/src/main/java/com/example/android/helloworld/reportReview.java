package com.example.android.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class reportReview extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_review);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        Intent me = getIntent();
        final int reviewIndex = me.getIntExtra("reviewIndex",0);

        Button sendReportReview = (Button)findViewById(R.id.SendReportReview);

        sendReportReview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ResultsActivity.curPlate.reportReview(reviewIndex);
                onBackPressed();
            }
        });



    }
}
