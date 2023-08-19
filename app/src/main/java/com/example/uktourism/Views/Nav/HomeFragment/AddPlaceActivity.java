package com.example.uktourism.Views.Nav.HomeFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.uktourism.Model.DataModel.Place;
import com.example.uktourism.Model.Repository.LoginCallback;
import com.example.uktourism.R;
import com.example.uktourism.ViewModel.PlaceDataViewModel;
import com.example.uktourism.Views.Nav.ProfileFragment.PersInfoActivity;
import com.example.uktourism.Views.ProgressBar.ProgressDialogFragment;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class AddPlaceActivity extends AppCompatActivity {

    private FrameLayout editPfp;
    private ShapeableImageView placePic;
    private ImageView backBtn,doneBtn;
    private TextInputEditText placeName,placeLocation,placeDescription;
    private TextInputLayout lplaceName,lplaceLocation,lplaceDescription;
    private ProgressBar imgProgressBar;
    private Place tempPlace;

    private Uri imageUri;
    private static final int My_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int PICK_IMAGE_REQUEST_CODE = 2;

    private PlaceDataViewModel placeDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        //casting views
        placeName = findViewById(R.id.place_name_edit_text);
        lplaceName = findViewById(R.id.place_name_layout);
        placeLocation = findViewById(R.id.place_location_edit_text);
        lplaceLocation = findViewById(R.id.place_location_layout);
        placeDescription = findViewById(R.id.place_description_edit_text);
        lplaceDescription = findViewById(R.id.place_description_layout);
        editPfp = findViewById(R.id.edit_place_pic);
        placePic = findViewById(R.id.place_pic);
        backBtn = findViewById(R.id.back_btn);
        doneBtn = findViewById(R.id.done_btn);
        imgProgressBar = findViewById(R.id.image_progress_bar);

        //viewModel
        placeDataViewModel = new ViewModelProvider(this).get(PlaceDataViewModel.class);

        //picking picture
        editPfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(AddPlaceActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //Request the permission
                    ActivityCompat.requestPermissions(AddPlaceActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            My_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                else {
                    // Permission already granted, proceed with accessing the content URI
                    openGallery();
                }
            }
        });

        //done Btn
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(placeName.getText().toString().isEmpty()){
                    lplaceName.setError("Name can't be empty!");
                }
                else if (placeLocation.getText().toString().isEmpty()) {
                    lplaceLocation.setError("Location can't be empty!");
                }
                else if (placeDescription.getText().toString().isEmpty()) {
                    lplaceDescription.setError("Description can't be empty!");
                }
                else if (imageUri == null) {
                    Toast.makeText(AddPlaceActivity.this, "Please upload an image!", Toast.LENGTH_SHORT).show();
                } else {

                    showProgressDialog();

                    tempPlace = new Place(placeName.getText().toString(),placeLocation.getText().toString()
                            ,placeDescription.getText().toString(),imageUri.toString());
                    placeDataViewModel.addPlaceData(tempPlace, imageUri, new LoginCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(AddPlaceActivity.this, "Place added!", Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                            finish();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            hideProgressDialog();
                            finish();
                        }
                    });
                }
            }
        });

        //back Btn
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void openGallery() {
        Intent photoIntent = new Intent(Intent.ACTION_PICK);
        photoIntent.setType("image/*");
        startActivityForResult(photoIntent, PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == My_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            // Check if the permission was granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with accessing the content URI
                openGallery();
            } else {
                // Permission denied, handle accordingly (e.g., show an error message)
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            setLocalPicture(placePic,imageUri);

        }
    }

    private void setLocalPicture(ImageView imageView, Uri uri){
        // Show the progress bar
        imgProgressBar.setVisibility(View.VISIBLE);
        editPfp.setVisibility(View.GONE);

        Picasso.get()
                .load(uri)
                .error(R.drawable.img_error)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        // Hide the progress bar on success
                        imgProgressBar.setVisibility(View.GONE);
                        editPfp.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        // Hide the progress bar on success
                        imgProgressBar.setVisibility(View.GONE);
                        editPfp.setVisibility(View.VISIBLE);
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