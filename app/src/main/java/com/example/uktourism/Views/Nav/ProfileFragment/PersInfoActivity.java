package com.example.uktourism.Views.Nav.ProfileFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.uktourism.Model.DataModel.User;
import com.example.uktourism.Model.Repository.LoginCallback;
import com.example.uktourism.R;
import com.example.uktourism.ViewModel.AuthViewModel;
import com.example.uktourism.ViewModel.UserDataViewModel;
import com.example.uktourism.Views.Auth.LoginActivity;
import com.example.uktourism.Views.ProgressBar.ProgressDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersInfoActivity extends AppCompatActivity {

    private TextInputEditText name;
    private TextInputLayout lname;
    private ImageView backBtn,doneBtn;
    private LinearLayout logOut, mainLayout;
    private FrameLayout editPfp;
    private CircleImageView userPfp;
    private AlertDialog.Builder builder;
    private AuthViewModel authViewModel;
    private UserDataViewModel userDataViewModel;
    private ProgressBar imgProgressBar;
    private User tempUser;
    private Uri imageUri;

    private static final int My_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int PICK_IMAGE_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pers_info);

        //Casting views
        name=findViewById(R.id.name_edit_text);
        lname=findViewById(R.id.name_layout);
        editPfp=findViewById(R.id.edit_pfp);
        userPfp=findViewById(R.id.user_pfp);
        backBtn=findViewById(R.id.back_btn);
        doneBtn=findViewById(R.id.done_btn);
        imgProgressBar=findViewById(R.id.image_progress_bar);
        logOut=findViewById(R.id.log_out);
        builder=new AlertDialog.Builder(this);

        showProgressDialog();

        //viewModels
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        //pfp logic
        editPfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(PersInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //Request the permission
                    ActivityCompat.requestPermissions(PersInfoActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            My_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                else {
                    // Permission already granted, proceed with accessing the content URI
                    openGallery();
                }
            }
        });


        //fetching data
        userDataViewModel.getLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                //Update UI
                tempUser = user;
                name.setText(user.getName());
                setPicture(userPfp,user.getUserPfp());

                hideProgressDialog();

            }
        });

        //Navigating back to profileFragment
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Updating data
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().isEmpty()){
                    lname.setError("Name can't be empty!");
                }
                else if(!name.getText().toString().equals(tempUser.getName())
                        || imageUri != null) {

                    showProgressDialog();

                    if(imageUri == null){
                        imageUri = Uri.parse(tempUser.getUserPfp());
                    }

                    tempUser.setName(name.getText().toString());
                    userDataViewModel.updateUserData(tempUser, imageUri, new LoginCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(PersInfoActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
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
                else {
                    hideProgressDialog();
                    finish();
                }
            }
        });

        //LogOut logic
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setTitle("LogOut")
                        .setMessage("Do you want to log out?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        showProgressDialog();
                                        authViewModel.logOut();
                                        hideProgressDialog();

                                        Intent intent = new Intent(PersInfoActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

                                        Toast.makeText(PersInfoActivity.this, "firestme!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.cancel();
                            }
                        })
                        .show();
            }
        });
    }

    private void setPicture(ImageView imageView, String imgUrl){
        // Show the progress bar
        imgProgressBar.setVisibility(View.VISIBLE);
        editPfp.setVisibility(View.GONE);

        Picasso.get()
                .load(imgUrl)
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
            setLocalPicture(userPfp,imageUri);
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