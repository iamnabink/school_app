package com.swipecrafts.school.data.model.db.relation;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import com.swipecrafts.school.data.model.db.Book;
import com.swipecrafts.school.data.model.db.User;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Madhusudan Sapkota on 3/20/2018.
 */

@Entity(tableName = "user_book",  primaryKeys = {"user_id", "book_id"})
public class UserBook {

    @ColumnInfo(name = "user_id")
    @NonNull
    @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id", onDelete = CASCADE)
    private long userId;

    @ColumnInfo(name = "book_id")
    @NonNull
    @ForeignKey(entity = Book.class, parentColumns = "id", childColumns = "book_id", onDelete = CASCADE)
    private long bookId;

    public UserBook(@NonNull long userId, @NonNull long bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    @NonNull
    public long getUserId() {
        return userId;
    }

    public void setUserId(@NonNull long userId) {
        this.userId = userId;
    }

    @NonNull
    public long getBookId() {
        return bookId;
    }

    public void setBookId(@NonNull long bookId) {
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return " {\n" +
                "        \"user_id\": "+ userId +",\n" +
                "        \"book_id\": "+ bookId +" ,\n" +
                "    }\n";
    }
}
