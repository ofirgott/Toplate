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
    private Button resultsExample;
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


        //Plate.addToDB("Malazit", "Jiraff", Arrays.asList("Asian", "Cilantro", "Spicy"), Arrays.asList("url1"), new Review("owner1", (float) 4, "Good!"));
        //Plate.addToDB("Meat Hummos", "Caspi", Arrays.asList("Hummos", "Meat", "Tomato"), Arrays.asList("url2"), new Review("owner1", (float) 3));
        //Plate.addToDB("Malazit", "Jiraff", Arrays.asList("Asian", "Spicy", "Cream"), Arrays.asList("url3"), new Review("owner2", (float) 3, "nice, expected more"));
        //Plate.addToDB("Afganit", "Jiraff", Arrays.asList("Asian", "Cilantro", "Bacon"), Arrays.asList("url4"), new Review("owner2", (float) 4, "like!"));
        //Plate.addToDB("Eggplant Shakshuka", "Caspi", Arrays.asList("Tomato", "Cilantro", "Eggs"), Arrays.asList("url5"), new Review("owner3", (float) 2.5, "too much sauce"));
        //Plate.addToDB("Pizza Fresca", "Joya", Arrays.asList("Tomato", "Cream", "Mozarella"), Arrays.asList("url6"), new Review("owner3", (float) 1, "Worst plate ever"));
        //Plate.addToDB("Capreza", "Joya", Arrays.asList("Tomato", "Mozarella"), Arrays.asList("url7"), new Review("owner4", (float) 4, "nice pizza and nothing more"));


            //Plate.reportPlate("Capreza", "Joya");
            //try {
              //  Thread.sleep(3000);
            //} catch (java.lang.InterruptedException e) {}



        //List<Plate> matchingPlates = Plate.getAllMatchingPlates(Arrays.asList("Asian", "Cilantro"), 7);
        //System.out.println(matchingPlates.size());
        //if (!matchingPlates.isEmpty())
        //{
            //for (Plate plate : matchingPlates)
            //{
                //System.out.println(plate.getPlateName());
            //}


            //matchingPlates.get(0).reportReview(0);
        //}

        Plate plate = Plate.getRandomPlate();
        if (plate != null) {
            System.out.println(plate.getPlateName());
            plate.insertNewTags(Arrays.asList("Potatos"));
        }
        else
        {
            System.out.println("NULL");
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
