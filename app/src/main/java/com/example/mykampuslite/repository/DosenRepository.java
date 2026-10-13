package com.example.mykampuslite.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mykampuslite.dao.DosenDao;
import com.example.mykampuslite.database.MyKampusDatabase;
import com.example.mykampuslite.model.Dosen;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Repository dosen — kerja database di thread terpisah
public class DosenRepository implements AkademikRepository<Dosen> {

    private final DosenDao dosenDao;
    private final LiveData<List<Dosen>> allDosen;
    private final ExecutorService executorService;

    public DosenRepository(Application application) {
        MyKampusDatabase db = MyKampusDatabase.getDatabase(application);
        dosenDao = db.dosenDao();
        allDosen = dosenDao.getAll();
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void insert(Dosen dosen) {
        executorService.execute(() -> dosenDao.insert(dosen));
    }

    @Override
    public void update(Dosen dosen) {
        executorService.execute(() -> dosenDao.update(dosen));
    }

    @Override
    public void delete(Dosen dosen) {
        executorService.execute(() -> dosenDao.delete(dosen));
    }

    @Override
    public LiveData<List<Dosen>> getAll() {
        return allDosen;
    }
}
