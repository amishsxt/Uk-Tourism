package com.example.uktourism.Model.Repository;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.uktourism.Model.DataModel.Place;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

public class PlaceDataRepo {

    private Application application;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRef,m2Ref;
    private DatabaseReference uRef;

    public PlaceDataRepo(Application application) {
        this.application = application;

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("places");
        m2Ref = FirebaseDatabase.getInstance().getReference("user_places")
                .child(currentUser.getUid());
    }

    public LiveData<ArrayList<Place>> fetchAllPlacesData(){
        MutableLiveData<ArrayList<Place>> mutableLiveData = new MutableLiveData<>(new ArrayList<>());

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Place> placeArrayList = new ArrayList<>();

                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    Place place = childSnapshot.getValue(Place.class);
                    placeArrayList.add(place);
                }

                mutableLiveData.setValue(placeArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(application, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return mutableLiveData;
    }

    public LiveData<ArrayList<Place>> fetchUserPlacesData(){
        MutableLiveData<ArrayList<Place>> mutableLiveData = new MutableLiveData<>(new ArrayList<>());

        m2Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Place> placeArrayList = new ArrayList<>();

                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    String userPlaceId = childSnapshot.getKey();

                    // Now, fetch the actual place data using placeId from the "places" node
                    mRef.child(userPlaceId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Place place = snapshot.getValue(Place.class);
                            placeArrayList.add(place);
                            mutableLiveData.setValue(placeArrayList);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return mutableLiveData;
    }

    public void addPlaceData(Place place, Uri imgUri, LoginCallback loginCallback){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("ukTourism")
                .child("placesPic").child(currentUser.getUid());

        String placeId = UUID.randomUUID().toString();
        place.setPlaceId(placeId);

        DatabaseReference dRef = mRef.child(placeId);

        dRef.setValue(place).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    m2Ref.child(placeId).setValue(true);
                    uploadImage(storageReference, imgUri, dRef, "placePic", loginCallback);
                }
                else {
                    Toast.makeText(application, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(application, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        });
    }

    public void deletePlace(String placeId, LoginCallback loginCallback){
        //Deleting from Places
        mRef.child(placeId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Deleting from user_places
                    m2Ref.child(placeId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               loginCallback.onSuccess();
                           }
                           else {
                               loginCallback.onFailure(task.getException().getMessage().toString());
                           }
                       }
                   });
                }
                else {
                    loginCallback.onFailure(task.getException().getMessage().toString());
                }
            }
        });
    }

    public void uploadImage(StorageReference storageReference, Uri imageUri, DatabaseReference reference
            , String dirName, LoginCallback loginCallback) {

        StorageReference fileReference = storageReference.child(UUID.randomUUID().toString());

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
