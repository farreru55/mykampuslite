package com.example.mykampuslite.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

// Kontrak repository (tambah, ubah, hapus, ambil semua)
public interface AkademikRepository<T> {

    void insert(T item);

    void update(T item);

    void delete(T item);

    LiveData<List<T>> getAll();
}
