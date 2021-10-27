package com.swipecrafts.school.data.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.swipecrafts.school.data.manager.Status.ERROR;
import static com.swipecrafts.school.data.manager.Status.LOADING;
import static com.swipecrafts.school.data.manager.Status.SUCCESS;


/**
 * Created by Madhusudan Sapkota on 5/18/2018.
 */
public class Resource<T> {
    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final String message;
    @NonNull
    final int code;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.code = -1;
    }

    private Resource(@NonNull Status status, @NonNull int code, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.code = code;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(SUCCESS, 200, data, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(ERROR, data, msg);
    }

    public static <T> Resource<T> error(@NonNull int code, String msg, @Nullable T data) {
        return new Resource<>(ERROR, code, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }

    public boolean isSuccessful() {
        return status == SUCCESS;
    }

    public boolean isLoading() {
        return status == LOADING;
    }

    public int code() {
        return code;
    }
}
