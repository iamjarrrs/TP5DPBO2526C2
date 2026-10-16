# TP5 DPBO 2025/2026 - Aplikasi CRUD Mahasiswa Java Swing & MySQL
--- 
## Janji

Saya Afit Fajar Rianto dengan NIM 2501826 mengerjakan Tugas Praktikum 4 pada Mata Kuliah Desain dan Pemrograman Berorientasi Objek (DPBO) untuk keberkahan-Nya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

---
# Struktur File
```text
TP5DPBO2526C2/
│
├── .git/
├── Database/
│   └── db_akademik.sql
│
├── Dokumentasi/
│   ├── crud_insert.png
│   ├── error_nim_huruf.png
│   ├── error_nama_angka.png
│   ├── error_field_kosong.png
│   └── error_duplicate_nim.png
│
├── Source Code/
│   └── src/
│       ├── Database.java
│       ├── Person.java
│       ├── PeopleMenu.java
│       └── PeopleMenu.form
│
├── lib/
│   └── mysql-connector-j-26.7.0.jar
│
└── README.md
```
---
#  Deskripsi Program & Arsitektur
Aplikasi Desktop berbasis **Java Swing** yang terintegrasi langsung dengan database **MySQL/MariaDB** untuk mengelola data mahasiswa secara lengkap (`Create`, `Read`, `Update`, `Delete`), dilengkapi dengan validasi input form dan error handling yang komprehensif.
Aplikasi ini dirancang menggunakan arsitektur modular sederhana dengan komponen GUI dari Java Swing (`JFrame`, `JTable`, `JTextField`, `JRadioButton`) yang terhubung ke database relasional.

## Struktur Database (`db_akademik`)
Tabel utama yang digunakan adalah `mahasiswa` dengan struktur kolom sebagai berikut:

1. `nim` (VARCHAR, Primary Key)
2. `nama` (VARCHAR)
3. `angkatan` (INT)
4. `jenis_kelamin` (ENUM/VARCHAR)
   
## Komponen Utama Kelas Java
1. `PeopleMenu.java`: Kelas utama berbasis `JFrame` yang mengatur antarmuka pengguna (GUI), event handling (tombol Add/Update, Cancel, Delete, dan klik baris tabel), serta logika validasi form.
2. `Database.java`: Kelas pengendali untuk mengatur koneksi JDBC (`DriverManager`) serta mengeksekusi query SQL (`SELECT`, `INSERT`, `UPDATE`, `DELETE`).
3. `Person.java`: Model objek data mahasiswa untuk merepresentasikan entitas data.

---

# Penjelasan Alur Program (_Workflow_)
## 1. Inisialisasi & koneksi:
Saat program dijalankan, `PeopleMenu` memanggil konstruktor yang menginisialisasi koneksi ke database melalui kelas `Database`. Data mahasiswa langsung dimuat dari database dan ditampilkan ke dalam `JTable` melalui method `setTable()`.

## 2. Operasi _Create_(Tambah Data):
1. Pengguna mengisi form (`NIM`, `Nama`, `Angkatan`, dan `Jenis Kelamin`).
2. Sistem memvalidasi apakah ada field yang kosong, memastikan NIM hanya berupa angka, dan Nama hanya berupa huruf/spasi.
3. Jika validasi lolos, query `INSERT INTO` dieksekusi ke database.
   
## 3. Operasi _read_ (Tampilkan Data):
1. Data di dalam database dirender ulang ke `JTable` secara dinamis setiap kali terjadi perubahan data (tambah, ubah, atau hapus).

## 4. Operasi _Update_ (Ubah Data):
1. Pengguna mengklik salah satu baris data pada tabel (`peopleTable`). Data akan otomatis terisi kembali ke form input di atasnya, dan tombol berubah menjadi "Update".
2. Setelah melakukan perubahan dan menekan tombol, sistem memvalidasi input sebelum menjalankan query `UPDATE`.
   
## 5. Operasi _Delete_(Hapus Data):
1. Ketika baris dipilih, tombol "Delete" akan muncul.
2. Saat diklik, sistem akan memunculkan dialog konfirmasi. Jika disetujui, query `DELETE` dijalankan untuk menghapus data berdasarkan NIM.
---

# Dokumentasi Validasi & Error Handling
Aplikasi ini menangani berbagai skenario kesalahan untuk memastikan integritas data:
1. **Peringatan Field Kosong**: Mencegah penyimpanan data jika salah satu atau seluruh kolom form kosong.
2. **Validasi NIM (Angka)**: Menampilkan pesan _Format Error_ jika pengguna memasukkan huruf atau simbol pada kolom NIM.
3. **Validasi Nama (Huruf)**: Menampilkan pesan _Format Error_ jika pengguna memasukkan angka pada kolom Nama.
4. Validasi NIM Duplikat: Menangkap Exception database (`Duplicate entry`) apabila NIM yang dimasukkan sudah terdaftar di database.
5. **Validasi Angkatan**: Memastikan input angkatan berupa angka yang valid.

---

# Dokumentasi Program
Penjelasan dan Demo Program dapat diakses melalui tautan di bawah ini:

[![YouTube Video Demo](https://img.shields.io/badge/Tonton%20Demo%20YouTube-FF0000?style=for-the-badge&logo=youtube&logoColor=white)](https://youtu.be/lzSAG2RNLLU)
