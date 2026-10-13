package com.example.mykampuslite.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mykampuslite.model.Dosen;

import java.util.List;

// Akses database untuk dosen
@Dao
public interface DosenDao {

    @Insert
    void insert(Dosen dosen);

    @Update
    void update(Dosen dosen);

    @Delete
    void delete(Dosen dosen);

    @Query("SELECT * FROM dosen ORDER BY id DESC")
    LiveData<List<Dosen>> getAll();

    @Query("DELETE FROM dosen")
    void deleteAll();
}
