package backend;

import java.sql.*;
import java.util.ArrayList;

public class Kategori {
    private int idkategori;
    private String nama;
    private String keterangan;

    // --- Konstruktor ---
    public Kategori() {
    }

    public Kategori(String nama, String keterangan) {
        this.nama = nama;
        this.keterangan = keterangan;
    }

    // --- Getter Setter ---
    public int getIdkategori() {
        return idkategori;
    }

    public void setIdkategori(int idkategori) {
        this.idkategori = idkategori;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    // --- getById() ---
    public static Kategori getById(int id) {
        Kategori kat = null;
        Connection conn = DBHelper.connect();
        if (conn == null)
            return null;

        try {
            String sql = "SELECT * FROM kategori WHERE id_kategori = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                kat = new Kategori();
                kat.setIdkategori(rs.getInt("id_kategori"));
                kat.setNama(rs.getString("nama"));
                kat.setKeterangan(rs.getString("keterangan"));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error getById: " + e.getMessage());
        }

        return kat;
    }

    // --- getAll() ---
    public static ArrayList<Kategori> getAll() {
        ArrayList<Kategori> listKategori = new ArrayList<>();
        Connection conn = DBHelper.connect();
        if (conn == null)
            return listKategori;

        try {
            String sql = "SELECT * FROM kategori ORDER BY id_kategori";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Kategori kat = new Kategori();
                kat.setIdkategori(rs.getInt("id_kategori"));
                kat.setNama(rs.getString("nama"));
                kat.setKeterangan(rs.getString("keterangan"));
                listKategori.add(kat);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error getAll: " + e.getMessage());
        }

        return listKategori;
    }

    // --- search() ---
    public static ArrayList<Kategori> search(String keyword) {
        ArrayList<Kategori> listKategori = new ArrayList<>();
        Connection conn = DBHelper.connect();
        if (conn == null)
            return listKategori;

        try {
            String sql = "SELECT * FROM kategori WHERE nama ILIKE ? OR keterangan ILIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Kategori kat = new Kategori();
                kat.setIdkategori(rs.getInt("id_kategori"));
                kat.setNama(rs.getString("nama"));
                kat.setKeterangan(rs.getString("keterangan"));
                listKategori.add(kat);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error search: " + e.getMessage());
        }

        return listKategori;
    }

    // --- save() ---
    public void save() {
        Connection conn = DBHelper.connect();
        if (conn == null)
            return;

        try {
            if (getById(this.idkategori) == null) {
                // INSERT
                String sql = "INSERT INTO kategori (nama, keterangan) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, this.nama);
                stmt.setString(2, this.keterangan);
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    this.idkategori = rs.getInt(1);
                }

                rs.close();
                stmt.close();
            } else {
                // UPDATE
                String sql = "UPDATE kategori SET nama = ?, keterangan = ? WHERE id_kategori = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, this.nama);
                stmt.setString(2, this.keterangan);
                stmt.setInt(3, this.idkategori);
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
            String sql = "DELETE FROM kategori WHERE id_kategori = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, this.idkategori);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error delete: " + e.getMessage());
        }
    }
}
