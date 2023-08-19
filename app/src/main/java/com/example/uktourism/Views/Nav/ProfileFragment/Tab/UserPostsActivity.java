package com.example.uktourism.Views.Nav.ProfileFragment.Tab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.uktourism.Model.DataModel.Place;
import com.example.uktourism.Model.Repository.LoginCallback;
import com.example.uktourism.R;
import com.example.uktourism.ViewModel.PlaceDataViewModel;
import com.example.uktourism.Views.Nav.HomeFragment.AllDestinationAdapter.AllDestinationAdapter;
import com.example.uktourism.Views.Nav.HomeFragment.BestDestinationAdapter.BestDestinationAdapter;
import com.example.uktourism.Views.Nav.ProfileFragment.Tab.UserPlacesAdapter.UserPlacesAdapter;
import com.example.uktourism.Views.ProgressBar.ProgressDialogFragment;

import java.util.ArrayList;

public class UserPostsActivity extends AppCompatActivity {

    private ImageView backBtn;
    private RecyclerView verticalRecyclerView;
    private UserPlacesAdapter userPlacesAdapter;
    private PlaceDataViewModel placeDataViewModel;

    private ArrayList<Place> userPlacesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posts);

        //casting views
        backBtn=findViewById(R.id.back_btn);
        verticalRecyclerView=findViewById(R.id.vertical_recycler_view);

        int xposition = getIntent().getIntExtra("position",0);

        placeDataViewModel = new ViewModelProvider(this).get(PlaceDataViewModel.class);

        verticalRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        placeDataViewModel.getUserPlacesData().observe(this, new Observer<ArrayList<Place>>() {
            @Override
            public void onChanged(ArrayList<Place> places) {
                userPlacesAdapter = new UserPlacesAdapter(getApplicationContext(), places);
                verticalRecyclerView.setAdapter(userPlacesAdapter);
                verticalRecyclerView.scrollToPosition(xposition);

                // Set item click listener
                userPlacesAdapter.setOnItemClickListener(position -> {
                    // Handle item click event here
                    // deleting
                    placeDataViewModel.deletePlace(places.get(position).getPlaceId(), new LoginCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(UserPostsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });

                });
            }
        });

        //back btn
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void showProgressDialog(){
        ProgressDialogFragment progressDialog = new ProgressDialogFragment();
        progressDialog.show(getSupportFragmentManager(), "progress_dialog_tag");
    }

    private void hideProgressDialog(){
        ProgressDialogFragment progressDialog = (ProgressDialogFragment) getSupportFragmentManager().findFragmentByTag("progress_dialog_tag");
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}