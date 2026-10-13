package com.example.mykampuslite.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// Kelas dasar untuk semua data (id + waktu)
public abstract class BaseEntity {

    private int id;
    private String createdAt;
    private String updatedAt;

    public BaseEntity() {
        String now = getCurrentTimestamp();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public abstract String getRole();

    public int getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Perbarui waktu terakhir diubah
    public void refreshUpdatedAt() {
        this.updatedAt = getCurrentTimestamp();
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
