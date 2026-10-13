package com.example.mykampuslite.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mykampuslite.model.Mahasiswa;
import com.example.mykampuslite.repository.AkademikRepository;
import com.example.mykampuslite.repository.MahasiswaRepository;

import java.util.List;

// ViewModel mahasiswa — data tetap aman saat layar diputar
public class MahasiswaViewModel extends AndroidViewModel {

    private final AkademikRepository<Mahasiswa> repository;
    private final LiveData<List<Mahasiswa>> allMahasiswa;

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();

    public MahasiswaViewModel(@NonNull Application application) {
        super(application);
        repository = new MahasiswaRepository(application);
        allMahasiswa = repository.getAll();
    }

    public LiveData<List<Mahasiswa>> getAllMahasiswa() {
        return allMahasiswa;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getSaveSuccess() {
        return saveSuccess;
    }

    // Tambah mahasiswa (validasi lewat setter model)
    public void insert(String nama, String email, String nim, String jurusan, String ipkStr) {
        try {
            Mahasiswa mahasiswa = new Mahasiswa();
            mahasiswa.setNama(nama);
            mahasiswa.setEmail(email);
            mahasiswa.setNim(nim);
            mahasiswa.setJurusan(jurusan);

            double ipk;
            try {
                ipk = Double.parseDouble(ipkStr);
            } catch (NumberFormatException e) {
                errorMessage.setValue("IPK harus berupa angka");
                return;
            }
            mahasiswa.setIpk(ipk);

            repository.insert(mahasiswa);
            saveSuccess.setValue(true);
        } catch (IllegalArgumentException e) {
            errorMessage.setValue(e.getMessage());
        }
    }

    // Ubah data — buat objek baru supaya list ikut ter-update
    public void update(Mahasiswa existing, String nama, String email, String nim, String jurusan, String ipkStr) {
        try {
            double ipk;
            try {
                ipk = Double.parseDouble(ipkStr.trim());
            } catch (NumberFormatException e) {
                errorMessage.setValue("IPK harus berupa angka");
                return;
            }

            Mahasiswa updated = new Mahasiswa();
            updated.setId(existing.getId());
            updated.setCreatedAt(existing.getCreatedAt());
            updated.setNama(nama);
            updated.setEmail(email);
            updated.setNim(nim);
            updated.setJurusan(jurusan);
            updated.setIpk(ipk);
            updated.refreshUpdatedAt();

            repository.update(updated);
            saveSuccess.setValue(true);
        } catch (IllegalArgumentException e) {
            errorMessage.setValue(e.getMessage());
        }
    }

    public void delete(Mahasiswa mahasiswa) {
        repository.delete(mahasiswa);
    }

    public void clearError() {
        errorMessage.setValue(null);
    }

    public void clearSaveSuccess() {
        saveSuccess.setValue(null);
    }
}
