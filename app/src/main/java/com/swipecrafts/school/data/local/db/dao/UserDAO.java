package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.swipecrafts.school.data.model.db.User;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 2/20/2018.
 */

@Dao
public abstract class UserDAO implements BaseDAO<User> {

    @Query("SELECT COUNT(*) FROM users")
    public abstract LiveData<Integer> count();

    @Query("SELECT COUNT(*) FROM users")
    public abstract int countStatic();

    @Query("UPDATE users set active_user = 0 where active_user = 1")
    public abstract int resetActiveUser();

    @Query("UPDATE users set active_user = 1 where id =:id")
    public abstract int setAsActiveUser(long id);

    @Query("SELECT * from users")
    public abstract LiveData<List<User>> getUserList();

    @Query("SELECT * from users")
    public abstract List<User> getStaticUserList();

    @Query("SELECT * FROM users WHERE name LIKE :username")
    public abstract User getStaticUserByUserName(String username);

    @Query("SELECT * FROM users WHERE name LIKE :username")
    public abstract LiveData<User> getUserByUserName(String username);

    @Query("SELECT * FROM users WHERE active_user = 1 LIMIT 1")
    public abstract LiveData<User> getActiveUser();

    @Query("SELECT * FROM users WHERE id = :id")
    public abstract LiveData<User> getUserById(String id);

    @Query("SELECT * FROM users ORDER BY id ASC LIMIT 1")
    public abstract User getFirstUser();

    @Transaction
    public Long insertAsActive(User item){
        resetActiveUser();
        item.isActive(1);
        return insert(item);
    }

    @Transaction
    public User makeNewActiveUser(){
        deleteActiveUser();
        User activeUser = getFirstUser();
        if (activeUser == null) return null;

        activeUser.isActive(1);
        insert(activeUser);
        return activeUser;
    }

    @Query("DELETE FROM users WHERE active_user = 1")
    public abstract void deleteActiveUser();

    @Query("DELETE FROM users")
    public abstract void deleteAll();
}
