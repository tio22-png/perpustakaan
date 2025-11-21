package backend;

public class TestKategori {
    public static void main(String[] args) {
        // Tambah data baru
        Kategori k1 = new Kategori("Novel", "Buku cerita fiksi");
        k1.save();

        // Update data
        k1.setKeterangan("Kumpulan cerita fiksi populer");
        k1.save();

        // Tampilkan semua
        for (Kategori k : Kategori.getAll()) {
            System.out.println(k.getIdkategori() + " - " + k.getNama() + " - " + k.getKeterangan());
        }

        // Cari data
        System.out.println("\nHasil search:");
        for (Kategori k : Kategori.search("novel")) {
            System.out.println(k.getNama());
        }

        // Hapus data
        // k1.delete();
    }
}
