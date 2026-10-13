package com.example.mykampuslite.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mykampuslite.model.Mahasiswa;

import java.util.List;

// Akses database untuk mahasiswa
@Dao
public interface MahasiswaDao {

    @Insert
    void insert(Mahasiswa mahasiswa);

    @Update
    void update(Mahasiswa mahasiswa);

    @Delete
    void delete(Mahasiswa mahasiswa);

    @Query("SELECT * FROM mahasiswa ORDER BY id DESC")
    LiveData<List<Mahasiswa>> getAll();

    @Query("DELETE FROM mahasiswa")
    void deleteAll();
}
