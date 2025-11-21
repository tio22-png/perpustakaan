package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    private static final String URL = "jdbc:postgresql://localhost:5433/perpustakaan_pbo";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345678";

    public static Connection connect() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Koneksi berhasil!");
            return conn;
        } catch (SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
            return null;
        }
    }
}
