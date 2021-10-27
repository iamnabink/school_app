package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.swipecrafts.school.data.model.db.Chapter;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/5/2018.
 */

@Dao
public abstract class ChapterDAO implements BaseDAO<Chapter>{

    @Query("SELECT COUNT(*) FROM chapters")
    public abstract LiveData<Integer> count();

    @Query("SELECT COUNT(*) FROM chapters WHERE subject_id=:subjectId")
    public abstract LiveData<Integer> countBySubject(long subjectId);

    @Query("SELECT COUNT(*) FROM chapters WHERE subject_id=:subjectId")
    public abstract int countStaticBySubject(long subjectId);

    @Query("SELECT * from chapters")
    public abstract LiveData<List<Chapter>> getAllChapter();

    @Query("SELECT * FROM chapters WHERE subject_id=:subjectId")
    public abstract LiveData<List<Chapter>> getChaptersBySubject(long subjectId);

    @Query("SELECT * FROM chapters WHERE id = :id")
    public abstract LiveData<Chapter> getChapterById(long id);

    @Query("DELETE FROM chapters")
    public abstract void deleteAll();
}
