package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.swipecrafts.school.data.model.db.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 2/20/2018.
 */

@Dao
public abstract class NotificationDAO implements BaseDAO<Notification> {

    @Query("SELECT COUNT(*) FROM notifications")
    public abstract LiveData<Integer> count();

    @Query("SELECT COUNT(*) FROM notifications")
    public abstract int countStatic();

    @Query("SELECT * from notifications ORDER By notice_date DESC")
    public abstract LiveData<List<Notification>> getAllNotifications();

    @Query("SELECT * from notifications WHERE is_pinned =:type ORDER By notice_date DESC")
    public abstract LiveData<List<Notification>> getNotificationsByCategory(int type);

    @Query("DELETE FROM notifications WHERE is_pinned ==:category")
    public abstract void deleteByCategory(int category);

    @Query("SELECT * FROM notifications WHERE id = :id")
    public abstract LiveData<Notification> getNotificationById(String id);

    @Transaction
    public List<Long> deleteAndInsert(List<Notification> notifications){
        if (notifications == null || notifications.isEmpty()) return new ArrayList<>();
        deleteAll();
        return insertAll(notifications);
    }

    @Query("SELECT * FROM notifications ORDER BY id DESC LIMIT 1")
    public abstract LiveData<Notification> getLastItem();


    @Query("DELETE FROM notifications")
    public abstract void deleteAll();
}
