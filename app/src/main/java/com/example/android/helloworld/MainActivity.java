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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.example.android.helloworld.DataObjects.User.*;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    static private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final int RC_SIGN_IN = 9001;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static User currentUser = new User();
    private boolean knownUser = false;

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


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //mFirebaseAuth.signOut();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setCurrentUser(user.getUid());
                    knownUser = true;
                    Log.d("AUTH***", "User is " + user.getDisplayName() + ", user id = " + user.getUid());
                    Intent a = new Intent(MainActivity.this, Search.class);
                    Toast.makeText(MainActivity.this, "User Signed In. Hello " + user.getDisplayName() + ". You have " + currentUser.getScore()  + " points", Toast.LENGTH_LONG).show();
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
        DatabaseReference userRef = database.getReference().child("Users");

        userRef.runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(MutableData mutableData) {
                User user = mutableData.child(uid).getValue(User.class);
                if (user == null) {
                    user = new User(mFirebaseAuth.getCurrentUser().getUid(),
                            mFirebaseAuth.getCurrentUser().getDisplayName(),
                            50,
                            new ArrayList<Review>(),
                            new ArrayList<Plate>(),
                            0,
                            "",
                            new HashMap<String, Integer>());
                    Log.i("AUTH","Creating new user for " + mFirebaseAuth.getCurrentUser().getDisplayName() + ". id = " + mFirebaseAuth.getCurrentUser().getUid());

                }

                mutableData.child(uid).setValue(user.toMap());
                //System.out.println("here about to set current user: " + currentUser.getName());
                currentUser = user;
                //System.out.println("after setting current user: " + currentUser.getName());
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    System.out.println(databaseError.getMessage());
                }
            }
        });

        try {
            Thread.sleep(2000);
        } catch (java.lang.InterruptedException e) {}

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
                if (!knownUser) {
                    Intent intent = new Intent(this, Search.class);
                    startActivity(intent);
                }
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

