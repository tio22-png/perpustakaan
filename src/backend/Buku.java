package backend;

import java.sql.*;
import java.util.ArrayList;

public class Buku {
    private int idbuku;
    private Kategori kategori;
    private String judul;
    private String penerbit;
    private String penulis;

    public Buku() {
    }

    public Buku(Kategori kategori, String judul, String penerbit, String penulis) {
        this.kategori = kategori;
        this.judul = judul;
        this.penerbit = penerbit;
        this.penulis = penulis;
    }

    // --- Getter Setter ---
    public int getIdbuku() {
        return idbuku;
    }

    public void setIdbuku(int idbuku) {
        this.idbuku = idbuku;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    // --- getById() dengan JOIN ---
    public static Buku getById(int id) {
        Buku buku = null;
        Connection conn = DBHelper.connect();
        if (conn == null)
            return null;

        try {
            String sql = "SELECT b.*, k.nama as kategori_nama, k.keterangan as kategori_ket "
                    + "FROM buku b JOIN kategori k ON b.id_kategori = k.id_kategori " + "WHERE b.id_buku = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                buku = new Buku();
                buku.setIdbuku(rs.getInt("id_buku"));

                Kategori kat = new Kategori();
                kat.setIdkategori(rs.getInt("id_kategori"));
                kat.setNama(rs.getString("kategori_nama"));
                kat.setKeterangan(rs.getString("kategori_ket"));
                buku.setKategori(kat);

                buku.setJudul(rs.getString("judul"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setPenulis(rs.getString("penulis"));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error getById: " + e.getMessage());
        }

        return buku;
    }

    // --- getAll() dengan JOIN ---
    public static ArrayList<Buku> getAll() {
        ArrayList<Buku> listBuku = new ArrayList<>();
        Connection conn = DBHelper.connect();
        if (conn == null)
            return listBuku;

        try {
            String sql = "SELECT b.*, k.nama as kategori_nama, k.keterangan as kategori_ket "
                    + "FROM buku b JOIN kategori k ON b.id_kategori = k.id_kategori " + "ORDER BY b.id_buku";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Buku buku = new Buku();
                buku.setIdbuku(rs.getInt("id_buku"));

                Kategori kat = new Kategori();
                kat.setIdkategori(rs.getInt("id_kategori"));
                kat.setNama(rs.getString("kategori_nama"));
                kat.setKeterangan(rs.getString("kategori_ket"));
                buku.setKategori(kat);

                buku.setJudul(rs.getString("judul"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setPenulis(rs.getString("penulis"));
                listBuku.add(buku);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error getAll: " + e.getMessage());
        }

        return listBuku;
    }

    // --- search() dengan JOIN ---
    public static ArrayList<Buku> search(String keyword) {
        ArrayList<Buku> listBuku = new ArrayList<>();
        Connection conn = DBHelper.connect();
        if (conn == null)
            return listBuku;

        try {
            String sql = "SELECT b.*, k.nama as kategori_nama, k.keterangan as kategori_ket "
                    + "FROM buku b JOIN kategori k ON b.id_kategori = k.id_kategori "
                    + "WHERE b.judul ILIKE ? OR b.penerbit ILIKE ? OR b.penulis ILIKE ? " + "OR k.nama ILIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            stmt.setString(3, "%" + keyword + "%");
            stmt.setString(4, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Buku buku = new Buku();
                buku.setIdbuku(rs.getInt("id_buku"));

                Kategori kat = new Kategori();
                kat.setIdkategori(rs.getInt("id_kategori"));
                kat.setNama(rs.getString("kategori_nama"));
                kat.setKeterangan(rs.getString("kategori_ket"));
                buku.setKategori(kat);

                buku.setJudul(rs.getString("judul"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setPenulis(rs.getString("penulis"));
                listBuku.add(buku);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error search: " + e.getMessage());
        }

        return listBuku;
    }

    // --- save() ---
    public void save() {
        Connection conn = DBHelper.connect();
        if (conn == null)
            return;

        try {
            if (getById(this.idbuku) == null) {
                // INSERT
                String sql = "INSERT INTO buku (id_kategori, judul, penerbit, penulis) " + "VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, this.kategori.getIdkategori()); // Menggunakan idkategori dari objek Kategori
                stmt.setString(2, this.judul);
                stmt.setString(3, this.penerbit);
                stmt.setString(4, this.penulis);
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    this.idbuku = rs.getInt(1);
                }

                rs.close();
                stmt.close();
            } else {
                // UPDATE
                String sql = "UPDATE buku SET id_kategori = ?, judul = ?, penerbit = ?, "
                        + "penulis = ? WHERE id_buku = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, this.kategori.getIdkategori()); // Menggunakan idkategori dari objek Kategori
                stmt.setString(2, this.judul);
                stmt.setString(3, this.penerbit);
                stmt.setString(4, this.penulis);
                stmt.setInt(5, this.idbuku);
                stmt.executeUpdate();
                stmt.close();
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println("Error save: " + e.getMessage());
        }
    }

    // --- delete() ---
    public void delete() {
        Connection conn = DBHelper.connect();
        if (conn == null)
            return;

        try {
            String sql = "DELETE FROM buku WHERE id_buku = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, this.idbuku);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error delete: " + e.getMessage());
        }
    }
}