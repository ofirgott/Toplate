package com.example.android.helloworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.helloworld.DataObjects.Plate;
import com.example.android.helloworld.DataObjects.Review;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button login;
    private Button signUp;
    private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (EditText)findViewById(R.id.emailText);
        password = (EditText)findViewById(R.id.passwordText);
        login = (Button)findViewById(R.id.loginButton);
        signUp = (Button)findViewById(R.id.signUpButton);

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Validate(email.getText().toString(),password.getText().toString());
            }
        });
        signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        /*Plate.addToDB("Malazit", "Jiraff",Arrays.asList("Asian", "Spicy", "Noodles"), Arrays.asList("fake url1"), new Review("owner1", 4, "best dish ever"));

        Review review = new Review("owner1", 3, "nice one");
        Plate.addToDB("Afganit", "Jiraff", Arrays.asList("Asian", "Spicy", "Rice"), Arrays.asList("fake url2"), review);

        Plate.addToDB("Malazit","Jiraff", Arrays.asList("Asian", "Spicy", "Cilantro", "Cream"), Arrays.asList("fake url3"),  new Review("owner3", 5));
        Plate.addToDB("Meat Hummos", "Caspi", Arrays.asList("Hummos", "Meat"), Arrays.asList("fake url4"), new Review("owner1", 4));

        Review newRev = new Review("owner2", 5, "like!");
        Plate.addToDB("Pad Tai", "Jiraff", Arrays.asList("Asian", "Cilantro", "Spicy"), Arrays.asList("fake url5"), new Review("owner5", 5, "lovely!"));

        Plate.addToDB("Pizza Fresca", "Joya", Arrays.asList("Mozarella", "Cream", "Tomato", "Cilantro"), Arrays.asList("fake url6"), new Review("owner2", 1, "worst dish ever"));
        Plate.addToDB("Eggplant Shakshuka", "Caspi", Arrays.asList("Eggs", "Tomato", "Cilantro"), Arrays.asList("fake url7", "fakeurl2"),  new Review("owner1", 4));

        */
        //List<String> plateNames = Plate.getAllRestPlates("Jiraff");
        //System.out.println(plateNames);

        List<Plate> plateList = Plate.getAllMatchingPlates(Arrays.asList("Cilantro"), 5);

        if (plateList.isEmpty())
        {
            System.out.println("empty list");
        }

        for (Plate plate : plateList){
            System.out.println(plate.getPlateName());
        }
    }

    private void Validate(String userEmail, String userPassword){
        if (userEmail.equals("") && userPassword.equals(""))
        {
            Intent intent = new Intent(MainActivity.this, Search.class);
            startActivity(intent);
        }
        else
        {
            counter--;
            if(counter==0)
            {
                login.setEnabled(false);
            }
        }
    }
}
