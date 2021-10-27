package com.swipecrafts.school.data.model.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Madhusudan Sapkota on 5/18/2018.
 */
public class ApiResponse<ResponseType> {

    public static final int SUCCESS = 200;
    public static final int NOT_FOUND = 404;
    public static final int LOADING = -1;

    public final int status;
    public final String message;
    public final ResponseType data;

    public ApiResponse(int status, String message, ResponseType data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccessful() {
        return status >= 200 && status < 300;
    }

    public boolean isLoading() {
        return status == LOADING;
    }

    public static <ResponseType> ApiResponse<ResponseType> loading(@Nullable ResponseType data) {
        return new ApiResponse<>(LOADING, null, data);
    }

    public static <ResponseType> ApiResponse<ResponseType> success(@NonNull ResponseType data) {
        return new ApiResponse<>(SUCCESS, null, data);
    }

    public static <ResponseType> ApiResponse<ResponseType> error(int status, String msg, @Nullable ResponseType data) {
        return new ApiResponse<>(status, msg, data);
    }
}
