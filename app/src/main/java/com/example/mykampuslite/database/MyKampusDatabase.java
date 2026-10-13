package com.example.mykampuslite.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mykampuslite.dao.DosenDao;
import com.example.mykampuslite.dao.MahasiswaDao;
import com.example.mykampuslite.model.Dosen;
import com.example.mykampuslite.model.Mahasiswa;

// Database Room (hanya satu instance)
@Database(entities = {Mahasiswa.class, Dosen.class}, version = 1, exportSchema = false)
public abstract class MyKampusDatabase extends RoomDatabase {

    private static volatile MyKampusDatabase INSTANCE;

    public abstract MahasiswaDao mahasiswaDao();
    public abstract DosenDao dosenDao();

    public static MyKampusDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyKampusDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MyKampusDatabase.class,
                            "mykampus_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
