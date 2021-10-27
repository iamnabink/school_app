package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.swipecrafts.school.data.model.db.DashboardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/4/2018.
 */

@Dao
public abstract class MenuDAO implements BaseDAO<DashboardItem>{

    @Query("SELECT COUNT(*) FROM menuItems")
    public abstract LiveData<Integer> count();

    @Query("SELECT COUNT(*) FROM menuItems")
    public abstract int countStatic();

    @Query("SELECT * from menuItems ORDER By menu_order ASC")
    public abstract LiveData<List<DashboardItem>> getAllMenus();

    @Query("SELECT * FROM menuItems WHERE id = :id")
    public abstract LiveData<DashboardItem> getMenuById(String id);

    @Transaction
    public List<Long> deleteAndInsert(List<DashboardItem> items){
        if (items == null || items.isEmpty()) return new ArrayList<>();
        deleteAll();
        return insertAll(items);
    }

    @Query("DELETE FROM menuItems")
    public abstract void deleteAll();
}
