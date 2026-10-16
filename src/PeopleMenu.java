import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PeopleMenu extends JFrame{

    private ButtonGroup genderGroup;
    // index baris yang diklik
    private int selectedIndex = -1;
    // list untuk menampung semua karyawan
    private ArrayList<Person> listPerson;
    private JPanel mainPanel;
    private JTable peopleTable;
    private JTextField nimField;
    private JTextField namaField;
    private JTextField angkatanField;
    private JRadioButton lakiLakiRadioButton;
    private JRadioButton perempuanRadioButton;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JButton deleteButton;

    private Database database;

    public static void main(String[] args) {
        // buat object window
        PeopleMenu menu = new PeopleMenu();

        // atur ukuran window
        menu.setSize( 600, 500);

        // Letakkan window di tengah layar
        menu.setLocationRelativeTo(null);

        // isi window
        menu.setContentPane(menu.mainPanel);

        // ubah warna background
        menu.getContentPane().setBackground(Color.WHITE);

        // tampilkan window
        menu.setVisible(true);

        // agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public PeopleMenu() {
        listPerson = new ArrayList<>();

        // Error Handling
        try {
            database = new Database();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal terhubung ke database: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        }

        // Grouping radioButton agar user hanya bisa memilih salah satu opsi jenis kelamin
        genderGroup = new ButtonGroup();
        genderGroup.add(lakiLakiRadioButton);
        genderGroup.add(perempuanRadioButton);
        lakiLakiRadioButton.setSelected(true);

        // memuat data awal dan menampilkannya ke tabel (Cukup panggil SEKALI saja)
        populateList();
        peopleTable.setModel(setTable());

        // menyembunyikan tombol delete di awal
        deleteButton.setVisible(false);

        // ketika tombol add di tekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex >= 0){
                    deleteData();
                }
            }
        });
        // saat salah satu baris tabel ditekan
        peopleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    selectedIndex = peopleTable.getSelectedRow();
                    if (selectedIndex != -1) {
                        String curNim = peopleTable.getValueAt(selectedIndex, 1).toString();
                        String curNama = peopleTable.getValueAt(selectedIndex, 2).toString();
                        String curAngkatan = peopleTable.getValueAt(selectedIndex, 3).toString();
                        String curJk = peopleTable.getValueAt(selectedIndex, 4).toString();

                        nimField.setText(curNim);
                        namaField.setText(curNama);
                        angkatanField.setText(curAngkatan);

                        if (curJk.equalsIgnoreCase("Laki-laki")) {
                            lakiLakiRadioButton.setSelected(true);
                        } else {
                            perempuanRadioButton.setSelected(true);
                        }

                        addUpdateButton.setText("Update");
                        deleteButton.setVisible(true);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PeopleMenu.this, "Terjadi kesalahan saat memilih data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    private void populateList(){
        listPerson.add(new Person("2301001", "Andi Pratama", 2023, "Laki-laki"));
        listPerson.add(new Person("2301002", "Siti Rahmawati", 2023, "Perempuan"));
        listPerson.add(new Person("2402015", "Budi Santoso", 2024, "Laki-laki"));
        listPerson.add(new Person("2402016", "Dewi Lestari", 2024, "Perempuan"));
        listPerson.add(new Person("2501801", "Fajar Hidayat", 2025, "Laki-laki"));
        listPerson.add(new Person("2501802", "Nabila Putri", 2025, "Perempuan"));
        listPerson.add(new Person("2501803", "Rizky Ramadhan", 2025, "Laki-laki"));
        listPerson.add(new Person("2501804", "Anisa Wijaya", 2025, "Perempuan"));
        listPerson.add(new Person("2603050", "Eko Prasetyo", 2026, "Laki-laki"));
        listPerson.add(new Person("2603051", "Maya Indah", 2026, "Perempuan"));
    }

    // untuk  merender / memperbarui data dari ArrayList ke dalam JTable
    public final DefaultTableModel setTable(){
        // Menentukan Kolom Table
        Object[] cols = {"No", "NIM", "Nama", "Angkatan", "Jenis Kelamin"};
        DefaultTableModel tmp = new DefaultTableModel(null, cols);

        try {
            ResultSet resultSet = database.selectQuery("SELECT * FROM mahasiswa");

            int i = 0;
            while (resultSet.next()){
                Object[] row = new Object[5];
                row[0] = (i + 1);
                row[1] = resultSet.getString("nim");
                row[2] = resultSet.getString("nama");
                row[3] = resultSet.getString("angkatan");
                row[4] = resultSet.getString("jenis_kelamin");
                tmp.addRow(row);
                i++;
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(this, "Gagal mengambil data dari database: " + e.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan tak terduga: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return tmp;
    }

    // Method untuk mengambil teks nilai dari JRadioButton yang sedang terpilih
    private String getSelectedGender(){
        return lakiLakiRadioButton.isSelected() ? "Laki-laki" : "Perempuan";
    }

    // VALIDASI & ERROR HANDLING: CREATE (INSERT)
    public void insertData(){
        try {
            String nim = nimField.getText().trim();
            String nama = namaField.getText().trim();
            String angkatanStr = angkatanField.getText().trim();
            String jenisKelamin = getSelectedGender();

            // 1. Validasi Input Kosong
            if (nim.isEmpty() || nama.isEmpty() || angkatanStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Validasi NIM harus angka (tidak boleh ada huruf)
            if (!nim.matches("^[0-9]+$")) {
                JOptionPane.showMessageDialog(this, "NIM harus berupa angka, tidak boleh mengandung huruf!", "Format Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Validasi Nama harus huruf (tidak boleh ada angka)
            if (!nama.matches("^[a-zA-Z\\s]+$")) {
                JOptionPane.showMessageDialog(this, "Nama harus berupa huruf, tidak boleh mengandung angka!", "Format Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int angkatan = Integer.parseInt(angkatanStr); // Validasi Angkatan berupa angka

            // Eksekusi Query ke Database
            String sqlQuery = "INSERT INTO mahasiswa VALUES('"+ nim +"', '" + nama + "', '" + angkatan + "', '" + jenisKelamin + "')";
            database.insertUpdateDeleteQuery(sqlQuery);

            peopleTable.setModel(setTable());
            clearForm();
            JOptionPane.showMessageDialog(this, "Data Mahasiswa berhasil ditambahkan!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Angkatan harus berupa angka yang valid!", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Error handling jika NIM duplikat (Primary Key sudah ada)
            if (ex.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(this, "Data dengan NIM tersebut sudah ada!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan data ke database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // VALIDASI & ERROR HANDLING: UPDATE
    public void updateData(){
        try {
            String nim = nimField.getText().trim();
            String nama = namaField.getText().trim();
            String angkatanStr = angkatanField.getText().trim();
            String jenisKelamin = getSelectedGender();

            // 1. Validasi Input Kosong
            if (nim.isEmpty() || nama.isEmpty() || angkatanStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Field tidak boleh ada yang kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Validasi NIM harus angka
            if (!nim.matches("^[0-9]+$")) {
                JOptionPane.showMessageDialog(this, "NIM harus berupa angka, tidak boleh mengandung huruf!", "Format Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Validasi Nama harus huruf
            if (!nama.matches("^[a-zA-Z\\s]+$")) {
                JOptionPane.showMessageDialog(this, "Nama harus berupa huruf, tidak boleh mengandung angka!", "Format Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int angkatan = Integer.parseInt(angkatanStr);

            // Eksekusi Query UPDATE ke Database
            String sqlQuery = "UPDATE mahasiswa SET nama = '" + nama +
                    "', angkatan = '" + angkatan +
                    "', jenis_kelamin = '" + jenisKelamin +
                    "' WHERE nim = '" + nim + "'";

            database.insertUpdateDeleteQuery(sqlQuery);

            peopleTable.setModel(setTable());
            clearForm();
            JOptionPane.showMessageDialog(this, "Data Mahasiswa berhasil di-update!");

        } catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Angkatan harus berupa angka yang valid!", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui data di database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method untuk menghapus data mahasiswa
    public void deleteData(){
        try {
            int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data mahasiswa ini?", "Konfirmasi Hapus Data", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION){
                String nim = nimField.getText().trim();

                if (nim.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus terlebih dahulu dari tabel!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Eksekusi Query DELETE dengan Try-Catch
                String sqlQuery = "DELETE FROM mahasiswa WHERE nim = '" + nim + "'";
                database.insertUpdateDeleteQuery(sqlQuery);

                if (selectedIndex >= 0 && selectedIndex < listPerson.size()) {
                    listPerson.remove(selectedIndex);
                }

                peopleTable.setModel(setTable());
                clearForm();
                JOptionPane.showMessageDialog(this, "Data Mahasiswa berhasil dihapus!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data dari database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method untuk mereset/membersihkan field input form
    public void clearForm(){
        nimField.setText("");
        namaField.setText("");
        angkatanField.setText("");
        lakiLakiRadioButton.setSelected(true);

        addUpdateButton.setText("Add");
        deleteButton.setVisible(false);
        selectedIndex = -1;
    }
}
