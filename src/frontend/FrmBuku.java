package frontend;

import backend.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class FrmBuku extends JFrame {
    private JTextField txtIdBuku, txtJudul, txtPenerbit, txtPenulis, txtCari;
    private JComboBox<String> cmbKategori;
    private JButton btnSimpan, btnHapus, btnTambahBaru, btnCari;
    private JTable tblBuku;
    private DefaultTableModel model;

    public FrmBuku() {
        setTitle("Data Buku");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // === Komponen Form ===
        JLabel lblIdBuku = new JLabel("ID Buku:");
        lblIdBuku.setBounds(20, 20, 100, 25);
        add(lblIdBuku);

        txtIdBuku = new JTextField();
        txtIdBuku.setBounds(130, 20, 100, 25);
        txtIdBuku.setEnabled(false);
        add(txtIdBuku);

        JLabel lblKategori = new JLabel("Kategori:");
        lblKategori.setBounds(20, 50, 100, 25);
        add(lblKategori);

        cmbKategori = new JComboBox<>();
        cmbKategori.setBounds(130, 50, 200, 25);
        add(cmbKategori);

        JLabel lblJudul = new JLabel("Judul:");
        lblJudul.setBounds(20, 80, 100, 25);
        add(lblJudul);

        txtJudul = new JTextField();
        txtJudul.setBounds(130, 80, 300, 25);
        add(txtJudul);

        JLabel lblPenerbit = new JLabel("Penerbit:");
        lblPenerbit.setBounds(20, 110, 100, 25);
        add(lblPenerbit);

        txtPenerbit = new JTextField();
        txtPenerbit.setBounds(130, 110, 200, 25);
        add(txtPenerbit);

        JLabel lblPenulis = new JLabel("Penulis:");
        lblPenulis.setBounds(20, 140, 100, 25);
        add(lblPenulis);

        txtPenulis = new JTextField();
        txtPenulis.setBounds(130, 140, 200, 25);
        add(txtPenulis);

        // === Tombol-tombol ===
        btnSimpan = new JButton("Simpan");
        btnSimpan.setBounds(450, 20, 100, 25);
        add(btnSimpan);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(450, 50, 100, 25);
        add(btnHapus);

        btnTambahBaru = new JButton("Tambah Baru");
        btnTambahBaru.setBounds(450, 80, 120, 25);
        add(btnTambahBaru);

        txtCari = new JTextField();
        txtCari.setBounds(580, 20, 150, 25);
        add(txtCari);

        btnCari = new JButton("Cari");
        btnCari.setBounds(740, 20, 60, 25);
        add(btnCari);

        // === Tabel ===
        model = new DefaultTableModel(new String[] { "ID", "Kategori", "Judul", "Penerbit", "Penulis" }, 0);
        tblBuku = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tblBuku);
        scrollPane.setBounds(20, 180, 750, 250);
        add(scrollPane);

        // === Event Handling ===
        btnSimpan.addActionListener(e -> simpanData());
        btnHapus.addActionListener(e -> hapusData());
        btnTambahBaru.addActionListener(e -> kosongkanForm());
        btnCari.addActionListener(e -> cari(txtCari.getText()));

        tblBuku.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblBuku.getSelectedRow();
                if (row >= 0) {
                    tampilkanDataKeTabel(row);
                }
            }
        });

        // === Inisialisasi ===
        isiComboKategori(); // Isi combo box terlebih dahulu
        tampilkanData();
        kosongkanForm();
    }

    private void isiComboKategori() {
        cmbKategori.removeAllItems();
        ArrayList<Kategori> list = Kategori.getAll();
        for (Kategori k : list) {
            cmbKategori.addItem(k.getIdkategori() + " - " + k.getNama());
        }
    }

    private void kosongkanForm() {
        txtIdBuku.setText("0");
        if (cmbKategori.getItemCount() > 0) {
            cmbKategori.setSelectedIndex(0);
        }
        txtJudul.setText("");
        txtPenerbit.setText("");
        txtPenulis.setText("");
    }

    private void tampilkanData() {
        model.setRowCount(0);
        ArrayList<Buku> list = Buku.getAll();
        for (Buku b : list) {
            model.addRow(new Object[] { b.getIdbuku(), b.getKategori().getNama(), b.getJudul(), b.getPenerbit(),
                    b.getPenulis() });
        }
    }

    private void tampilkanDataKeTabel(int row) {
        txtIdBuku.setText(model.getValueAt(row, 0).toString());

        // Cari kategori yang sesuai di combo box
        String kategoriNama = model.getValueAt(row, 1).toString();
        for (int i = 0; i < cmbKategori.getItemCount(); i++) {
            if (cmbKategori.getItemAt(i).contains(kategoriNama)) {
                cmbKategori.setSelectedIndex(i);
                break;
            }
        }

        txtJudul.setText(model.getValueAt(row, 2).toString());
        txtPenerbit.setText(model.getValueAt(row, 3).toString());
        txtPenulis.setText(model.getValueAt(row, 4).toString());
    }

    private void cari(String keyword) {
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
            Buku b = Buku.getById(id);
            if (b != null) {
                model.addRow(new Object[] { b.getIdbuku(), b.getKategori().getNama(), b.getJudul(), b.getPenerbit(),
                        b.getPenulis() });
            }
            return;
        } catch (NumberFormatException ex) {
            // bukan angka -> lanjut ke pencarian teks
        }

        ArrayList<Buku> list = Buku.search(kw);
        for (Buku b : list) {
            model.addRow(new Object[] { b.getIdbuku(), b.getKategori().getNama(), b.getJudul(), b.getPenerbit(),
                    b.getPenulis() });
        }
    }

    private void simpanData() {
        // Ambil ID kategori dari combo box
        String selectedKategori = cmbKategori.getSelectedItem().toString();
        int idKategori = Integer.parseInt(selectedKategori.split(" - ")[0]);

        Buku b = new Buku();
        b.setIdbuku(Integer.parseInt(txtIdBuku.getText()));
        b.setKategori(Kategori.getById(idKategori));
        b.setJudul(txtJudul.getText());
        b.setPenerbit(txtPenerbit.getText());
        b.setPenulis(txtPenulis.getText());

        b.save();
        tampilkanData();
        kosongkanForm();
    }

    private void hapusData() {
        int id = Integer.parseInt(txtIdBuku.getText());
        Buku b = Buku.getById(id);
        if (b != null) {
            b.delete();
            tampilkanData();
            kosongkanForm();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FrmBuku().setVisible(true);
        });
    }
}