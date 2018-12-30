package com.example.android.helloworld;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.ArrayList;


public class AddReviewActivity extends Fragment {
    AutoCompleteTextView restaurantsComplete;
    ArrayList<String> restaurants = new ArrayList<String>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_add_review,container,false);

        //Autocomplete
        restaurantsComplete = (AutoCompleteTextView) root.findViewById(R.id.RestaurantsComplete);
        restaurants.add("Arepas");
        restaurants.add("American Burger");
        restaurants.add("American Diner");
        restaurants.add("American Dream");
        restaurants.add("Azura");
        restaurants.add("Mezcal");
        restaurants.add("Vong");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,restaurants);
        restaurantsComplete.setAdapter(adapter);


        return root;
     }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button continueButton = (Button) getView().findViewById(R.id.addReviewSend);

        continueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                AddReviewActivity2 addReview2Fragment = new AddReviewActivity2();

                Bundle arguments = new Bundle();
                arguments.putString("restaurantName","Joya");
                addReview2Fragment.setArguments(arguments);


                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container,addReview2Fragment);
                ft.addToBackStack("addReview2");
                ft.commit();



            }
        });




    }

}
