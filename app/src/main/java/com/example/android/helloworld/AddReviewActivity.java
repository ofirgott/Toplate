package com.example.android.helloworld;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import java.util.ArrayList;

import static com.google.android.gms.location.places.Place.TYPE_RESTAURANT;


public class AddReviewActivity extends Fragment {

    private static final String TAG = "AddReviewActivity";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_add_review,container,false);

        final SupportPlaceAutocompleteFragment  autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(TYPE_RESTAURANT).setCountry("IL")
                .build();

        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setHint("Enter Restaurant");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (place.getPlaceTypes().contains(TYPE_RESTAURANT)) {
                    Log.i(TAG, "Place: " + place.getName());
                    final String restaurant = place.getName().toString();
                    final float restaurantGoogleRating = place.getRating();
                    final String restaurantAddress = place.getAddress().toString();

                    autocompleteFragment.setMenuVisibility(false);

                    Button continueButton = (Button) getView().findViewById(R.id.addReviewSend);

                    continueButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AddReviewActivity2 addReview2Fragment = new AddReviewActivity2();

                            Bundle arguments = new Bundle();
                            arguments.putString("restaurantName", restaurant);
                            arguments.putString("restaurantAddress", restaurantAddress);
                            arguments.putFloat("restaurantGoogleRating", restaurantGoogleRating);
                            addReview2Fragment.setArguments(arguments);


                            android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_container, addReview2Fragment);
                            ft.addToBackStack("addReview2");
                            ft.commit();
                        }

                    });
                }

                 else {
                    autocompleteFragment.setText("");
                    Toast.makeText(getActivity(), "Place is not a restaurant\nplease enter again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        return root;
    }








    }

