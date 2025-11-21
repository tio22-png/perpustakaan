package backend;

import java.sql.*;
import java.util.ArrayList;

public class Anggota {
    private int idanggota;
    private String nama;
    private String alamat;
    private String telepon;

    // --- Konstruktor ---
    public Anggota() {
    }

    public Anggota(String nama, String alamat, String telepon) {
        this.nama = nama;
        this.alamat = alamat;
        this.telepon = telepon;
    }

    // --- Getter Setter ---
    public int getIdanggota() {
        return idanggota;
    }

    public void setIdanggota(int idanggota) {
        this.idanggota = idanggota;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    // --- getById() ---
    public static Anggota getById(int id) {
        Anggota ang = null;
        Connection conn = DBHelper.connect();
        if (conn == null)
            return null;

        try {
            String sql = "SELECT * FROM anggota WHERE id_anggota = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ang = new Anggota();
                ang.setIdanggota(rs.getInt("id_anggota"));
                ang.setNama(rs.getString("nama"));
                ang.setAlamat(rs.getString("alamat"));
                ang.setTelepon(rs.getString("telepon"));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error getById: " + e.getMessage());
        }

        return ang;
    }

    // --- getAll() ---
    public static ArrayList<Anggota> getAll() {
        ArrayList<Anggota> listAnggota = new ArrayList<>();
        Connection conn = DBHelper.connect();
        if (conn == null)
            return listAnggota;

        try {
            String sql = "SELECT * FROM anggota ORDER BY id_anggota";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Anggota ang = new Anggota();
                ang.setIdanggota(rs.getInt("id_anggota"));
                ang.setNama(rs.getString("nama"));
                ang.setAlamat(rs.getString("alamat"));
                ang.setTelepon(rs.getString("telepon"));
                listAnggota.add(ang);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error getAll: " + e.getMessage());
        }

        return listAnggota;
    }

    // --- search() ---
    public static ArrayList<Anggota> search(String keyword) {
        ArrayList<Anggota> listAnggota = new ArrayList<>();
        Connection conn = DBHelper.connect();
        if (conn == null)
            return listAnggota;

        try {
            String sql = "SELECT * FROM anggota WHERE nama ILIKE ? OR alamat ILIKE ? OR telepon ILIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            stmt.setString(3, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Anggota ang = new Anggota();
                ang.setIdanggota(rs.getInt("id_anggota"));
                ang.setNama(rs.getString("nama"));
                ang.setAlamat(rs.getString("alamat"));
                ang.setTelepon(rs.getString("telepon"));
                listAnggota.add(ang);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error search: " + e.getMessage());
        }

        return listAnggota;
    }

    // --- save() ---
    public void save() {
        Connection conn = DBHelper.connect();
        if (conn == null)
            return;

        try {
            if (getById(this.idanggota) == null) {
                // INSERT
                String sql = "INSERT INTO anggota (nama, alamat, telepon) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, this.nama);
                stmt.setString(2, this.alamat);
                stmt.setString(3, this.telepon);
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    this.idanggota = rs.getInt(1);
                }

                rs.close();
                stmt.close();
            } else {
                // UPDATE
                String sql = "UPDATE anggota SET nama = ?, alamat = ?, telepon = ? WHERE id_anggota = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, this.nama);
                stmt.setString(2, this.alamat);
                stmt.setString(3, this.telepon);
                stmt.setInt(4, this.idanggota);
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
            String sql = "DELETE FROM anggota WHERE id_anggota = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, this.idanggota);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error delete: " + e.getMessage());
        }
    }
}