package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.swipecrafts.school.data.model.db.ImageAlbumContent;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/18/2018.
 */

@Dao
public abstract class ImageAlbumContentsDAO implements BaseDAO<ImageAlbumContent> {

    @Query("SELECT COUNT(*) FROM image_album_contents")
    public abstract LiveData<Integer> count();

    @Query("SELECT COUNT(*) FROM image_album_contents")
    public abstract int countStatic();

    @Query("SELECT * FROM image_album_contents WHERE album_id = :id")
    public abstract LiveData<List<ImageAlbumContent>> getAlbumContentByAlbum(long id);

    @Query("SELECT * from image_album_contents")
    public abstract LiveData<List<ImageAlbumContent>> getAllAlbumContents();

    @Query("SELECT * FROM image_album_contents WHERE id = :id")
    public abstract LiveData<ImageAlbumContent> getAlbumContentById(long id);

    @Query("DELETE FROM image_album_contents")
    public abstract void deleteAll();
}
