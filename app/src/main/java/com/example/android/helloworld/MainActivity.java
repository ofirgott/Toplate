package com.example.android.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    public static final int RC_SIGN_IN = 9001;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
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
        mFirebaseAuth.signOut();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent a = new Intent(MainActivity.this, MyProfile.class);
                    startActivity(a);
                     Toast.makeText(MainActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(MainActivity.this, SearchFragment.class);
                startActivity(intent);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN) {
            if(resultCode == RESULT_OK) {
                //user logged in
                Log.d("AUTH","Current user e-Mail: " + mFirebaseAuth.getCurrentUser().getEmail());
                Log.d("AUTH","Current user Display Name: " + mFirebaseAuth.getCurrentUser().getDisplayName());
                Intent intent = new Intent(this, SearchFragment.class);
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
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);

    }
}

