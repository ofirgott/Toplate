package com.example.android.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.android.helloworld.DataObjects.Plate;
import com.example.android.helloworld.DataObjects.Review;
import com.example.android.helloworld.DataObjects.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.android.helloworld.DataObjects.User.*;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    static private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final int RC_SIGN_IN = 9001;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static User currentUser = null;


    List<AuthUI.IdpConfig> providers = Arrays.asList(

            new AuthUI.IdpConfig.EmailBuilder().setRequireName(true).build(),
            new AuthUI.IdpConfig.GoogleBuilder().setSignInOptions(GoogleSignInOptions.DEFAULT_SIGN_IN).build(),
            new AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso("il").build(),
            new AuthUI.IdpConfig.FacebookBuilder().build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();
        //currentUser = getCurrentUserPersonalInfo();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //mFirebaseAuth.signOut();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setCurrentUser(user.getUid());
                    Log.d("AUTH***", "User is " + user.getDisplayName() + ", user id = " + user.getUid());
                    Intent a = new Intent(MainActivity.this, Search.class);
                    setCurrentUser(user.getUid());
                    //Toast.makeText(MainActivity.this, "User Signed In. Hello " + user.getDisplayName() + ". You have " + currentUser.getScore()  + " points", Toast.LENGTH_SHORT).show();
                    startActivity(a);

                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAlwaysShowSignInMethodScreen(true)
                                    .setAvailableProviders(providers)
                                    .setIsSmartLockEnabled(false)
                                    .setLogo(R.mipmap.ic_launcher_foreground)

                                    .build(),
                            RC_SIGN_IN
                    );


                }
            }
        };
    }

    private void setCurrentUser(final String uid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if (data.child(uid).exists()) {
                        currentUser = data.child(uid).getValue(User.class);
                    } else {
                        try {
                            User newUser = new User(mFirebaseAuth.getCurrentUser().getUid(),
                                    mFirebaseAuth.getCurrentUser().getDisplayName(),
                                    50,
                                    new ArrayList<Review>(),
                                    new ArrayList<Plate>(),
                                    0,
                                    "");
                            User.addNewUserToDB(newUser);
                            currentUser = newUser;
                            Log.i("AUTH", "Creating new user in the db: " + mFirebaseAuth.getCurrentUser().getUid());
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                setCurrentUser(mFirebaseAuth.getCurrentUser().getUid());

                //user logged in

                Log.i("AUTH", "Current user id: " + mFirebaseAuth.getCurrentUser().getUid());
                Log.i("AUTH", "Current Display Name: " + mFirebaseAuth.getCurrentUser().getDisplayName());
                Log.i("AUTH", "Current photo url: " + mFirebaseAuth.getCurrentUser().getPhotoUrl());

                Toast.makeText(MainActivity.this, "Welcome" +  mFirebaseAuth.getCurrentUser().getDisplayName()+" . You have points" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, Search.class);
                startActivity(intent);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }

    }


}

