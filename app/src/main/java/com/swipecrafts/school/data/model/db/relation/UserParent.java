package com.swipecrafts.school.data.model.db.relation;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import com.swipecrafts.school.data.model.db.Book;
import com.swipecrafts.school.data.model.db.Parent;
import com.swipecrafts.school.data.model.db.User;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Madhusudan Sapkota on 3/21/2018.
 */

@Entity(tableName = "user_parent", primaryKeys = { "user_id", "parent_id"})
public class UserParent {

    @ColumnInfo(name = "user_id")
    @NonNull
    @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id", onDelete = CASCADE)
    private long userId;

    @ColumnInfo(name = "parent_id")
    @NonNull
    @ForeignKey(entity = Parent.class, parentColumns = "id", childColumns = "parent_id", onDelete = CASCADE)
    private long parentId;

    public UserParent(@NonNull long userId, @NonNull long parentId) {
        this.userId = userId;
        this.parentId = parentId;
    }

    @NonNull
    public long getUserId() {
        return userId;
    }

    public void setUserId(@NonNull long userId) {
        this.userId = userId;
    }

    @NonNull
    public long getParentId() {
        return parentId;
    }

    public void setParentId(@NonNull long parentId) {
        this.parentId = parentId;
    }
}
