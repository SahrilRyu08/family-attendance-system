CREATE DATABASE IF NOT EXISTS reuni_keluarga;
USE reuni_keluarga;

-- Tabel Events
CREATE TABLE events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nama_event VARCHAR(255) NOT NULL,
    tanggal DATETIME NOT NULL,
    lokasi VARCHAR(255) NOT NULL,
    deskripsi TEXT,
    status ENUM('BERLANGSUNG', 'SELESAI', 'AKAN_DATANG') DEFAULT 'AKAN_DATANG',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabel Keluarga
CREATE TABLE keluarga (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  kode_keluarga VARCHAR(50) UNIQUE NOT NULL,
  nama_keluarga VARCHAR(255) NOT NULL,
  alamat TEXT,
  no_telepon VARCHAR(20),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabel Peserta
CREATE TABLE peserta (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 keluarga_id BIGINT,
 nama_lengkap VARCHAR(255) NOT NULL,
 kategori ENUM('PESERTA_MUDA', 'ORANG_TUA', 'LANSIA') DEFAULT 'PESERTA_MUDA',
 qr_code VARCHAR(255) UNIQUE,
 status_aktif BOOLEAN DEFAULT true,
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 FOREIGN KEY (keluarga_id) REFERENCES keluarga(id) ON DELETE SET NULL
);

-- Tabel Absensi
CREATE TABLE absensi (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
event_id BIGINT NOT NULL,
peserta_id BIGINT NOT NULL,
waktu_kehadiran DATETIME NOT NULL,
metode_absen ENUM('QR', 'MANUAL') NOT NULL,
keterangan VARCHAR(255),
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
FOREIGN KEY (peserta_id) REFERENCES peserta(id) ON DELETE CASCADE,
UNIQUE KEY unique_event_peserta (event_id, peserta_id)
);

-- Tabel Admin
CREATE TABLE admin (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       nama_lengkap VARCHAR(255) NOT NULL,
                       role ENUM('ADMIN', 'PANITIA') DEFAULT 'PANITIA',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default event
INSERT INTO events (nama_event, tanggal, lokasi, deskripsi, status) VALUES
    ('Reuni Keluarga Besar 2025', '2025-07-12 09:00:00', 'Aula Serbaguna', 'Reuni keluarga besar tahunan', 'BERLANGSUNG');

-- Insert default admin (password: admin123)
INSERT INTO admin (username, password, nama_lengkap, role) VALUES
    ('admin', '$2a$10$N5XKj6cX5Z9Qh8YKj6cX5Z9Qh8YKj6cX5Z9Qh8YKj6cX5Z9Qh8YKj', 'Administrator', 'ADMIN');