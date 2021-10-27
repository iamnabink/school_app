package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.swipecrafts.school.data.model.db.Book;
import com.swipecrafts.school.data.model.db.relation.UserBook;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/20/2018.
 */

@Dao
public abstract class UserBookDAO implements BaseDAO<UserBook>{

    @Query("SELECT COUNT(user_book.book_id) id FROM user_book INNER JOIN books ON books.id = user_book.book_id INNER JOIN users ON users.id = user_book.user_id WHERE users.active_user = 1")
    public abstract LiveData<Integer> countBooksByActiveUser();

    @Query("SELECT books.* FROM books INNER JOIN user_book ON books.id = user_book.book_id INNER JOIN users ON users.id = user_book.user_id WHERE users.active_user = 1")
    public abstract LiveData<List<Book>> getBooksByActiveUser();

    @Query("SELECT * FROM user_book")
    public abstract List<UserBook> getAllData();

    @Query("DELETE FROM books")
    public abstract void deleteAll();
}
