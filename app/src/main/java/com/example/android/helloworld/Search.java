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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

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

public class Search extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
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
        ImageView img = (ImageView)  headerView.findViewById(R.id.imageView);
        TextView navUsername = (TextView) headerView.findViewById(R.id.menuHelloName);
        TextView navPoints = (TextView) headerView.findViewById(R.id.menuPoints);
        TextView navLevel = (TextView) headerView.findViewById(R.id.menuLevel);
        navUsername.setText("Hello "+MainActivity.currentUser.getName()+"!");
//        if(MainActivity.currentUser.getImgUrl() != null){
//            img.setImageURI(Uri.parse(MainActivity.currentUser.getImgUrl()));
//        }
//        else{
//            img.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
//        }
        img.setMaxWidth(89);
        img.setMaxHeight(96);
        img.setVisibility(VISIBLE);
        int userPoints = MainActivity.currentUser.getScore();
        navPoints.setText("You have "+userPoints+" points!");
        Integer numOfPlatesToShow;

        if (userPoints < Plate.USER_LEVEL_1)
        {
            numOfPlatesToShow = 2;
        }
        else if (userPoints < Plate.USER_LEVEL_2)
        {
            numOfPlatesToShow = 5;
        }
        else if (userPoints < Plate.USER_LEVEL_3)
        {
            numOfPlatesToShow = 10;
        }
        else
        {
            numOfPlatesToShow = 20;
        }
        navLevel.setText("You can see up to "+numOfPlatesToShow+" results");

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
                                Toast.makeText(Search.this, "User successfully deleted", Toast.LENGTH_SHORT).show();
                                Log.d("AUTH", "User successfully deleted");
                                finish();
                            }
                        });
                break;
        }



        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
