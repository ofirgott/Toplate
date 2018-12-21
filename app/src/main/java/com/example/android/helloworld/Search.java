package com.example.android.helloworld;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.allyants.chipview.ChipView;
import com.allyants.chipview.SimpleChipAdapter;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null){
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container,new SearchFragment());
            ft.addToBackStack("search");
            ft.commit();

        }
/*
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ContactUsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_contact);
        }
*/
/*
        Button dishExample = (Button)findViewById(R.id.dishExample);
        Button addExample = (Button)findViewById(R.id.addExample);
        Button gameExample = (Button)findViewById(R.id.gameExample);
        Button resultsExample = (Button)findViewById(R.id.resultsExample);

        dishExample.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Search.this, DishActivity.class);
                startActivity(intent);
            }
        });

        addExample.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Search.this, AddReviewActivity.class);
                startActivity(intent);
            }
        });

        gameExample.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Search.this, GameActivity.class);
                startActivity(intent);
            }
        });

        resultsExample.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Search.this, ResultsActivity.class);
                startActivity(intent);
            }
        });
*/
/*        ChipView cvTag = (ChipView)findViewById(R.id.cvTag);
        ArrayList<Object> data = new ArrayList<>();
        data.add("First Item");
        data.add("Second Item");
        data.add("Third Item");
        data.add("Fourth Item");
        data.add("Fifth Item");
        data.add("Sixth Item");
        data.add("Seventh Item");
        SimpleChipAdapter adapter = new SimpleChipAdapter(data);
        cvTag.setAdapter(adapter);
*/
    }


    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            int topStackIndex = getSupportFragmentManager().getBackStackEntryCount()-1;
            String topStackName = getSupportFragmentManager().getBackStackEntryAt(topStackIndex).getName();
            if(topStackName.equals("search")) {
                super.onBackPressed();
                //if we want to get out of the app - getSupportFragmentManager().popBackStack();
            }
            else if(topStackName.equals("game")||topStackName.equals("addReview")||topStackName.equals("contact")){
            //also from results
                getSupportFragmentManager().popBackStack("search",0);
            }
            else{
                super.onBackPressed();
            }
            /*
            else if(topStackName.equals("dish"))
                getSupportFragmentManager().popBackStack("results",0);
            else if(topStackName.equals("report"))
                getSupportFragmentManager().popBackStack("dish",0);
             */
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    //NEXT TIME - FIGURE OUT HE WHOLE FRAGMENTS VS ACTIVITIES PART!

        switch(item.getItemId()){
            case R.id.nav_add_plate:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AddReviewActivity()).addToBackStack("addReview").commit();
        }
        switch(item.getItemId()){
            case R.id.nav_gain_points:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new GameActivity()).addToBackStack("game").commit();
        }
        switch(item.getItemId()){
            case R.id.nav_contact:
                 getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ContactUsFragment(),"contact").addToBackStack("contact").commit();
        }
        switch(item.getItemId()){
            case R.id.nav_sign_out:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DishActivity()).addToBackStack("exit").commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
