package com.example.uktourism.Model.DataModel;

import androidx.lifecycle.MutableLiveData;

public interface ResponseListener {
    void onSuccess(User data);
    void onFailure(String errorMessage);
}
