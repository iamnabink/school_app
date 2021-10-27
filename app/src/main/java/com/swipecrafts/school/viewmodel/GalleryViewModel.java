package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Pair;

import com.swipecrafts.school.data.DataManager;
import com.swipecrafts.school.data.model.db.Album;
import com.swipecrafts.school.data.model.db.ImageAlbumContent;
import com.swipecrafts.school.data.remote.ApiListener;
import com.swipecrafts.school.utils.Constants;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/16/2018.
 */

public class GalleryViewModel extends ViewModel {

    private DataManager dataManager;
    private MutableLiveData result;

    public GalleryViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    public LiveData<Integer> countAlbums() {
        return dataManager.getDbRepo().getAlbumDAO().count();
    }

    public LiveData<List<Album>> getAlbums() {
        return dataManager.getDbRepo().getAlbumDAO().getAllAlbums();
    }

    public LiveData<List<ImageAlbumContent>> getAlbumContents(long albumId) {
        return dataManager.getDbRepo().getAlbumContentsDAO().getAlbumContentByAlbum(albumId);
    }

    public MutableLiveData<Pair<String, Boolean>> refreshAlbums() {
        MutableLiveData<Pair<String, Boolean>> data = new MutableLiveData<>();
        data.postValue(null);

        dataManager.getApiRepo().getImageAlbumList(new ApiListener<List<Album>>() {
            @Override
            public void onSuccess(List<Album> response) {
                new AsyncTask<List<Album>, Void, Void>() {
                    @Override
                    protected Void doInBackground(List<Album>... args) {
                        for (Album album : args[0]) {
                            String url = null;
                            for (ImageAlbumContent content : album.getAlbumContent()) {
                                content.setAlbumId(album.getAlbumId());
                                url = content.getC1Url();
                            }
                            album.setAlbumImgUrl(url);
                            dataManager.getDbRepo().getAlbumDAO().insert(album);
                            dataManager.getDbRepo().getAlbumContentsDAO().insertAll(album.getAlbumContent());
                        }

                        data.postValue(new Pair<>("Success!!", true));
                        return null;
                    }
                }.execute(response);
            }

            @Override
            public void onFailed(String message, int code) {
                message = (code == 1001 ? dataManager.isNetworkConntected() ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : message);

                data.postValue(new Pair<>(message, false));
            }
        });

        return data;
    }
}
