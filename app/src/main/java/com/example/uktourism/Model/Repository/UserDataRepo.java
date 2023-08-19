package com.example.uktourism.Model.Repository;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.uktourism.Model.DataModel.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class UserDataRepo {

    private Application application;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRef;

    public UserDataRepo(Application application) {
        this.application = application;

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("users")
                .child(currentUser.getUid());

    }

    public LiveData<User> fetchUserData(){
        MutableLiveData<User> mutableLiveData = new MutableLiveData<>();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                mutableLiveData.setValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return mutableLiveData;
    }

    public void updateUserData(User user, Uri imgUri, LoginCallback loginCallback){

        if(currentUser != null){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("ukTourism")
                    .child("usersPfp").child(currentUser.getUid());

            mRef.setValue(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                // Update was successful
                                if(!user.getUserPfp().toString().equals(imgUri.toString())){
                                    uploadImage(storageReference, imgUri, mRef,"userPfp",loginCallback);
                                }
                                else {
                                    loginCallback.onSuccess();
                                }
                            }
                            else {
                                // Handle update failure
                                Toast.makeText(application, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void uploadImage(StorageReference storageReference, Uri imageUri, DatabaseReference reference
            , String dirName, LoginCallback loginCallback) {

        StorageReference fileReference = storageReference.child(UUID.randomUUID().toString());


        storageReference.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                // Delete each item (file) in the folder
                item.delete().addOnSuccessListener(deleteTask -> {
                    // Item (file) deleted successfully
                }).addOnFailureListener(exception -> {
                    // Handle delete failure
                    Toast.makeText(application, exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
                });
            }

            UploadTask uploadTask = compressImage(imageUri, fileReference);

            //upload new pic
            uploadTask.addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    reference.child(dirName).setValue(String.valueOf(uri));
                                    loginCallback.onSuccess();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(application, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    throw error.toException();
                                }
                            });
                        }
                    }).addOnFailureListener(e -> {
                        // Handle download URL retrieval failure
                        Toast.makeText(application, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    });
                }
                else {
                    Toast.makeText(application, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private UploadTask compressImage(Uri uri, StorageReference storageReference){
        try{
            Bitmap bmp = MediaStore.Images.Media.getBitmap(application.getContentResolver(), uri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = storageReference.putBytes(data);

            return uploadTask;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}
