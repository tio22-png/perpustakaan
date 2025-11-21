package backend;

public class TestBuku {
    public static void main(String[] args) {
        Kategori novel = new Kategori("Novel", "Buku novel populer");
        novel.save();

        // Test tambah data
        Buku buku1 = new Buku(novel, "Laskar Pelangi", "Bentang Pustaka", "Andrea Hirata");
        buku1.save();

        // Test update
        buku1.setPenerbit("Bentang Pustaka Baru");
        buku1.save();

        // Test tampil semua
        System.out.println("\nDaftar Buku:");
        for (Buku b : Buku.getAll()) {
            System.out.println("ID Buku: " + b.getIdbuku());
            System.out.println("Kategori: " + b.getKategori().getNama());
            System.out.println("Judul: " + b.getJudul());
            System.out.println("Penerbit: " + b.getPenerbit());
            System.out.println("Penulis: " + b.getPenulis());
            System.out.println("-------------------------");
        }

        // Test search
        System.out.println("\nHasil pencarian 'laskar':");
        for (Buku b : Buku.search("laskar")) {
            System.out.println(b.getJudul() + " - " + b.getPenulis());
        }

        // Test delete
        // buku1.delete();
    }
}