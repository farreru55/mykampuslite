package com.example.mykampuslite.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mykampuslite.model.Dosen;
import com.example.mykampuslite.repository.AkademikRepository;
import com.example.mykampuslite.repository.DosenRepository;

import java.util.List;

// ViewModel dosen — data tetap aman saat layar diputar
public class DosenViewModel extends AndroidViewModel {

    private final AkademikRepository<Dosen> repository;
    private final LiveData<List<Dosen>> allDosen;

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();

    public DosenViewModel(@NonNull Application application) {
        super(application);
        repository = new DosenRepository(application);
        allDosen = repository.getAll();
    }

    public LiveData<List<Dosen>> getAllDosen() {
        return allDosen;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getSaveSuccess() {
        return saveSuccess;
    }

    // Tambah dosen (validasi lewat setter model)
    public void insert(String nama, String email, String nidn, String mataKuliahDiampu) {
        try {
            Dosen dosen = new Dosen();
            dosen.setNama(nama);
            dosen.setEmail(email);
            dosen.setNidn(nidn);
            dosen.setMataKuliahDiampu(mataKuliahDiampu);

            repository.insert(dosen);
            saveSuccess.setValue(true);
        } catch (IllegalArgumentException e) {
            errorMessage.setValue(e.getMessage());
        }
    }

    // Ubah data — buat objek baru supaya list ikut ter-update
    public void update(Dosen existing, String nama, String email, String nidn, String mataKuliahDiampu) {
        try {
            Dosen updated = new Dosen();
            updated.setId(existing.getId());
            updated.setCreatedAt(existing.getCreatedAt());
            updated.setNama(nama);
            updated.setEmail(email);
            updated.setNidn(nidn);
            updated.setMataKuliahDiampu(mataKuliahDiampu);
            updated.refreshUpdatedAt();

            repository.update(updated);
            saveSuccess.setValue(true);
        } catch (IllegalArgumentException e) {
            errorMessage.setValue(e.getMessage());
        }
    }

    public void delete(Dosen dosen) {
        repository.delete(dosen);
    }

    public void clearError() {
        errorMessage.setValue(null);
    }

    public void clearSaveSuccess() {
        saveSuccess.setValue(null);
    }
}
