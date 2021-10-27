package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.swipecrafts.library.imageSlider.FeaturesImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 5/21/2018.
 */
@Dao
public abstract class FeaturesImageDAO implements BaseDAO<FeaturesImage> {

    @Query("SELECT COUNT(*) FROM features_images")
    public abstract int countStatic();

    @Query("SELECT COUNT(*) FROM features_images")
    public abstract LiveData<Integer> count();


    @Query("SELECT * FROM features_images")
    public abstract LiveData<List<FeaturesImage>> getFeatureImages();

    @Query("SELECT * FROM features_images WHERE id = :id")
    public abstract LiveData<FeaturesImage> getFeatureImageById(long id);

    @Query("SELECT * FROM features_images ORDER BY id DESC LIMIT 1")
    public abstract LiveData<FeaturesImage> getLastItem();

    @Transaction
    public List<Long> deleteAndInsert(List<FeaturesImage> items) {
        if (items == null || items.isEmpty()) return new ArrayList<>();
        deleteAll();
        return insertAll(items);
    }

    @Query("DELETE FROM features_images")
    public abstract void deleteAll();
}
