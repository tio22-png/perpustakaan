package frontend;

import backend.Kategori;
import backend.DBHelper;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class FrmKategori extends JFrame {
    // === Deklarasi komponen ===
    private JTextField txtIdKategori, txtNama, txtKeterangan, txtCari;
    private JButton btnSimpan, btnHapus, btnTambahBaru, btnCari;
    private JTable tblKategori;
    private DefaultTableModel model;

    public FrmKategori() {
        setTitle("Manajemen Kategori");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // === Komponen Input ===
        JLabel lblId = new JLabel("ID Kategori:");
        lblId.setBounds(20, 20, 100, 25);
        add(lblId);

        txtIdKategori = new JTextField();
        txtIdKategori.setBounds(130, 20, 150, 25);
        txtIdKategori.setEnabled(false);
        add(txtIdKategori);

        JLabel lblNama = new JLabel("Nama:");
        lblNama.setBounds(20, 60, 100, 25);
        add(lblNama);

        txtNama = new JTextField();
        txtNama.setBounds(130, 60, 150, 25);
        add(txtNama);

        JLabel lblKet = new JLabel("Keterangan:");
        lblKet.setBounds(20, 100, 100, 25);
        add(lblKet);

        txtKeterangan = new JTextField();
        txtKeterangan.setBounds(130, 100, 150, 25);
        add(txtKeterangan);

        // === Tombol ===
        btnSimpan = new JButton("Simpan");
        btnSimpan.setBounds(320, 20, 100, 25);
        add(btnSimpan);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(320, 60, 100, 25);
        add(btnHapus);

        btnTambahBaru = new JButton("Tambah Baru");
        btnTambahBaru.setBounds(320, 100, 120, 25);
        add(btnTambahBaru);

        txtCari = new JTextField();
        txtCari.setBounds(480, 20, 150, 25);
        add(txtCari);

        btnCari = new JButton("Cari");
        btnCari.setBounds(640, 20, 60, 25);
        add(btnCari);

        // === Tabel ===
        model = new DefaultTableModel(new String[] { "ID", "Nama", "Keterangan" }, 0);
        tblKategori = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tblKategori);
        scrollPane.setBounds(20, 150, 660, 200);
        add(scrollPane);

        // === Event ===
        btnSimpan.addActionListener(e -> simpanData());
        btnHapus.addActionListener(e -> hapusData());
        btnTambahBaru.addActionListener(e -> kosongkanForm());
        btnCari.addActionListener(e -> cari(txtCari.getText()));

        tblKategori.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblKategori.getSelectedRow();
                txtIdKategori.setText(model.getValueAt(row, 0).toString());
                txtNama.setText(model.getValueAt(row, 1).toString());
                txtKeterangan.setText(model.getValueAt(row, 2).toString());
            }
        });

        // === Load Data Awal ===
        kosongkanForm();
        tampilkanData();
    }

    // --- Method untuk kosongkan form ---
    public void kosongkanForm() {
        txtIdKategori.setText("0");
        txtNama.setText("");
        txtKeterangan.setText("");
    }

    // --- Method untuk tampilkan semua data ---
    public void tampilkanData() {
        model.setRowCount(0);
        ArrayList<Kategori> list = Kategori.getAll();
        for (Kategori k : list) {
            model.addRow(new Object[] { k.getIdkategori(), k.getNama(), k.getKeterangan() });
        }
    }

    // --- Method untuk cari data ---
    public void cari(String keyword) {
        model.setRowCount(0);
        ArrayList<Kategori> list = Kategori.search(keyword);
        for (Kategori k : list) {
            model.addRow(new Object[] { k.getIdkategori(), k.getNama(), k.getKeterangan() });
        }
    }

    // --- Method untuk simpan data ---
    public void simpanData() {
        Kategori k = new Kategori();
        k.setIdkategori(Integer.parseInt(txtIdKategori.getText()));
        k.setNama(txtNama.getText());
        k.setKeterangan(txtKeterangan.getText());
        k.save();
        tampilkanData();
        kosongkanForm();
    }

    // --- Method untuk hapus data ---
    public void hapusData() {
        int id = Integer.parseInt(txtIdKategori.getText());
        Kategori k = Kategori.getById(id);
        if (k != null) {
            k.delete();
            tampilkanData();
            kosongkanForm();
        }
    }

    // --- Main method ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmKategori().setVisible(true));
    }
}
