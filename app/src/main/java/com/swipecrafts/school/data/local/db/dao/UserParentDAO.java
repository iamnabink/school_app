package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.swipecrafts.school.data.model.db.Parent;
import com.swipecrafts.school.data.model.db.relation.UserParent;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/21/2018.
 */

@Dao
public abstract class UserParentDAO implements BaseDAO<UserParent> {

    @Query("SELECT parents.* FROM parents INNER JOIN user_parent ON parents.id = user_parent.parent_id INNER JOIN users ON users.id = user_parent.user_id WHERE users.active_user = 1")
    public abstract LiveData<List<Parent>> getActiveStudentParentDetails();

    @Query("SELECT parents.* FROM parents INNER JOIN user_parent ON parents.id = user_parent.parent_id WHERE user_parent.user_id =:studentId")
    public abstract LiveData<List<Parent>> getParents(long studentId);

    @Query("DELETE FROM user_parent")
    public abstract void deleteAll();
}
