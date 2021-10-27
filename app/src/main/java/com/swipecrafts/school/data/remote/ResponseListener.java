package com.swipecrafts.school.data.remote;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.ErrorsUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Madhusudan Sapkota on 6/23/2019.
 */
public class ResponseListener<T> implements Callback<T> {

    private MutableLiveData<ApiResponse<T>> response;

    public ResponseListener() {
        response = new MutableLiveData<>();
        response.postValue(ApiResponse.loading(null));
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (response.isSuccessful()) this.response.postValue(ApiResponse.success(mapResponse(response)));
        else this.response.postValue(ApiResponse.error(response.code(), ErrorsUtil.getMessage(response.code()), response.body()));
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable error) {
        this.response.postValue(ApiResponse.error(Constants.UNKNOWN_ERROR_CODE, error.getMessage(), null));
    }

    @NonNull
    protected T mapResponse(@NonNull Response<T> response){
        return response.body();
    }

    public LiveData<ApiResponse<T>> buildResponse() {
        return response;
    }
}
