package com.swipecrafts.school.data.remote;

import android.util.Log;

import com.swipecrafts.school.data.DataManager;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Madhusudan Sapkota on 1/26/2018.
 */

public class RequestInterceptor implements Interceptor {
    private ApiHeader mApiHeader;
    private int loggedInMode;

    @Inject
    public RequestInterceptor(ApiHeader mApiHeader, int loggedInMode) {
        this.mApiHeader = mApiHeader;
        this.loggedInMode = loggedInMode;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Response response = chain.proceed(originalRequest);


        if (loggedInMode == DataManager.LoggedInMode.LOGGED_IN_MODE.value()) {
            Log.e("intercepting", "loggedIn");
            Request compressedRequest = originalRequest.newBuilder()
                    .header("api_key", mApiHeader.getProtectedApiHeader().getApiKey())
                    .header("sshkey", mApiHeader.getProtectedApiHeader().getSSHKey())
                    .header("seskey", mApiHeader.getProtectedApiHeader().getSESKey())
                    .header("sesnid", mApiHeader.getProtectedApiHeader().getSESNId())
                    .header("sesuid", String.valueOf(mApiHeader.getProtectedApiHeader().getSESUId()))
                    .method(originalRequest.method(), originalRequest.body())
                    .build();

            response = chain.proceed(compressedRequest);
        } else {
            Log.e("intercepting", "loggedOut");
            Request compressedRequest = originalRequest.newBuilder()
                    .header("api_key", mApiHeader.getPublicApiHeader().getApiKey())
                    .method(originalRequest.method(), originalRequest.body())
                    .build();

            response = chain.proceed(compressedRequest);
        }

//        long t1 = System.nanoTime();
//        Log.e("Header",String.format("Sending request %s on %s%n%s",
//                originalRequest.url(), chain.connection(), originalRequest.headers()) );
//
//        long t2 = System.nanoTime();
//        Log.e("Header",String.format("Received data for %s in %.1fms%n%s",
//                data.request().url(), (t2 - t1) / 1e6d, data.headers()));

        return response;
    }
}