package com.example.mykampuslite.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

// Data dosen (tersimpan di Room)
@Entity(tableName = "dosen")
public class Dosen extends Person implements EvaluasiAkademik {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nidn;
    private String mataKuliahDiampu; // dipisah koma, contoh: Algo,OOP

    public Dosen() {
        super();
    }

    @Override
    public int getId() {
        return id;
    }

    public String getNidn() {
        return nidn;
    }

    public String getMataKuliahDiampu() {
        return mataKuliahDiampu;
    }

    // Hitung jumlah mata kuliah
    public int getJumlahMataKuliah() {
        if (mataKuliahDiampu == null || mataKuliahDiampu.trim().isEmpty()) {
            return 0;
        }
        String[] parts = mataKuliahDiampu.split(",");
        int count = 0;
        for (String part : parts) {
            if (!part.trim().isEmpty()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        this.id = id;
    }

    public void setNidn(String nidn) {
        if (nidn == null || nidn.trim().isEmpty()) {
            throw new IllegalArgumentException("NIDN tidak boleh kosong");
        }
        this.nidn = nidn.trim();
    }

    public void setMataKuliahDiampu(String mataKuliahDiampu) {
        if (mataKuliahDiampu == null || mataKuliahDiampu.trim().isEmpty()) {
            throw new IllegalArgumentException("Mata kuliah diampu tidak boleh kosong");
        }
        this.mataKuliahDiampu = mataKuliahDiampu.trim();
    }

    @Override
    public String getRole() {
        return "Dosen";
    }

    // Status berdasarkan jumlah mata kuliah
    @Override
    public String getStatusAkademik() {
        if (getJumlahMataKuliah() > 3) {
            return "Beban Lebih";
        } else {
            return "Normal";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dosen that = (Dosen) o;
        return id == that.id &&
                Objects.equals(getNama(), that.getNama()) &&
                Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(nidn, that.nidn) &&
                Objects.equals(mataKuliahDiampu, that.mataKuliahDiampu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getNama(), getEmail(), nidn, mataKuliahDiampu);
    }
}
