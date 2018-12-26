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

        //Review review = new Review("owner1", 4, "best dish ever");
        //Plate.addToDB("Malazit", "Jiraff", Arrays.asList("Asian", "Spicy", "Noodles"), review);

        //Review newRev = new Review("owner3", 3);

        //Plate.addToDB("Malazit", "Jiraff", Arrays.asList("Asian", "Cilantro", "Cream"), newRev);

        List<String> plateNames = Plate.getAllRestPlates("Jiraff");
        System.out.println(plateNames);
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
