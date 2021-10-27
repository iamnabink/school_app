package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.swipecrafts.school.data.model.db.Parent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/21/2018.
 */

@Dao
public abstract class ParentDAO implements BaseDAO<Parent> {
    @Query("SELECT COUNT(*) FROM parents")
    public abstract LiveData<Integer> count();

    @Query("SELECT COUNT(*) FROM parents")
    public abstract int countStatic();

    @Query("SELECT * from parents")
    public abstract LiveData<List<Parent>> getParentList();

    @Query("SELECT * FROM parents WHERE id = :id")
    public abstract LiveData<Parent> getParentById(long id);

    @Transaction
    public List<Long> deleteAndInsert(List<Parent> parents){
        if (parents == null || parents.isEmpty()) return new ArrayList<>();

        for (Parent parent : parents) {
            delete(parent);
        }
        return insertAll(parents);
    }

    @Query("DELETE FROM parents")
    public abstract void deleteAll();
}
