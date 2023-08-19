package com.example.uktourism.ViewModel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.uktourism.Model.DataModel.Place;
import com.example.uktourism.Model.Repository.LoginCallback;
import com.example.uktourism.Model.Repository.PlaceDataRepo;

import java.util.ArrayList;

public class PlaceDataViewModel extends AndroidViewModel {

    private PlaceDataRepo placeDataRepo;
    private LiveData<ArrayList<Place>> allPlacesData;
    private LiveData<ArrayList<Place>> userPlacesData;

    public PlaceDataViewModel(@NonNull Application application) {
        super(application);

        placeDataRepo = new PlaceDataRepo(application);
    }

    public LiveData<ArrayList<Place>> getAllPlacesData() {
        return placeDataRepo.fetchAllPlacesData();
    }
    public LiveData<ArrayList<Place>> getUserPlacesData() {
        return placeDataRepo.fetchUserPlacesData();
    }

    public void addPlaceData(Place place, Uri uri, LoginCallback loginCallback){
        placeDataRepo.addPlaceData(place,uri,loginCallback);
    }

    public void deletePlace(String placeId, LoginCallback loginCallback){
        placeDataRepo.deletePlace(placeId, new LoginCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(String errorMessage) {
                loginCallback.onFailure(errorMessage);
            }
        });
    }
}
