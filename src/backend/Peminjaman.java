package backend;

import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDate;

public class Peminjaman {
    private int idpeminjaman;
    private Anggota anggota;
    private Buku buku;
    private LocalDate tglPinjam;
    private LocalDate tglKembali;

    // --- Konstruktor ---
    public Peminjaman() {
    }

    public Peminjaman(Anggota anggota, Buku buku, LocalDate tglPinjam, LocalDate tglKembali) {
        this.anggota = anggota;
        this.buku = buku;
        this.tglPinjam = tglPinjam;
        this.tglKembali = tglKembali;
    }

    // --- Getter Setter ---
    public int getIdpeminjaman() {
        return idpeminjaman;
    }

    public void setIdpeminjaman(int idpeminjaman) {
        this.idpeminjaman = idpeminjaman;
    }

    public Anggota getAnggota() {
        return anggota;
    }

    public void setAnggota(Anggota anggota) {
        this.anggota = anggota;
    }

    public Buku getBuku() {
        return buku;
    }

    public void setBuku(Buku buku) {
        this.buku = buku;
    }

    public LocalDate getTglPinjam() {
        return tglPinjam;
    }

    public void setTglPinjam(LocalDate tglPinjam) {
        this.tglPinjam = tglPinjam;
    }

    public LocalDate getTglKembali() {
        return tglKembali;
    }

    public void setTglKembali(LocalDate tglKembali) {
        this.tglKembali = tglKembali;
    }

    // Helper method untuk konversi Date ke LocalDate
    private static LocalDate dateToLocalDate(Date date) {
        if (date == null)
            return null;
        return date.toLocalDate();
    }

