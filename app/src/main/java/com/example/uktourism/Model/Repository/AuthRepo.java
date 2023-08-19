package com.example.uktourism.Model.Repository;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.uktourism.Model.DataModel.User;
import com.example.uktourism.Views.Auth.LoginActivity;
import com.example.uktourism.Views.Auth.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthRepo {

    private Application application;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRef;

    private boolean isUserLoggedIn;

    public AuthRepo(Application application){
        this.application = application;

        isUserLoggedIn = false;
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            setUserLoggedIn(true);

        }
    }

    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }

    public void setUserLoggedIn(boolean userLoggedIn) {
        isUserLoggedIn = userLoggedIn;
    }

    public void logInUser(String email, String password, LoginCallback loginCallback){
        mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    //Sign in success
                                    loginCallback.onSuccess();
                                }
                                else {
                                    if(task.getException() instanceof FirebaseAuthInvalidUserException){
                                        loginCallback.onFailure("Email is not registered");
                                    }
                                    else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        loginCallback.onFailure("Incorrect password or email");
                                    } else {
                                        loginCallback.onFailure("Error occured");
                                    }
                                }
                            }
                        });
    }

    public void registerUser(String name, String email, String password, LoginCallback loginCallback){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //SignUp success
                            User user = new User(name,email);

                            mRef = FirebaseDatabase.getInstance().getReference("users")
                                    .child(mAuth.getCurrentUser().getUid());

                            mRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        loginCallback.onSuccess();
                                    }
                                    else {
                                        Toast.makeText(application, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                        }
                        else {
                            checkEmail(email, loginCallback);
                        }
                    }
                });
    }

    public void logOut(){
        mAuth.signOut();
        setUserLoggedIn(false);
    }

    private void checkEmail(String email, LoginCallback loginCallback){

        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if(task.isSuccessful()){

                    SignInMethodQueryResult result = task.getResult();

                    if(result != null && result.getSignInMethods() != null && result.getSignInMethods().size() > 0){
                        loginCallback.onFailure("Email already exists");
                    }
                    else{
                        loginCallback.onFailure("User Registration failed");
                    }
                }
            }
        });
    }

}
