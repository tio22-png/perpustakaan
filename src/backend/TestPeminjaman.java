package backend;

import java.time.LocalDate;

public class TestPeminjaman {
    public static void main(String[] args) {
        // Test: Ambil anggota dan buku yang ada
        Anggota ang = Anggota.getById(1);
        Buku buku = Buku.getById(1);

        if (ang == null || buku == null) {
            System.out.println("Anggota atau Buku tidak ditemukan. Pastikan data sudah ada di database.");
            return;
        }

        // Test tambah peminjaman
        Peminjaman p1 = new Peminjaman(ang, buku, LocalDate.now(), null);
        p1.save();
        System.out.println("Peminjaman baru ditambahkan dengan ID: " + p1.getIdpeminjaman());

        // Test update (set tanggal kembali)
        p1.setTglKembali(LocalDate.now().plusDays(7));
        p1.save();
        System.out.println("Peminjaman diperbarui dengan tanggal kembali: " + p1.getTglKembali());

        // Test tampil semua
        System.out.println("\nDaftar Semua Peminjaman:");
        for (Peminjaman p : Peminjaman.getAll()) {
            System.out.println("ID: " + p.getIdpeminjaman() +
                    " | Anggota: " + p.getAnggota().getNama() +
                    " | Buku: " + p.getBuku().getJudul() +
                    " | Pinjam: " + p.getTglPinjam() +
                    " | Kembali: " + (p.getTglKembali() != null ? p.getTglKembali() : "-"));
        }

        // Test getById
        System.out.println("\nPeminjaman dengan ID " + p1.getIdpeminjaman() + ":");
        Peminjaman pCari = Peminjaman.getById(p1.getIdpeminjaman());
        if (pCari != null) {
            System.out.println("Anggota: " + pCari.getAnggota().getNama() +
                    " | Buku: " + pCari.getBuku().getJudul());
        }

        // Test delete (optional)
        // p1.delete();
        // System.out.println("\nPeminjaman dihapus.");
    }
}