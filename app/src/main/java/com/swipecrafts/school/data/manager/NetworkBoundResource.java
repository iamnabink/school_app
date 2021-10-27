package com.swipecrafts.school.data.manager;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.swipecrafts.school.data.model.api.ApiResponse;

/**
 * Created by Madhusudan Sapkota on 5/18/2018.
 */
// ResultType: Type for the Resource data
// RequestType: Type for the API data
public abstract class NetworkBoundResource<ResultType, RequestType> {
    private final int NOT_FOUND = 404;
    private final int SUCCESS = 200;

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    public NetworkBoundResource() {
        result.setValue(Resource.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (!validateLocalData(data)){
                result.setValue(createErrorResponse(NOT_FOUND, "data validation failed!", data));
            }else if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            }else {
                result.addSource(dbSource, newData -> result.setValue(createSuccessResponse(newData)));
            }
        });
    }

    @MainThread
    protected  Resource<ResultType> createSuccessResponse(@NonNull ResultType item){
        return Resource.success(item);
    }

    @MainThread
    protected  Resource<ResultType> createErrorResponse(int code, String message, @Nullable ResultType item){
        return Resource.error(code, message, item);
    }

    @MainThread
    protected  Resource<ResultType> createLoadingResponse(@Nullable ResultType item){
        return Resource.loading(item);
    }

    // Called only for the api requests
    @MainThread
    protected boolean shouldReturnLocalData(){
        return true;
    }

    @MainThread
    protected boolean validateLocalData(ResultType item){
        return true;
    }

    // Called to save the result of the API data into the database
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    // Called with the data in the database to decide whether it should be
    // fetched from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    // Called to get the cached data from the database
    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    // Called to create the API call.
    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    @MainThread
    protected void onFetchFailed(MediatorLiveData<Resource<ResultType>> result) {
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        LiveData<ApiResponse<RequestType>> apiResponse = createCall();
        // we re-attach dbSource as a new source,
        // it will dispatch its latest value quickly
        result.addSource(dbSource, newData -> result.setValue(createLoadingResponse(newData)));
        result.addSource(apiResponse, response -> {
            if (response == null || response.isLoading()) return;
            result.removeSource(apiResponse);
            result.removeSource(dbSource);
            //noinspection ConstantConditions
            if (response.isSuccessful()) {
                saveResultAndReInit(response);
            } else {
                if (shouldReturnLocalData()){
                    result.addSource(dbSource,
                            newData -> result.setValue(
                                    createErrorResponse(response.status, response.message, newData)));
                }else {
                    result.setValue(createErrorResponse(response.status, response.message, null));
                }
                onFetchFailed(result);
            }
        });
    }

    @MainThread
    private void saveResultAndReInit(ApiResponse<RequestType> response) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                saveCallResult(response.data);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // we specially request a new live data,
                // otherwise we will get immediately last cached value,
                // which may not be updated with latest results received from network.
                result.addSource(loadFromDb(),
                        newData -> result.setValue(createSuccessResponse(newData)));
            }
        }.execute();
    }

    // returns a LiveLocationData that represents the resource, implemented
    // in the base class.
    public final LiveData<Resource<ResultType>> getAsLiveData() {
        return result;
    }
}