    // --- getById() dengan JOIN ---
    public static Peminjaman getById(int id) {
        Peminjaman pem = null;
        Connection conn = DBHelper.connect();
        if (conn == null)
            return null;

        try {
            String sql = "SELECT p.*, a.nama as anggota_nama, a.alamat, a.telepon, " +
                    "b.judul, b.penerbit, b.penulis, k.id_kategori, k.nama as kategori_nama, k.keterangan " +
                    "FROM peminjaman p " +
                    "JOIN anggota a ON p.id_anggota = a.id_anggota " +
                    "JOIN buku b ON p.id_buku = b.id_buku " +
                    "JOIN kategori k ON b.id_kategori = k.id_kategori " +
                    "WHERE p.id_peminjaman = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                pem = new Peminjaman();
                pem.setIdpeminjaman(rs.getInt("id_peminjaman"));

                Anggota ang = new Anggota();
                ang.setIdanggota(rs.getInt("id_anggota"));
                ang.setNama(rs.getString("anggota_nama"));
                ang.setAlamat(rs.getString("alamat"));
                ang.setTelepon(rs.getString("telepon"));
                pem.setAnggota(ang);

                Kategori kat = new Kategori();
                kat.setIdkategori(rs.getInt("id_kategori"));
                kat.setNama(rs.getString("kategori_nama"));
                kat.setKeterangan(rs.getString("keterangan"));

                Buku buku = new Buku();
                buku.setIdbuku(rs.getInt("id_buku"));
                buku.setKategori(kat);
                buku.setJudul(rs.getString("judul"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setPenulis(rs.getString("penulis"));
                pem.setBuku(buku);

                pem.setTglPinjam(dateToLocalDate(rs.getDate("tanggal_pinjam")));
                pem.setTglKembali(dateToLocalDate(rs.getDate("tanggal_kembali")));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error getById: " + e.getMessage());
        }

        return pem;
    }

    // --- getAll() dengan JOIN ---
    public static ArrayList<Peminjaman> getAll() {
        ArrayList<Peminjaman> listPeminjaman = new ArrayList<>();
        Connection conn = DBHelper.connect();
        if (conn == null)
            return listPeminjaman;

        try {
            String sql = "SELECT p.*, a.nama as anggota_nama, a.alamat, a.telepon, " +
                    "b.judul, b.penerbit, b.penulis, k.id_kategori, k.nama as kategori_nama, k.keterangan " +
                    "FROM peminjaman p " +
                    "JOIN anggota a ON p.id_anggota = a.id_anggota " +
                    "JOIN buku b ON p.id_buku = b.id_buku " +
                    "JOIN kategori k ON b.id_kategori = k.id_kategori " +
                    "ORDER BY p.id_peminjaman";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Peminjaman pem = new Peminjaman();
                pem.setIdpeminjaman(rs.getInt("id_peminjaman"));

                Anggota ang = new Anggota();
                ang.setIdanggota(rs.getInt("id_anggota"));
                ang.setNama(rs.getString("anggota_nama"));
                ang.setAlamat(rs.getString("alamat"));
                ang.setTelepon(rs.getString("telepon"));
                pem.setAnggota(ang);

                Kategori kat = new Kategori();
                kat.setIdkategori(rs.getInt("id_kategori"));
                kat.setNama(rs.getString("kategori_nama"));
                kat.setKeterangan(rs.getString("keterangan"));

                Buku buku = new Buku();
                buku.setIdbuku(rs.getInt("id_buku"));
                buku.setKategori(kat);
                buku.setJudul(rs.getString("judul"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setPenulis(rs.getString("penulis"));
                pem.setBuku(buku);

                pem.setTglPinjam(dateToLocalDate(rs.getDate("tanggal_pinjam")));
                pem.setTglKembali(dateToLocalDate(rs.getDate("tanggal_kembali")));

                listPeminjaman.add(pem);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error getAll: " + e.getMessage());
        }

        return listPeminjaman;
    }

    // --- search() dengan JOIN ---
    public static ArrayList<Peminjaman> search(String keyword) {
        ArrayList<Peminjaman> listPeminjaman = new ArrayList<>();
        Connection conn = DBHelper.connect();
        if (conn == null)
            return listPeminjaman;

        try {
            String sql = "SELECT p.*, a.nama as anggota_nama, a.alamat, a.telepon, " +
                    "b.judul, b.penerbit, b.penulis, k.id_kategori, k.nama as kategori_nama, k.keterangan " +
                    "FROM peminjaman p " +
                    "JOIN anggota a ON p.id_anggota = a.id_anggota " +
                    "JOIN buku b ON p.id_buku = b.id_buku " +
                    "JOIN kategori k ON b.id_kategori = k.id_kategori " +
                    "WHERE a.nama ILIKE ? OR b.judul ILIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Peminjaman pem = new Peminjaman();
                pem.setIdpeminjaman(rs.getInt("id_peminjaman"));

                Anggota ang = new Anggota();
                ang.setIdanggota(rs.getInt("id_anggota"));
                ang.setNama(rs.getString("anggota_nama"));
                ang.setAlamat(rs.getString("alamat"));
                ang.setTelepon(rs.getString("telepon"));
                pem.setAnggota(ang);

                Kategori kat = new Kategori();
                kat.setIdkategori(rs.getInt("id_kategori"));
                kat.setNama(rs.getString("kategori_nama"));
                kat.setKeterangan(rs.getString("keterangan"));

                Buku buku = new Buku();
                buku.setIdbuku(rs.getInt("id_buku"));
                buku.setKategori(kat);
                buku.setJudul(rs.getString("judul"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setPenulis(rs.getString("penulis"));
                pem.setBuku(buku);

                pem.setTglPinjam(dateToLocalDate(rs.getDate("tanggal_pinjam")));
                pem.setTglKembali(dateToLocalDate(rs.getDate("tanggal_kembali")));

                listPeminjaman.add(pem);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error search: " + e.getMessage());
        }

        return listPeminjaman;
    }

    // --- save() ---
    public void save() {
        Connection conn = DBHelper.connect();
        if (conn == null)
            return;

        try {
            if (getById(this.idpeminjaman) == null) {
                // INSERT
                String sql = "INSERT INTO peminjaman (id_anggota, id_buku, tanggal_pinjam, tanggal_kembali) " +
                        "VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, this.anggota.getIdanggota());
                stmt.setInt(2, this.buku.getIdbuku());
                stmt.setDate(3, java.sql.Date.valueOf(this.tglPinjam));
                if (this.tglKembali != null) {
                    stmt.setDate(4, java.sql.Date.valueOf(this.tglKembali));
                } else {
                    stmt.setNull(4, java.sql.Types.DATE);
                }
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    this.idpeminjaman = rs.getInt(1);
                }

                rs.close();
                stmt.close();
            } else {
                // UPDATE
                String sql = "UPDATE peminjaman SET id_anggota = ?, id_buku = ?, " +
                        "tanggal_pinjam = ?, tanggal_kembali = ? WHERE id_peminjaman = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, this.anggota.getIdanggota());
                stmt.setInt(2, this.buku.getIdbuku());
                stmt.setDate(3, java.sql.Date.valueOf(this.tglPinjam));
                if (this.tglKembali != null) {
                    stmt.setDate(4, java.sql.Date.valueOf(this.tglKembali));
                } else {
                    stmt.setNull(4, java.sql.Types.DATE);
                }
                stmt.setInt(5, this.idpeminjaman);
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
            String sql = "DELETE FROM peminjaman WHERE id_peminjaman = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, this.idpeminjaman);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error delete: " + e.getMessage());
        }
    }
}