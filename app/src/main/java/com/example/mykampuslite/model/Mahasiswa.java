package com.example.mykampuslite.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

// Data mahasiswa (tersimpan di Room)
@Entity(tableName = "mahasiswa")
public class Mahasiswa extends Person implements EvaluasiAkademik {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nim;
    private String jurusan;
    private double ipk;

    public Mahasiswa() {
        super();
    }

    @Override
    public int getId() {
        return id;
    }

    public String getNim() {
        return nim;
    }

    public String getJurusan() {
        return jurusan;
    }

    public double getIpk() {
        return ipk;
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        this.id = id;
    }

    // NIM harus 10 digit angka
    public void setNim(String nim) {
        if (nim == null || !nim.matches("\\d{10}")) {
            throw new IllegalArgumentException("NIM harus 10 digit angka");
        }
        this.nim = nim;
    }

    public void setJurusan(String jurusan) {
        if (jurusan == null || jurusan.trim().isEmpty()) {
            throw new IllegalArgumentException("Jurusan tidak boleh kosong");
        }
        this.jurusan = jurusan.trim();
    }

    // IPK antara 0.0 sampai 4.0
    public void setIpk(double ipk) {
        if (ipk < 0.0 || ipk > 4.0) {
            throw new IllegalArgumentException("IPK harus dalam range 0.0 - 4.0");
        }
        this.ipk = ipk;
    }

    @Override
    public String getRole() {
        return "Mahasiswa";
    }

    // Status berdasarkan IPK
    @Override
    public String getStatusAkademik() {
        if (ipk > 3.5) {
            return "Cumlaude";
        } else if (ipk > 3.0) {
            return "Memuaskan";
        } else if (ipk < 2.0) {
            return "Peringatan";
        } else {
            return "Normal";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mahasiswa that = (Mahasiswa) o;
        return id == that.id &&
                Double.compare(that.ipk, ipk) == 0 &&
                Objects.equals(getNama(), that.getNama()) &&
                Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(nim, that.nim) &&
                Objects.equals(jurusan, that.jurusan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getNama(), getEmail(), nim, jurusan, ipk);
    }
}
