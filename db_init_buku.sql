-- Pastikan tabel buku sudah ada dengan struktur yang benar
CREATE TABLE IF NOT EXISTS buku (
    id_buku SERIAL PRIMARY KEY,
    id_kategori INTEGER REFERENCES kategori(id_kategori),
    judul VARCHAR(100),
    penerbit VARCHAR(100),
    penulis VARCHAR(100)
);

-- Hapus data yang mungkin sudah ada (opsional)
-- TRUNCATE TABLE buku RESTART IDENTITY CASCADE;

-- Insert beberapa data sampel
INSERT INTO buku (id_kategori, judul, penerbit, penulis) VALUES
    -- Novel (asumsikan id_kategori=1 untuk Novel)
    (1, 'Laskar Pelangi', 'Bentang Pustaka', 'Andrea Hirata'),
    (1, 'Bumi Manusia', 'Hasta Mitra', 'Pramoedya Ananta Toer'),
    (1, 'Negeri 5 Menara', 'Gramedia', 'Ahmad Fuadi'),

    -- Teknologi (asumsikan id_kategori=2 untuk Teknologi)
    (2, 'Clean Code', 'Prentice Hall', 'Robert C. Martin'),
    (2, 'Design Patterns', 'Addison-Wesley', 'Erich Gamma'),
    (2, 'Head First Java', 'O''Reilly Media', 'Kathy Sierra'),

    -- Pendidikan (asumsikan id_kategori=3 untuk Pendidikan)
    (3, 'Metodologi Pembelajaran', 'Bumi Aksara', 'Dr. Wina Sanjaya'),
    (3, 'Psikologi Pendidikan', 'Rajawali Pers', 'Dr. Muhibbin Syah'),
    (3, 'Strategi Belajar Mengajar', 'Rineka Cipta', 'Dr. Syaiful Bahri');

-- Verifikasi data
SELECT b.id_buku, k.nama as kategori, b.judul, b.penerbit, b.penulis 
FROM buku b 
JOIN kategori k ON b.id_kategori = k.id_kategori 
ORDER BY b.id_buku;