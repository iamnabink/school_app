package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.swipecrafts.school.data.model.db.Book;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/20/2018.
 */

@Dao
public abstract class BookDAO implements BaseDAO<Book>{
    @Query("SELECT COUNT(*) FROM books")
    public abstract LiveData<Integer> count();

    @Query("SELECT COUNT(*) FROM books")
    public abstract int countStatic();

    @Query("SELECT * from books")
    public abstract LiveData<List<Book>> getAllBooks();

    @Query("SELECT * from books")
    public abstract List<Book> getAllStaticBooks();

    @Query("SELECT * FROM books WHERE class_id=:gradeId")
    public abstract LiveData<List<Book>> getBooksByGrade(long gradeId);

    @Query("SELECT * FROM books WHERE subject_id=:subjectId")
    public abstract LiveData<List<Book>> getBooksBySubject(long subjectId);

    @Query("SELECT * FROM books WHERE id = :id")
    public abstract LiveData<Book> getBookById(long id);

    @Query("DELETE FROM books")
    public abstract void deleteAll();
}
