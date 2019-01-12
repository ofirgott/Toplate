package com.example.android.helloworld;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.helloworld.DataObjects.Plate;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import static android.view.View.VISIBLE;
import static com.example.android.helloworld.DataObjects.User.deleteUserFromDB;
import static com.example.android.helloworld.R.id.imageView;
import static java.lang.Math.min;
import static java.security.AccessController.getContext;

public class Search extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    public static TextView navPoints;
    public static TextView navLevel;
    public static ImageView navImg;
    private String TAG = "Search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            finish();
        }
        else{
            Log.i("AUTH_TEST","Current user is :"  + MainActivity.currentUser.getName()+ ". id = " + MainActivity.currentUser.getUid());
        }

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        navImg = (ImageView)  headerView.findViewById(R.id.imageView);
        TextView navUsername = (TextView) headerView.findViewById(R.id.menuHelloName);
        navPoints = (TextView) headerView.findViewById(R.id.menuPoints);
        TextView navResultsNum = (TextView) headerView.findViewById(R.id.menuResultsNum);
        navLevel = (TextView) headerView.findViewById(R.id.menuLevel);
        TextView navReportStatus = (TextView) headerView.findViewById(R.id.menuReportStatus);
        navUsername.setText("Hello "+MainActivity.currentUser.getName()+"!");
        if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null){
            MainActivity.currentUser.setImgUrl(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString() + "?height=500");
            Picasso.get().load(MainActivity.currentUser.getImgUrl()).into(navImg);
            //            navImg.setImageURI(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
//            navImg.setMaxWidth(80);
//            navImg.setMaxHeight(80);
//            navImg.setVisibility(VISIBLE);

        }


        int userPoints = MainActivity.currentUser.getScore();
        navPoints.setText("You have "+userPoints+" points!");

        Integer numOfPlatesToShow;
        String userLevel;
        if (userPoints < Plate.USER_LEVEL_1)
        {
            numOfPlatesToShow = 2;
            userLevel = "Beginner";
        }
        else if (userPoints < Plate.USER_LEVEL_2)
        {
            numOfPlatesToShow = 5;
            userLevel = "Medium";
        }
        else if (userPoints < Plate.USER_LEVEL_3)
        {
            numOfPlatesToShow = 10;
            userLevel = "Pro";
        }
        else
        {
            numOfPlatesToShow = 20;
            userLevel = "Master";
        }
        navResultsNum.setText("You can see up to "+numOfPlatesToShow+" results");
        navReportStatus.setText("Times reported: "+MainActivity.currentUser.getMarkedAsSpammer());
        navPoints.setText("You have "+userPoints+" points!");
        navLevel.setText("Level: "+userLevel);
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

       // User info = User.getCurrentUserPersonalInfo();
    }
    public static void UpdatePointsLevel(){
        int userPoints = MainActivity.currentUser.getScore();
        navPoints.setText("You have "+userPoints+" points!");
        String userLevel;
        if (userPoints < Plate.USER_LEVEL_1)
        {
            userLevel = "Beginner";
        }
        else if (userPoints < Plate.USER_LEVEL_2)
        {
            userLevel = "Medium";
        }
        else if (userPoints < Plate.USER_LEVEL_3)
        {
            userLevel = "Pro";
        }
        else
        {
            userLevel = "Master";
        }
        navPoints.setText("You have "+userPoints+" points!");
        navLevel.setText("Level: "+userLevel);
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
            if (topStackName.equals("search")) {
                super.onBackPressed();
                super.onBackPressed();
                //if we want to get out of the app - getSupportFragmentManager().popBackStack();
            }
            else if (topStackName.equals("game") || topStackName.equals("addReview") || topStackName.equals("contact") || topStackName.equals("results")) {
                getSupportFragmentManager().popBackStack("search", 0);
            }
            else if (topStackName.equals("dish")) {
                getSupportFragmentManager().popBackStack("results", 0);
            }
            else if (topStackName.equals("addReview2")) {
                getSupportFragmentManager().popBackStack("search", 0);
            }

            else {
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
                break;
            case R.id.nav_gain_points:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new GameActivity()).addToBackStack("game").commit();
                break;
            case R.id.nav_contact:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ContactUsFragment(),"contact").addToBackStack("contact").commit();
                break;
            case R.id.nav_sign_out:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                Toast.makeText(Search.this, "User Signed Out", Toast.LENGTH_SHORT).show();
                                MainActivity.currentUser = null;
                                Log.d("AUTH", "user signed out");
                                finish();
                            }
                        });
                break;
            case R.id.nav_delete_account:
                deleteUserFromDB(FirebaseAuth.getInstance().getCurrentUser().getUid());
                AuthUI.getInstance()
                        .delete(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now deleted
                                Toast.makeText(Search.this, "User successfully deleted", Toast.LENGTH_LONG).show();
                                Log.d("AUTH", "User successfully deleted");
                                FirebaseAuth.getInstance().signOut();


                            }
                        });
                finish();

                break;
        }



        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
