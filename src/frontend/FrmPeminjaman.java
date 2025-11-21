package frontend;

import backend.*;
import java.util.ArrayList;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class FrmPeminjaman extends JFrame {
    private JTextField txtIdPeminjaman, txtIdAnggota, txtIdBuku;
    private JLabel lblNamaAnggota, lblJudulBuku;
    private JButton btnCariAnggota, btnCariBuku, btnSimpan, btnTambahBaru, btnHapus;
    private JSpinner spinTglPinjam, spinTglKembali;
    private JTable tblPeminjaman;
    private DefaultTableModel model;

    public FrmPeminjaman() {
        setTitle("Data Peminjaman");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // === ID Peminjaman ===
        JLabel lblIdPeminjaman = new JLabel("ID:");
        lblIdPeminjaman.setBounds(20, 20, 50, 25);
        add(lblIdPeminjaman);

        txtIdPeminjaman = new JTextField();
        txtIdPeminjaman.setBounds(80, 20, 80, 25);
        txtIdPeminjaman.setEnabled(false);
        add(txtIdPeminjaman);

        // === ID Anggota ===
        JLabel lblIdAnggota = new JLabel("ID Anggota:");
        lblIdAnggota.setBounds(20, 60, 100, 25);
        add(lblIdAnggota);

        txtIdAnggota = new JTextField();
        txtIdAnggota.setBounds(130, 60, 80, 25);
        add(txtIdAnggota);

        btnCariAnggota = new JButton("Cari");
        btnCariAnggota.setBounds(220, 60, 70, 25);
        add(btnCariAnggota);

        lblNamaAnggota = new JLabel("");
        lblNamaAnggota.setBounds(300, 60, 250, 25);
        add(lblNamaAnggota);

        // === ID Buku ===
        JLabel lblIdBuku = new JLabel("ID Buku:");
        lblIdBuku.setBounds(20, 100, 100, 25);
        add(lblIdBuku);

        txtIdBuku = new JTextField();
        txtIdBuku.setBounds(130, 100, 80, 25);
        add(txtIdBuku);

        btnCariBuku = new JButton("Cari");
        btnCariBuku.setBounds(220, 100, 70, 25);
        add(btnCariBuku);

        lblJudulBuku = new JLabel("");
        lblJudulBuku.setBounds(300, 100, 350, 25);
        add(lblJudulBuku);

        // === Tanggal Pinjam ===
        JLabel lblTglPinjam = new JLabel("Tanggal Pinjam:");
        lblTglPinjam.setBounds(20, 140, 100, 25);
        add(lblTglPinjam);

        spinTglPinjam = new JSpinner(new javax.swing.SpinnerDateModel());
        JSpinner.DateEditor editorPinjam = new JSpinner.DateEditor(spinTglPinjam, "yyyy-MM-dd");
        spinTglPinjam.setEditor(editorPinjam);
        spinTglPinjam.setBounds(130, 140, 150, 25);
        add(spinTglPinjam);

        // === Tanggal Kembali ===
        JLabel lblTglKembali = new JLabel("Tanggal Kembali:");
        lblTglKembali.setBounds(300, 140, 120, 25);
        add(lblTglKembali);

        spinTglKembali = new JSpinner(new javax.swing.SpinnerDateModel());
        JSpinner.DateEditor editorKembali = new JSpinner.DateEditor(spinTglKembali, "yyyy-MM-dd");
        spinTglKembali.setEditor(editorKembali);
        spinTglKembali.setBounds(430, 140, 150, 25);
        add(spinTglKembali);

        // === Tombol ===
        btnSimpan = new JButton("Simpan");
        btnSimpan.setBounds(620, 60, 100, 25);
        add(btnSimpan);

        btnTambahBaru = new JButton("Tambah Baru");
        btnTambahBaru.setBounds(620, 100, 120, 25);
        add(btnTambahBaru);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(620, 140, 100, 25);
        add(btnHapus);

        // === Tabel ===
        model = new DefaultTableModel(new String[] { "ID", "Nama Anggota", "Judul Buku", "Tgl Pinjam", "Tgl Kembali" },
                0);
        tblPeminjaman = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tblPeminjaman);
        scrollPane.setBounds(20, 200, 850, 280);
        add(scrollPane);

        // === Event Handling ===
        btnCariAnggota.addActionListener(e -> cariAnggota());
        btnCariBuku.addActionListener(e -> cariBuku());
        btnSimpan.addActionListener(e -> simpanData());
        btnTambahBaru.addActionListener(e -> kosongkanForm());
        btnHapus.addActionListener(e -> hapusData());

        tblPeminjaman.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblPeminjaman.getSelectedRow();
                if (row >= 0) {
                    tampilkanDataKeTabel(row);
                }
            }
        });

        // === Inisialisasi ===
        kosongkanForm();
        tampilkanData();
    }

    private void kosongkanForm() {
        txtIdPeminjaman.setText("0");
        txtIdAnggota.setText("");
        txtIdBuku.setText("");
        lblNamaAnggota.setText("");
        lblJudulBuku.setText("");
        spinTglPinjam.setValue(new java.util.Date());
        spinTglKembali.setValue(new java.util.Date());
    }

    private void cariAnggota() {
        String idStr = txtIdAnggota.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan ID Anggota terlebih dahulu", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Anggota ang = Anggota.getById(id);
            if (ang != null) {
                lblNamaAnggota.setText(ang.getNama());
            } else {
                lblNamaAnggota.setText("Tidak ditemukan");
                JOptionPane.showMessageDialog(this, "Anggota dengan ID " + id + " tidak ditemukan", "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID harus berupa angka", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cariBuku() {
        String idStr = txtIdBuku.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan ID Buku terlebih dahulu", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Buku buku = Buku.getById(id);
            if (buku != null) {
                lblJudulBuku.setText(buku.getJudul() + " (" + buku.getPenulis() + ")");
            } else {
                lblJudulBuku.setText("Tidak ditemukan");
                JOptionPane.showMessageDialog(this, "Buku dengan ID " + id + " tidak ditemukan", "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID harus berupa angka", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tampilkanData() {
        model.setRowCount(0);
        ArrayList<Peminjaman> list = Peminjaman.getAll();
        for (Peminjaman p : list) {
            String tglKembali = p.getTglKembali() != null ? p.getTglKembali().toString() : "-";
            model.addRow(new Object[] {
                    p.getIdpeminjaman(),
                    p.getAnggota().getNama(),
                    p.getBuku().getJudul(),
                    p.getTglPinjam().toString(),
                    tglKembali
            });
        }
    }

    private void tampilkanDataKeTabel(int row) {
        txtIdPeminjaman.setText(model.getValueAt(row, 0).toString());
        // Dapatkan data lengkap dari database
        int id = Integer.parseInt(txtIdPeminjaman.getText());
        Peminjaman p = Peminjaman.getById(id);
        if (p != null) {
            txtIdAnggota.setText(String.valueOf(p.getAnggota().getIdanggota()));
            lblNamaAnggota.setText(p.getAnggota().getNama());
            txtIdBuku.setText(String.valueOf(p.getBuku().getIdbuku()));
            lblJudulBuku.setText(p.getBuku().getJudul());
            spinTglPinjam.setValue(java.sql.Date.valueOf(p.getTglPinjam()));
            if (p.getTglKembali() != null) {
                spinTglKembali.setValue(java.sql.Date.valueOf(p.getTglKembali()));
            }
        }
    }

    private void simpanData() {
        if (txtIdAnggota.getText().isEmpty() || txtIdBuku.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Anggota dan ID Buku harus diisi", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idAnggota = Integer.parseInt(txtIdAnggota.getText());
            int idBuku = Integer.parseInt(txtIdBuku.getText());

            Anggota ang = Anggota.getById(idAnggota);
            Buku buku = Buku.getById(idBuku);

            if (ang == null || buku == null) {
                JOptionPane.showMessageDialog(this, "Anggota atau Buku tidak ditemukan", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Peminjaman p = new Peminjaman();
            p.setIdpeminjaman(Integer.parseInt(txtIdPeminjaman.getText()));
            p.setAnggota(ang);
            p.setBuku(buku);

            java.util.Date tglPinjamObj = (java.util.Date) spinTglPinjam.getValue();
            p.setTglPinjam(new java.sql.Date(tglPinjamObj.getTime()).toLocalDate());

            java.util.Date tglKembaliObj = (java.util.Date) spinTglKembali.getValue();
            if (tglKembaliObj != null) {
                p.setTglKembali(new java.sql.Date(tglKembaliObj.getTime()).toLocalDate());
            }

            p.save();
            tampilkanData();
            kosongkanForm();
            JOptionPane.showMessageDialog(this, "Data peminjaman berhasil disimpan", "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Format input tidak valid", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusData() {
        String idStr = txtIdPeminjaman.getText().trim();
        if (idStr.isEmpty() || idStr.equals("0")) {
            JOptionPane.showMessageDialog(this, "Pilih data peminjaman terlebih dahulu", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Peminjaman p = Peminjaman.getById(id);
            if (p != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    p.delete();
                    tampilkanData();
                    kosongkanForm();
                    JOptionPane.showMessageDialog(this, "Data peminjaman berhasil dihapus", "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID tidak valid", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmPeminjaman().setVisible(true));
    }
}