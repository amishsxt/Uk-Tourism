package com.example.uktourism.ViewModel;

import android.app.Application;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.uktourism.Model.DataModel.User;
import com.example.uktourism.Model.Repository.LoginCallback;
import com.example.uktourism.Model.Repository.UserDataRepo;

public class UserDataViewModel extends AndroidViewModel {

    private UserDataRepo userDataRepo;
    private LiveData<User> liveData;


    public UserDataViewModel(@NonNull Application application) {
        super(application);

        userDataRepo = new UserDataRepo(application);
        liveData = userDataRepo.fetchUserData();
    }

    public LiveData<User> getLiveData(){
        return liveData;
    }

    public void updateUserData(User user, Uri uri, LoginCallback loginCallback){
        userDataRepo.updateUserData(user, uri, loginCallback);
    }




}
