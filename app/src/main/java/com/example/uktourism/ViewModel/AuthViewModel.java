package com.example.uktourism.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.uktourism.Model.Repository.AuthRepo;
import com.example.uktourism.Model.Repository.LoginCallback;

public class AuthViewModel extends AndroidViewModel {

    private AuthRepo authRepo;

    private boolean isUserLoggedIn;

    public AuthViewModel(@NonNull Application application) {
        super(application);

        authRepo = new AuthRepo(application);
    }

    public boolean isUserLoggedIn() {
        return authRepo.isUserLoggedIn();
    }

    public void setUserLoggedIn(boolean userLoggedIn) {
        isUserLoggedIn = userLoggedIn;
    }

    public void logInUser(String email, String password, LoginCallback loginCallback){

        authRepo.logInUser(email, password, new LoginCallback() {
            @Override
            public void onSuccess() {
                loginCallback.onSuccess();
            }

            @Override
            public void onFailure(String errorMessage) {
                loginCallback.onFailure(errorMessage);
            }
        });

    }

    public void registerUser(String name,String email,String password, LoginCallback  loginCallback){

        authRepo.registerUser(name, email, password, new LoginCallback() {
            @Override
            public void onSuccess() {
                loginCallback.onSuccess();
            }

            @Override
            public void onFailure(String errorMessage) {
                loginCallback.onFailure(errorMessage);
            }
        });
    }

    public void logOut(){
        authRepo.logOut();
        setUserLoggedIn(authRepo.isUserLoggedIn());
    }
}
