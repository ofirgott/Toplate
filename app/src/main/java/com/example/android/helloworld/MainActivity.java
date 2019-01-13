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
import com.firebase.ui.auth.data.client.AuthUiInitProvider;
import com.firebase.ui.auth.util.data.ProviderUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.GoogleAuthProvider;
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
    private String TAG = "MainActivity";

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
                //FirebaseUser user = firebaseAuth.getCurrentUser();
                final FirebaseUser cu_user = firebaseAuth.getCurrentUser();
                if (cu_user != null) {
                    if(cu_user.getEmail() != null && !cu_user.isEmailVerified() && cu_user.getPhotoUrl() == null){
                        for (int i=0; i < 10; i++)
                        {
                            Toast.makeText(getApplicationContext(),
                                    cu_user.getDisplayName() + ", Please verify your account email \n address in order to log in.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        AuthUI.getInstance()
                                .signOut(getApplicationContext())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // user is now signed out
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    }
                                });
                        return;

                    }
                    setCurrentUser(cu_user.getUid());
                    knownUser = true;
                    Log.d("AUTH***", "User is " + cu_user.getDisplayName() + ", user id = " + cu_user.getUid());
                    Intent a = new Intent(MainActivity.this, Search.class);
                    for (int i=0; i < 10; i++) {
                        Toast.makeText(MainActivity.this, "User Signed In. Hello " + (cu_user.getDisplayName() != null ? cu_user.getDisplayName()  : cu_user.getPhoneNumber())  + ".\n You have " + currentUser.getScore() + " points", Toast.LENGTH_SHORT).show();
                    }
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

                    Plate plate = Plate.getRandomPlate();
                    if (plate != null)
                    {
                        System.out.println(plate.getPlateName() +", " + plate.getRestName());
                    }

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
                            getDisplayName(),
                            50,
                            new ArrayList<Review>(),
                            new ArrayList<Plate>(),
                            0,
                            "",
                            new HashMap<String, Integer>());
                    Log.i("AUTH","Creating new user for " + getDisplayName() + ". id = " + mFirebaseAuth.getCurrentUser().getUid());

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

    public String getDisplayName() {
        if(mFirebaseAuth.getCurrentUser().getDisplayName() != null){
            return mFirebaseAuth.getCurrentUser().getDisplayName();
        }
        else if(mFirebaseAuth.getCurrentUser().getPhoneNumber() != null){
            return mFirebaseAuth.getCurrentUser().getPhoneNumber();
        }
        else return "Anonymous user";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final FirebaseUser cu_user = FirebaseAuth.getInstance().getCurrentUser();
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                setCurrentUser(mFirebaseAuth.getCurrentUser().getUid());
                //

                if(cu_user.getEmail() != null && cu_user.getPhotoUrl() == null && cu_user.getPhotoUrl() == null) { //email auth
                    if (!cu_user.isEmailVerified()) {
                        /* Send Verification Email */
                        cu_user.sendEmailVerification()
                                .addOnCompleteListener(this, new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        /* Check Success */
                                        if (task.isSuccessful()) {
                                            for (int i=0; i < 10; i++) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Verification Email Sent To: " + cu_user.getEmail() + ".\n Pleasse verify your account in order to log in.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                            AuthUI.getInstance()
                                                    .signOut(getApplicationContext())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            // user is now signed out
                                                            finish();
                                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));


                                                        }
                                                    });

                                            return;
                                        } else {
                                            Log.e(TAG, "sendEmailVerification", task.getException());
                                            Toast.makeText(getApplicationContext(),
                                                    "Failed To Send Verification Email!",
                                                    Toast.LENGTH_SHORT).show();
                                            AuthUI.getInstance().signOut(getApplicationContext());
                                            finish();
                                            return;
                                        }
                                    }
                                });

//                        for (int i=0; i < 3; i++) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Your email account is not verified!\n Please verify your account in order to log in.",
//                                    Toast.LENGTH_SHORT).show();
//                        }

//                        AuthUI.getInstance().signOut(getApplicationContext());
//                        finish();
//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        startActivity(intent);

                    }
                }

                    //user logged in

                    Log.i("AUTH", "Current user id: " + mFirebaseAuth.getCurrentUser().getUid());
                    Log.i("AUTH", "Current Display Name: " + mFirebaseAuth.getCurrentUser().getDisplayName());
                    Log.i("AUTH", "Current photo url: " + mFirebaseAuth.getCurrentUser().getPhotoUrl());

                    Toast.makeText(MainActivity.this, "Welcome " + mFirebaseAuth.getCurrentUser().getDisplayName() + "!" , Toast.LENGTH_SHORT).show();
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

