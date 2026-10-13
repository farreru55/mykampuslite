package com.example.mykampuslite.model;

// Orang: punya nama dan email
public abstract class Person extends BaseEntity {

    private String nama;
    private String email;

    public Person() {
        super();
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    // Nama wajib, minimal 3 huruf
    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong");
        }
        if (nama.trim().length() < 3) {
            throw new IllegalArgumentException("Nama minimal 3 karakter");
        }
        this.nama = nama.trim();
    }

    // Email wajib dan harus ada @ serta .
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email tidak boleh kosong");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Format email tidak valid");
        }
        this.email = email.trim();
    }
}
