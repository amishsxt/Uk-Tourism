package com.example.uktourism.Views.Auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uktourism.R;
import com.example.uktourism.ViewModel.AuthViewModel;
import com.example.uktourism.Views.Nav.LandingPageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private TextView myText,myText02;
    private ImageView greyLine,hiker;

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //casting views
        myText=findViewById(R.id.my_text);
        myText02=findViewById(R.id.my_text_02);
        greyLine=findViewById(R.id.grey_line);
        hiker=findViewById(R.id.hiker);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        //Fade In Animation
        Animation fadeInAnimation = new AlphaAnimation(0,1);
        fadeInAnimation.setDuration(1500);
        fadeInAnimation.setFillAfter(true);

        myText.startAnimation(fadeInAnimation);
        myText02.startAnimation(fadeInAnimation);

        //Slide Animation
        Animation xAnimation = new TranslateAnimation(-70f,770f,0f,0f);
        xAnimation.setDuration(2500);
        xAnimation.setFillAfter(true);

        xAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //Navigating to NextPage
                boolean check = authViewModel.isUserLoggedIn();
                if (check) {
                    // User is already logged in, navigate to the landing page or main activity
                    Intent intent = new Intent(SplashActivity.this, LandingPageActivity.class);
                    startActivity(intent);
                    finish(); // Optionally, finish the SplashActivity to prevent going back to it
                } else {
                    // User is not logged in, proceed to the login screen
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        hiker.startAnimation(xAnimation);
    }
}