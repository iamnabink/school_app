package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.swipecrafts.school.data.model.db.Album;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/18/2018.
 */

@Dao
public abstract class AlbumDAO implements BaseDAO<Album> {
    @Query("SELECT COUNT(*) FROM albums")
    public abstract LiveData<Integer> count();

    @Query("SELECT COUNT(*) FROM albums")
    public abstract int countStatic();

    @Query("SELECT * from albums")
    public abstract LiveData<List<Album>> getAllAlbums();

    @Query("SELECT * FROM albums WHERE id = :id")
    public abstract LiveData<Album> getAlbumById(long id);

    @Query("DELETE FROM albums")
    public abstract void deleteAll();
}
