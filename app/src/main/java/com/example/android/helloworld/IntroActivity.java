package com.example.android.helloworld;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
//        addSlide(new IntroSlide1());
//        addSlide(new IntroSlide2());

        addSlide(new Intro1());
        addSlide(new Intro2());
        addSlide(new Intro3());
        addSlide(new Intro4());
        //addSlide(thirdFragment);
        //addSlide(fourthFragment);

        // Instead of fragments, you can also use our default slide.
        // Just create a `SliderPage` and provide title, description, background and image.
        // AppIntro will do the rest.
//        SliderPage sliderPage = new SliderPage();
//        sliderPage.setTitle("Welcome to Toplate!");
//        sliderPage.setImageDrawable(R.mipmap.logo);
//        sliderPage.setBgColor(Color.parseColor("#2196F3"));
//        addSlide(AppIntroFragment.newInstance(sliderPage));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#000000"));
        setSeparatorColor(Color.parseColor("#000000"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent a = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(a);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent a = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(a);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
//        Intent a = new Intent(IntroActivity.this, MainActivity.class);
//        startActivity(a);
    }

    public static class Intro1 extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_intro1, container, false);
        }
    }

    public static class Intro2 extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_intro2, container, false);
        }

    }

    public static class Intro3 extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_intro3, container, false);
        }
    }

    public static class Intro4 extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_intro4, container, false);
        }
    }


}
