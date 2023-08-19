package com.example.uktourism.Views.Nav;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
//import com.example.uktourism.databinding.ActivityMainBinding;
import com.example.uktourism.R;
import com.example.uktourism.Views.Nav.FavFragment.FavFragment;
import com.example.uktourism.Views.Nav.HomeFragment.HomeFragment;
import com.example.uktourism.Views.Nav.ProfileFragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LandingPageActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment =  new HomeFragment();
    FavFragment favFragment = new FavFragment();
    ProfileFragment profileFragment = new ProfileFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        //casting views
        bottomNavigationView = findViewById(R.id.bottom_navigation);


        // Show the initial fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, homeFragment)
                .add(R.id.container, favFragment)
                .add(R.id.container, profileFragment)
                .hide(favFragment)
                .hide(profileFragment)
                .commit();

        // Set up the bottom navigation item click listener
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.nav_home:
                    transaction.show(homeFragment);
                    transaction.hide(favFragment);
                    transaction.hide(profileFragment);
                    break;
                case R.id.nav_fav:
                    transaction.show(favFragment);
                    transaction.hide(homeFragment);
                    transaction.hide(profileFragment);
                    break;
                case R.id.nav_profile:
                    transaction.show(profileFragment);
                    transaction.hide(favFragment);
                    transaction.hide(homeFragment);
                    break;
            }

            transaction.commit();
            return true;
        });
    }
}