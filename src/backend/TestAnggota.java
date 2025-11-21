package backend;

public class TestAnggota {
    public static void main(String[] args) {
        // Tambah data baru
        Anggota a1 = new Anggota("John Doe", "Jl. Contoh No. 123", "081234567890");
        a1.save();

        // Update data
        a1.setAlamat("Jl. Update No. 456");
        a1.save();

        // Tampilkan semua
        System.out.println("\nSeluruh Data Anggota:");
        for (Anggota a : Anggota.getAll()) {
            System.out.println(a.getIdanggota() + " - " + a.getNama() + " - " + a.getAlamat() + " - " + a.getTelepon());
        }

        // Cari data
        System.out.println("\nHasil pencarian 'john':");
        for (Anggota a : Anggota.search("john")) {
            System.out.println(a.getNama() + " - " + a.getTelepon());
        }

        // Hapus data
        // a1.delete(); // Uncomment untuk menguji penghapusan
    }
}