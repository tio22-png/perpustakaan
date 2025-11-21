package frontend;

import backend.Anggota;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;

public class FrmAnggota extends JFrame {
    private JTextField txtIdAnggota, txtNama, txtAlamat, txtTelepon, txtCari;
    private JButton btnSimpan, btnHapus, btnTambahBaru, btnCari;
    private JTable tblAnggota;
    private DefaultTableModel model;

    public FrmAnggota() {
        setTitle("Manajemen Anggota");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // === Komponen Input ===
        JLabel lblId = new JLabel("ID Anggota:");
        lblId.setBounds(20, 20, 100, 25);
        add(lblId);

        txtIdAnggota = new JTextField();
        txtIdAnggota.setBounds(130, 20, 150, 25);
        txtIdAnggota.setEnabled(false);
        add(txtIdAnggota);

        JLabel lblNama = new JLabel("Nama:");
        lblNama.setBounds(20, 60, 100, 25);
        add(lblNama);

        txtNama = new JTextField();
        txtNama.setBounds(130, 60, 250, 25);
        add(txtNama);

        JLabel lblAlamat = new JLabel("Alamat:");
        lblAlamat.setBounds(20, 100, 100, 25);
        add(lblAlamat);

        txtAlamat = new JTextField();
        txtAlamat.setBounds(130, 100, 250, 25);
        add(txtAlamat);

        JLabel lblTelepon = new JLabel("Telepon:");
        lblTelepon.setBounds(20, 140, 100, 25);
        add(lblTelepon);

        txtTelepon = new JTextField();
        txtTelepon.setBounds(130, 140, 150, 25);

        // Filter hanya angka untuk telepon
        PlainDocument doc = (PlainDocument) txtTelepon.getDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string == null)
                    return;
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text == null)
                    return;
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        add(txtTelepon);

        // === Tombol ===
        btnSimpan = new JButton("Simpan");
        btnSimpan.setBounds(400, 20, 100, 25);
        add(btnSimpan);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(400, 60, 100, 25);
        add(btnHapus);

        btnTambahBaru = new JButton("Tambah Baru");
        btnTambahBaru.setBounds(400, 100, 120, 25);
        add(btnTambahBaru);

        txtCari = new JTextField();
        txtCari.setBounds(530, 20, 150, 25);
        add(txtCari);

        btnCari = new JButton("Cari");
        btnCari.setBounds(690, 20, 80, 25);
        add(btnCari);

        // === Tabel ===
        model = new DefaultTableModel(new String[] { "ID", "Nama", "Alamat", "Telepon" }, 0);
        tblAnggota = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tblAnggota);
        scrollPane.setBounds(20, 180, 750, 250);
        add(scrollPane);

        // === Event Handling ===
        btnSimpan.addActionListener(e -> simpanData());
        btnHapus.addActionListener(e -> hapusData());
        btnTambahBaru.addActionListener(e -> kosongkanForm());
        btnCari.addActionListener(e -> cari(txtCari.getText()));

        tblAnggota.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblAnggota.getSelectedRow();
                txtIdAnggota.setText(model.getValueAt(row, 0).toString());
                txtNama.setText(model.getValueAt(row, 1).toString());
                txtAlamat.setText(model.getValueAt(row, 2).toString());
                txtTelepon.setText(model.getValueAt(row, 3).toString());
            }
        });

        // === Load Data Awal ===
        kosongkanForm();
        tampilkanData();
    }

    public void kosongkanForm() {
        txtIdAnggota.setText("0");
        txtNama.setText("");
        txtAlamat.setText("");
        txtTelepon.setText("");
    }

    public void tampilkanData() {
        model.setRowCount(0);
        ArrayList<Anggota> list = Anggota.getAll();
        for (Anggota a : list) {
            model.addRow(new Object[] { a.getIdanggota(), a.getNama(), a.getAlamat(), a.getTelepon() });
        }
    }

    public void cari(String keyword) {
        model.setRowCount(0);
        if (keyword == null || keyword.trim().isEmpty()) {
            // kosong -> tampilkan semua
            tampilkanData();
            return;
        }

        String kw = keyword.trim();
        // Jika keyword hanya angka, coba cari berdasarkan ID
        try {
            int id = Integer.parseInt(kw);
            Anggota a = Anggota.getById(id);
            if (a != null) {
                model.addRow(new Object[] { a.getIdanggota(), a.getNama(), a.getAlamat(), a.getTelepon() });
            }
            return;
        } catch (NumberFormatException ex) {
            // bukan angka -> lanjut ke pencarian teks
        }

        ArrayList<Anggota> list = Anggota.search(kw);
        for (Anggota a : list) {
            model.addRow(new Object[] { a.getIdanggota(), a.getNama(), a.getAlamat(), a.getTelepon() });
        }
    }

    public void simpanData() {
        Anggota a = new Anggota();
        a.setIdanggota(Integer.parseInt(txtIdAnggota.getText()));
        a.setNama(txtNama.getText());
        a.setAlamat(txtAlamat.getText());
        a.setTelepon(txtTelepon.getText());
        a.save();
        tampilkanData();
        kosongkanForm();
    }

    public void hapusData() {
        int id = Integer.parseInt(txtIdAnggota.getText());
        Anggota a = Anggota.getById(id);
        if (a != null) {
            a.delete();
            tampilkanData();
            kosongkanForm();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmAnggota().setVisible(true));
    }
}