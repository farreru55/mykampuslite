package com.example.mykampuslite.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mykampuslite.dao.MahasiswaDao;
import com.example.mykampuslite.database.MyKampusDatabase;
import com.example.mykampuslite.model.Mahasiswa;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Repository mahasiswa — kerja database di thread terpisah
public class MahasiswaRepository implements AkademikRepository<Mahasiswa> {

    private final MahasiswaDao mahasiswaDao;
    private final LiveData<List<Mahasiswa>> allMahasiswa;
    private final ExecutorService executorService;

    public MahasiswaRepository(Application application) {
        MyKampusDatabase db = MyKampusDatabase.getDatabase(application);
        mahasiswaDao = db.mahasiswaDao();
        allMahasiswa = mahasiswaDao.getAll();
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void insert(Mahasiswa mahasiswa) {
        executorService.execute(() -> mahasiswaDao.insert(mahasiswa));
    }

    @Override
    public void update(Mahasiswa mahasiswa) {
        executorService.execute(() -> mahasiswaDao.update(mahasiswa));
    }

    @Override
    public void delete(Mahasiswa mahasiswa) {
        executorService.execute(() -> mahasiswaDao.delete(mahasiswa));
    }

    @Override
    public LiveData<List<Mahasiswa>> getAll() {
        return allMahasiswa;
    }
}
