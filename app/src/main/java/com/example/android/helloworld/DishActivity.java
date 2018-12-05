package com.example.android.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);

        ListView reviewsList = (ListView)findViewById(R.id.reviwesList);

        final String[] values = new String[] { "SO FUCKING DELICIOUS", "This dish is shit, lots of Kusbara", "It's ok, but I wouldn't try it again. But if it's a cold day and you want something that warms you up so you wouldn't feel lonely, then it's ok. Overall it's ok, like really ok", "Yummm", "I wish I could marry Bok Choy <3", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry", "Blackberry"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        reviewsList.setAdapter(adapter);

    }


}
