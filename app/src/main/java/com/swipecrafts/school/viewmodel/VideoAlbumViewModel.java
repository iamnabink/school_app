package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.swipecrafts.school.data.DataManager;
import com.swipecrafts.school.data.model.others.VMResponse;
import com.swipecrafts.school.data.remote.ApiListener;
import com.swipecrafts.school.ui.dashboard.videos.VideoAlbum;
import com.swipecrafts.school.utils.Constants;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/26/2018.
 */

public class VideoAlbumViewModel extends ViewModel {
    private DataManager dataManager;

    public VideoAlbumViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public MutableLiveData<VMResponse<List<VideoAlbum>>> refreshVideoAlbums() {
        MutableLiveData<VMResponse<List<VideoAlbum>>> data = new MutableLiveData<>();

        this.dataManager.getApiRepo().getVideoAlbums(new ApiListener<List<VideoAlbum>>() {
            @Override
            public void onSuccess(List<VideoAlbum> response) {
                data.postValue(new VMResponse<>(true, "Success", response));
            }

            @Override
            public void onFailed(String message, int code) {
                message = (code == 1001 ? dataManager.isNetworkConntected() ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : message);

                data.postValue(new VMResponse<>(false, message, null));
            }
        });

        return data;
    }
}
