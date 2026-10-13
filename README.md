# MyKampus Lite

Aplikasi Android offline untuk manajemen data akademik Prodi menggunakan **4 Pilar OOP** dalam arsitektur **MVVM + Room**.

**Mata Kuliah:** Pemrograman Mobile Lanjut - Java Intermediate  
**Topik:** Implementasi 4 Pilar OOP dalam MVVM + Room

---

## Fitur

- **Manajemen Data Mahasiswa** - CRUD (Create, Read, Update, Delete)
- **Manajemen Data Dosen** - CRUD (Create, Read, Update, Delete)
- **Validasi Input** - Validasi di level Model (setter) dan ditampilkan via ViewModel
- **Status Akademik Otomatis** - Polymorphism menghasilkan status berdasarkan IPK/jumlah MK
- **Tab Navigation** - ViewPager2 dengan TabLayout
- **Data Persisten** - Room Database lokal
- **Tahan Rotasi** - ViewModel menyimpan state

---

## Arsitektur

```
UI (Activity/Fragment) → ViewModel → Repository (Interface) → DAO → Room Database
```

### 4 Pilar OOP

| Pilar | Implementasi |
|-------|-------------|
| **Inheritance** | BaseEntity → Person → Mahasiswa/Dosen (3 level hierarki) |
| **Encapsulation** | Semua field private + validasi di setter |
| **Polymorphism** | Interface `EvaluasiAkademik` + `getRole()` + multi-type adapter |
| **Abstraction** | Interface `AkademikRepository` + Dependency Inversion |

### Bonus

- `equals()` & `hashCode()` di Mahasiswa dan Dosen untuk DiffUtil
- Menggunakan `ListAdapter` (bukan `RecyclerView.Adapter` biasa)

---

## Struktur Package

```
com.example.mykampuslite/
├── model/
│   ├── BaseEntity.java          (abstract - level 1)
│   ├── Person.java              (abstract - level 2)
│   ├── EvaluasiAkademik.java    (interface polymorphism)
│   ├── Mahasiswa.java           (Room @Entity - level 3)
│   └── Dosen.java               (Room @Entity - level 3)
├── dao/
│   ├── MahasiswaDao.java
│   └── DosenDao.java
├── database/
│   └── MyKampusDatabase.java
├── repository/
│   ├── AkademikRepository.java  (interface abstraction)
│   ├── MahasiswaRepository.java
│   └── DosenRepository.java
├── viewmodel/
│   ├── MahasiswaViewModel.java
│   └── DosenViewModel.java
├── adapter/
│   └── AkademikAdapter.java     (multi-type ListAdapter)
├── ui/
│   ├── MahasiswaFragment.java
│   ├── DosenFragment.java
│   └── ViewPagerAdapter.java
└── MainActivity.java
```

---

## Pertanyaan Refleksi

### 1. Apa yang terjadi jika kalian menghapus constructor kosong di Entity Room?

Jika constructor kosong (no-arg constructor) dihapus dari Entity Room, maka **aplikasi akan gagal compile** dengan error seperti:

```
error: Entities and POJOs must have a usable public constructor.
```

Room membutuhkan no-arg constructor untuk **membuat instance objek** saat membaca data dari database (query). Room menggunakan **reflection** untuk memanggil constructor kosong, kemudian mengisi field-field melalui setter. Tanpa constructor kosong, Room tidak bisa menginstansiasi objek Entity, sehingga tidak bisa memetakan row dari SQLite ke objek Java.

Alternatifnya, Room juga bisa menggunakan constructor yang parameter-nya cocok dengan semua kolom, tapi minimal satu constructor yang valid harus tersedia.

### 2. Mengapa ViewModel tidak boleh meng-import `android.widget.TextView`? Hubungkan dengan prinsip Abstraction.

ViewModel **tidak boleh** meng-import `android.widget.TextView` atau class Android UI lainnya karena hal ini melanggar **prinsip Abstraction (dan Separation of Concerns)**:

- **ViewModel bertindak sebagai abstraksi** antara data layer dan UI layer. Ia hanya perlu tahu "apa" data yang akan ditampilkan, bukan "bagaimana" data tersebut ditampilkan.
- Jika ViewModel mengakses `TextView`, ia menjadi **tightly coupled** dengan UI layer. Ini berarti ViewModel harus tahu detail implementasi UI (apakah pakai TextView, EditText, atau Compose).
- ViewModel **berumur lebih lama** dari Activity/Fragment (survives configuration change). Jika ViewModel memegang referensi ke View, maka saat Activity di-destroy (misalnya rotasi layar), View yang lama sudah tidak valid → terjadi **memory leak** dan potentially **crash**.
- Dengan prinsip Abstraction, ViewModel hanya mengekspos data via `LiveData` (abstraksi), dan UI (Fragment/Activity) yang memutuskan bagaimana menampilkannya. Ini membuat ViewModel **reusable** dan **testable** tanpa memerlukan Android framework.

### 3. Berikan 1 contoh nyata di studi kasus ini di mana Polymorphism menghemat baris kode kalian.

Contoh nyata: **`AkademikAdapter` (single adapter for multi-type)**.

Tanpa polymorphism, kita perlu membuat **2 adapter terpisah**: `MahasiswaAdapter` dan `DosenAdapter`, masing-masing dengan logic binding yang mirip (menampilkan nama, email, status, tombol edit/delete). Ini berarti kode duplikat ±100 baris.

Dengan polymorphism, cukup **1 `AkademikAdapter`** yang beroperasi pada tipe `Person` (parent class). Di dalam `onBindViewHolder()`:

```java
// Polymorphism: getRole() dipanggil secara polimorfik
holder.tvRole.setText(person.getRole());  // otomatis "Mahasiswa" atau "Dosen"
holder.tvNama.setText(person.getNama());  // dari Person (parent)

// Polymorphism: getStatusAkademik() dari interface EvaluasiAkademik
String status = ((EvaluasiAkademik) person).getStatusAkademik();
// otomatis "Cumlaude"/"Peringatan" untuk Mahasiswa, "Beban Lebih"/"Normal" untuk Dosen
```

Method `getRole()` dan `getStatusAkademik()` dipanggil pada tipe `Person`, tapi yang dijalankan adalah implementasi dari masing-masing subclass (`Mahasiswa` atau `Dosen`). Dengan satu adapter ini, kita menghemat **~100 baris kode** yang akan jadi duplikat jika menggunakan 2 adapter terpisah.

---

## Tech Stack

- **Language:** Java
- **Architecture:** MVVM
- **Database:** Room (SQLite)
- **UI Components:** Material Design 3, ViewPager2, TabLayout, RecyclerView
- **Lifecycle:** ViewModel, LiveData

---

## Build & Run

1. Clone repository
2. Buka di Android Studio
3. Sync Gradle
4. Run di emulator/device (Min SDK 24)
