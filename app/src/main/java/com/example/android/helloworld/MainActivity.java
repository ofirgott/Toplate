package com.example.android.helloworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button login;
    private Button signUp;
    private int counter = 5;
    private ArrayList<String> mImageUrls = new ArrayList<>();

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
        initImageBitmaps();
    }
    private void initImageBitmaps(){

        mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");

        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");

        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");

        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");

        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");

        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mImageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
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
