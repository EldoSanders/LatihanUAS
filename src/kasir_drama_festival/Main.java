package kasir_drama_festival;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

// Class Main [cite: 136]
public class Main {
    // Atribut [cite: 137-139]
    private Scanner scanner;
    private List<Item> items;
    private List<Invoice> invoices;
    private final String ITEMS_FILE = "Items.txt"; // [cite: 53]
    private final String REPORT_FILE = "Report.txt"; // [cite: 62]
    private int nextItemId = 1;

    public Main() {
        // Inisialisasi
        scanner = new Scanner(System.in);
        items = new ArrayList<>();
        invoices = new ArrayList<>();

        // Memuat data saat program dimulai
        loadItemsFromFile();
        loadInvoicesFromFile();

        // Jika file kosong, masukkan data awal
        if (items.isEmpty()) {
            seedItems(); // [cite: 141]
            saveItemsToFile();
        } else {
            // Tentukan nextItemId berdasarkan ID terbesar yang ada
            nextItemId = items.stream().mapToInt(Item::getId).max().orElse(0) + 1;
        }
    }

    // Main method [cite: 140]
    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    // Loop utama program
    public void run() {
        boolean running = true;
        while (running) {
            printMenu(); // [cite: 143]
            try {
                System.out.print("Pilih menu (1-7): ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        addItemMenu(); // [cite: 147]
                        break;
                    case 2:
                        printListOfItem(); // [cite: 144]
                        break;
                    case 3:
                        editItemMenu(); // [cite: 149]
                        break;
                    case 4:
                        deleteItem(); // [cite: 151]
                        break;
                    case 5:
                        searchItems(); // [cite: 154]
                        break;
                    case 6:
                        buyItems(); // [cite: 155]
                        break;
                    case 7:
                        printInvoices(); // [cite: 156]
                        break;
                    case 8:
                        System.out.println("\nProgram Kasir Selesai. Terima kasih.");
                        running = false;
                        break;
                    default:
                        System.out.println("\n[Error] Pilihan tidak valid. Mohon masukkan angka 1-8.");
                }
            } catch (NumberFormatException e) {
                // Penanganan Error Input [cite: 63, 64]
                System.out.println("\n[Error] Input harus berupa angka. Silakan coba lagi.");
            } catch (Exception e) {
                // Penanganan Exception Umum [cite: 63, 65]
                System.out.println("\n[Error] Terjadi kesalahan: " + e.getMessage());
            }
            if(running) {
                System.out.print("\nTekan ENTER untuk melanjutkan...");
                scanner.nextLine();
            }
        }
    }

    // Data awal [cite: 141]
    public void seedItems() {
        items.add(new Ticket(nextItemId++, "Pertunjukan Hamlet", 150000.0, 50, LocalDate.now().plusDays(10)));
        items.add(new Merchandise(nextItemId++, "Kaos Teater", 85000.0, 100, "Kaos"));
        items.add(new Recording(nextItemId++, "Rekaman Pertunjukan Romeo", 120000.0, 30, 150));
        items.add(new Ticket(nextItemId++, "Meet & Greet Aktor", 250000.0, 20, LocalDate.now().plusDays(5)));
        items.add(new Merchandise(nextItemId++, "Poster Limited Edition", 45000.0, 75, "Poster"));
    }

    // --- FILE I/O ---

    // Menyimpan daftar barang ke Items.txt [cite: 53, 142]
    private void saveItemsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ITEMS_FILE))) {
            oos.writeObject(items);
            System.out.println("\n[INFO] Data barang berhasil disimpan ke " + ITEMS_FILE);
        } catch (IOException e) {
            System.out.println("\n[Error] Gagal menyimpan data barang ke file: " + e.getMessage());
        }
    }

    // Memuat daftar barang dari Items.txt
    private void loadItemsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ITEMS_FILE))) {
            items = (List<Item>) ois.readObject();
            System.out.println("\n[INFO] Data barang berhasil dimuat dari " + ITEMS_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("\n[INFO] File " + ITEMS_FILE + " tidak ditemukan. Membuat daftar barang baru.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\n[Error] Gagal memuat data barang dari file: " + e.getMessage());
        }
    }

    // Menyimpan daftar transaksi ke Report.txt [cite: 62, 142]
    private void saveInvoicesToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(REPORT_FILE))) {
            oos.writeObject(invoices);
            System.out.println("\n[INFO] Laporan transaksi berhasil disimpan ke " + REPORT_FILE);
        } catch (IOException e) {
            System.out.println("\n[Error] Gagal menyimpan laporan transaksi ke file: " + e.getMessage());
        }
        generateReportTextFile();
    }

    // Memuat daftar transaksi dari Report.txt
    private void loadInvoicesFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(REPORT_FILE))) {
            invoices = (List<Invoice>) ois.readObject();
            System.out.println("\n[INFO] Data invoice berhasil dimuat dari " + REPORT_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("\n[INFO] File " + REPORT_FILE + " tidak ditemukan. Membuat daftar invoice baru.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\n[Error] Gagal memuat data invoice dari file: " + e.getMessage());
        }
    }

    // Membuat laporan penjualan berbentuk teks [cite: 60, 62]
    private void generateReportTextFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("Report_Summary.txt"))) {
            writer.println("=== Laporan Penjualan Festival Drama ===");
            writer.println("Tanggal Laporan: " + LocalDate.now());
            writer.println("----------------------------------------");

            double totalRevenue = invoices.stream().mapToDouble(Invoice::getTotalAmount).sum();
            writer.printf("Total Pendapatan: Rp %,.2f\n", totalRevenue); // [cite: 61]
            writer.println("----------------------------------------");

            // Jumlah barang terjual per kategori [cite: 61]
            Map<String, Integer> soldPerCategory = invoices.stream()
                    .collect(Collectors.groupingBy(
                            invoice -> invoice.getItem().getCategory(),
                            Collectors.summingInt(Invoice::getQuantity)
                    ));
            writer.println("Jumlah Barang Terjual per Kategori:");
            soldPerCategory.forEach((category, totalSold) ->
                    writer.printf("- %-15s: %d item\n", category, totalSold));
            writer.println("----------------------------------------");

            // Daftar lengkap semua transaksi [cite: 61]
            writer.println("Daftar Lengkap Semua Transaksi:");
            writer.println("| Tanggal         | Nama Barang                    | Jumlah     | Total Pembelian      | Kategori       |");
            writer.println("-------------------------------------------------------------------------------------------------------");
            invoices.forEach(invoice -> writer.println(invoice.printDetails()));
            writer.println("-------------------------------------------------------------------------------------------------------");

            System.out.println("\n[INFO] Laporan ringkas (Report_Summary.txt) berhasil dibuat.");
        } catch (IOException e) {
            System.out.println("\n[Error] Gagal membuat file laporan ringkas: " + e.getMessage());
        }
    }


    // --- MENU INTERFACE ---

    // Mencetak menu utama [cite: 143]
    public void printMenu() {
        System.out.println("\n=== Menu Kasir Mainkrep ===");
        System.out.println("1. Tambah Barang"); // [cite: 48]
        System.out.println("2. Lihat Semua Barang"); // [cite: 51]
        System.out.println("3. Edit Barang"); // [cite: 54]
        System.out.println("4. Hapus Barang"); // [cite: 54]
        System.out.println("5. Cari Barang"); // [cite: 56]
        System.out.println("6. Transaksi Pembelian"); // [cite: 58]
        System.out.println("7. Lihat Laporan Penjualan"); // [cite: 60]
        System.out.println("8. Keluar");
        System.out.println("---------------------------");
    }

    // Mencetak daftar barang lengkap [cite: 144]
    public void printListOfItem() {
        printListOfItem(null); // Panggil fungsi utama tanpa filter
    }

    // Mencetak daftar barang dengan filter [cite: 145]
    public void printListOfItem(String searchFilter) {
        // Filter barang jika ada filter yang diberikan
        List<Item> filteredItems = items;
        if (searchFilter != null && !searchFilter.trim().isEmpty()) {
            filteredItems = items.stream()
                    .filter(item -> item.getName().toLowerCase().contains(searchFilter.toLowerCase()) ||
                            item.getCategory().toLowerCase().contains(searchFilter.toLowerCase()))
                    .collect(Collectors.toList());
        }

        System.out.println("\n=== Daftar Barang Dijual ===");
        if (filteredItems.isEmpty()) {
            System.out.println("Tidak ada barang yang tersedia.");
            return;
        }

        System.out.println("| ID  | Nama Barang          | Kategori        | Harga          | Stok  | Detail Tambahan");
        System.out.println("---------------------------------------------------------------------------------------");
        // Gunakan Polymorphism: Item.printDetails() akan memanggil implementasi dari subclass (Ticket, Merchandise, Recording) [cite: 164]
        filteredItems.forEach(item -> System.out.println(item.printDetails())); // [cite: 52]
        System.out.println("---------------------------------------------------------------------------------------");
    }

    // Mendapatkan item berdasarkan ID [cite: 146]
    public Item getItemById(int id) {
        // Cari item dengan ID yang cocok
        return items.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // --- FITUR CRUD BARANG ---

    // Menu Tambah Barang [cite: 48, 147, 148]
    public void addItemMenu() {
        System.out.println("\n=== Tambah Barang ===");
        System.out.println("Pilih Kategori Barang:");
        System.out.println("1. Ticket");
        System.out.println("2. Merchandise");
        System.out.println("3. Recording");
        System.out.print("Pilihan (1-3): ");

        try {
            int categoryChoice = Integer.parseInt(scanner.nextLine());
            String name = "";
            double price = 0.0;
            int stock = 0;

            // Validasi Input Nama
            while (name.trim().isEmpty()) {
                System.out.print("Nama Barang: ");
                name = scanner.nextLine().trim();
                if (name.trim().isEmpty()) System.out.println("[Error] Nama barang tidak boleh kosong.");
            }

            // Validasi Input Harga
            while (price <= 0) {
                System.out.print("Harga: ");
                try {
                    price = Double.parseDouble(scanner.nextLine());
                    if (price <= 0) System.out.println("[Error] Harga harus lebih dari nol.");
                } catch (NumberFormatException e) {
                    System.out.println("[Error] Harga harus berupa angka.");
                }
            }

            // Validasi Input Stok
            while (stock <= 0) {
                System.out.print("Stok: ");
                try {
                    stock = Integer.parseInt(scanner.nextLine());
                    if (stock <= 0) System.out.println("[Error] Stok harus lebih dari nol.");
                } catch (NumberFormatException e) {
                    System.out.println("[Error] Stok harus berupa bilangan bulat.");
                }
            }


            Item newItem = null;
            switch (categoryChoice) {
                case 1: // Ticket [cite: 50]
                    LocalDate validUntil = null;
                    while (validUntil == null) {
                        System.out.print("Tanggal Kadaluarsa (YYYY-MM-DD): ");
                        try {
                            validUntil = LocalDate.parse(scanner.nextLine());
                            if (validUntil.isBefore(LocalDate.now())) {
                                System.out.println("[Error] Tanggal kadaluarsa tidak boleh di masa lalu.");
                                validUntil = null;
                            }
                        } catch (Exception e) {
                            System.out.println("[Error] Format tanggal salah. Gunakan YYYY-MM-DD.");
                        }
                    }
                    newItem = new Ticket(nextItemId, name, price, stock, validUntil);
                    break;
                case 2: // Merchandise [cite: 50]
                    System.out.print("Tipe Merchandise (Kaos/Poster/dll): ");
                    String type = scanner.nextLine();
                    newItem = new Merchandise(nextItemId, name, price, stock, type);
                    break;
                case 3: // Recording [cite: 50]
                    int duration = 0;
                    while (duration <= 0) {
                        System.out.print("Durasi (menit): ");
                        try {
                            duration = Integer.parseInt(scanner.nextLine());
                            if (duration <= 0) System.out.println("[Error] Durasi harus lebih dari nol.");
                        } catch (NumberFormatException e) {
                            System.out.println("[Error] Durasi harus berupa bilangan bulat.");
                        }
                    }
                    newItem = new Recording(nextItemId, name, price, stock, duration);
                    break;
                default:
                    System.out.println("[Error] Pilihan kategori tidak valid.");
                    return;
            }

            items.add(newItem);
            nextItemId++;
            saveItemsToFile(); // [cite: 55]
            System.out.println("\n[SUKSES] Barang " + name + " berhasil ditambahkan!");

        } catch (NumberFormatException e) {
            System.out.println("[Error] Input kategori harus berupa angka.");
        }
    }

    // Menu Edit Barang [cite: 54, 149, 150]
    public void editItemMenu() {
        printListOfItem();
        System.out.println("\n=== Edit Barang ===");
        try {
            System.out.print("Masukkan ID Barang yang akan diubah: ");
            int idToEdit = Integer.parseInt(scanner.nextLine());
            Item itemToEdit = getItemById(idToEdit);

            if (itemToEdit == null) {
                System.out.println("[Error] Barang dengan ID " + idToEdit + " tidak ditemukan.");
                return;
            }

            System.out.println("Mengedit: " + itemToEdit.getName());

            // Edit Nama
            System.out.printf("Nama (Kosongkan untuk tetap '%s'): ", itemToEdit.getName());
            String newName = scanner.nextLine().trim();
            if (!newName.isEmpty()) {
                itemToEdit.setName(newName);
            }

            // Edit Harga
            System.out.printf("Harga (Kosongkan untuk tetap '%,.2f'): ", itemToEdit.getPrice());
            String priceStr = scanner.nextLine().trim();
            if (!priceStr.isEmpty()) {
                try {
                    double newPrice = Double.parseDouble(priceStr);
                    if (newPrice > 0) {
                        itemToEdit.setPrice(newPrice);
                    } else {
                        System.out.println("[Warning] Harga harus > 0. Harga tidak diubah.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[Warning] Input harga tidak valid. Harga tidak diubah.");
                }
            }

            // Edit Stok
            System.out.printf("Stok (Kosongkan untuk tetap '%d'): ", itemToEdit.getStock());
            String stockStr = scanner.nextLine().trim();
            if (!stockStr.isEmpty()) {
                try {
                    int newStock = Integer.parseInt(stockStr);
                    if (newStock >= 0) {
                        itemToEdit.setStock(newStock);
                    } else {
                        System.out.println("[Warning] Stok harus >= 0. Stok tidak diubah.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[Warning] Input stok tidak valid. Stok tidak diubah.");
                }
            }

            // Edit Detail Spesifik (Contoh: ValidUntil untuk Ticket)
            if (itemToEdit instanceof Ticket) {
                Ticket ticket = (Ticket) itemToEdit;
                System.out.printf("Tanggal Kadaluarsa (Kosongkan untuk tetap '%s', format YYYY-MM-DD): ", ticket.getValidUntil().toString());
                String dateStr = scanner.nextLine().trim();
                if (!dateStr.isEmpty()) {
                    try {
                        LocalDate newDate = LocalDate.parse(dateStr);
                        if (newDate.isAfter(LocalDate.now())) {
                            ticket.setValidUntil(newDate);
                        } else {
                            System.out.println("[Warning] Tanggal kadaluarsa tidak boleh di masa lalu. Tanggal tidak diubah.");
                        }
                    } catch (Exception e) {
                        System.out.println("[Warning] Format tanggal salah. Tanggal tidak diubah.");
                    }
                }
            }

            // Anda dapat menambahkan logika edit untuk Merchandise (type) dan Recording (duration) di sini.

            saveItemsToFile(); // [cite: 55]
            System.out.println("\n[SUKSES] Barang ID " + idToEdit + " berhasil diubah!");

        } catch (NumberFormatException e) {
            System.out.println("[Error] Input ID harus berupa angka.");
        }
    }

    // Hapus Barang [cite: 54, 151]
    public void deleteItem() {
        printListOfItem();
        System.out.println("\n=== Hapus Barang ===");
        try {
            System.out.print("Masukkan ID Barang yang akan dihapus: ");
            int idToDelete = Integer.parseInt(scanner.nextLine());

            Item itemToRemove = getItemById(idToDelete);

            if (itemToRemove == null) {
                System.out.println("[Error] Barang dengan ID " + idToDelete + " tidak ditemukan.");
                return;
            }

            System.out.print("Yakin ingin menghapus barang " + itemToRemove.getName() + "? (y/n): ");
            String confirmation = scanner.nextLine().toLowerCase();

            if (confirmation.equals("y")) {
                items.remove(itemToRemove);
                saveItemsToFile(); // [cite: 55]
                System.out.println("\n[SUKSES] Barang " + itemToRemove.getName() + " berhasil dihapus!");
            } else {
                System.out.println("\n[INFO] Penghapusan dibatalkan.");
            }

        } catch (NumberFormatException e) {
            System.out.println("[Error] Input ID harus berupa angka.");
        }
    }

    // Pencarian Barang [cite: 56, 57, 152, 154]
    public void searchItems() {
        System.out.println("\n=== Cari Barang ===");
        System.out.print("Masukkan Nama atau Kategori Barang yang dicari: ");
        String searchInput = scanner.nextLine();

        if (searchInput.trim().isEmpty()) {
            System.out.println("[Warning] Input pencarian kosong. Menampilkan semua barang.");
            printListOfItem();
        } else {
            // Re-use printListOfItem dengan filter
            printListOfItem(searchInput);
        }
    }

    // --- FITUR TRANSAKSI ---

    // Transaksi Pembelian Barang [cite: 58, 155]
    public void buyItems() {
        printListOfItem();
        System.out.println("\n=== Transaksi Pembelian ===");

        try {
            System.out.print("Masukkan ID Barang yang dibeli: ");
            int id = Integer.parseInt(scanner.nextLine());
            Item itemToBuy = getItemById(id);

            if (itemToBuy == null) {
                System.out.println("[Error] Barang dengan ID " + id + " tidak ditemukan.");
                return;
            }

            // Verifikasi stok [cite: 59]
            System.out.print("Masukkan Jumlah Kuantitas yang dibeli: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            if (quantity <= 0) {
                System.out.println("[Error] Kuantitas pembelian harus lebih dari nol.");
                return;
            }

            if (quantity > itemToBuy.getStock()) {
                System.out.println("[Error] Stok tidak mencukupi. Stok tersedia: " + itemToBuy.getStock());
                return;
            }

            // Hitung Diskon [cite: 59, 72]
            double discountPercentage = itemToBuy.countDiscount(quantity);
            double originalPrice = itemToBuy.getPrice() * quantity;
            double discountAmount = originalPrice * discountPercentage;
            double totalAmount = originalPrice - discountAmount;

            // Tampilkan Detail
            System.out.println("\n--- Detail Pembelian ---");
            System.out.printf("Nama Barang: %s\n", itemToBuy.getName());
            System.out.printf("Harga Satuan: Rp %,.2f\n", itemToBuy.getPrice());
            System.out.printf("Kuantitas: %d\n", quantity);
            System.out.printf("Harga Awal: Rp %,.2f\n", originalPrice);
            System.out.printf("Diskon Otomatis (%.0f%%): Rp %,.2f\n", discountPercentage * 100, discountAmount);
            System.out.printf("Total Bayar: Rp %,.2f\n", totalAmount); // [cite: 59]
            System.out.println("------------------------");

            // Konfirmasi Pembelian
            System.out.print("Konfirmasi pembelian? (y/n): ");
            String confirm = scanner.nextLine().toLowerCase();

            if (confirm.equals("y")) {
                // Catat detail transaksi [cite: 59]
                Invoice newInvoice = new Invoice(itemToBuy, quantity, totalAmount, LocalDate.now());
                invoices.add(newInvoice);

                // Kurangi Stok [cite: 59]
                itemToBuy.minusStock(quantity);

                // Simpan perubahan
                saveItemsToFile();
                saveInvoicesToFile(); // [cite: 62]

                System.out.println("\n[SUKSES] Transaksi pembelian berhasil dicatat!");
            } else {
                System.out.println("\n[INFO] Transaksi dibatalkan.");
            }


        } catch (NumberFormatException e) {
            System.out.println("[Error] Input ID dan Kuantitas harus berupa angka.");
        }
    }

    // Mencetak Laporan Penjualan [cite: 60, 156]
    public void printInvoices() {
        System.out.println("\n=== Laporan Penjualan ===");

        if (invoices.isEmpty()) {
            System.out.println("Belum ada transaksi yang tercatat.");
            return;
        }

        // Hitung total pendapatan [cite: 61]
        double totalRevenue = invoices.stream()
                .mapToDouble(Invoice::getTotalAmount)
                .sum();

        System.out.printf("Total Pendapatan: Rp %,.2f\n", totalRevenue);

        // Cetak daftar transaksi [cite: 61]
        System.out.println("\n| Tanggal         | Nama Barang                    | Jumlah     | Total Pembelian      | Kategori       |");
        System.out.println("-------------------------------------------------------------------------------------------------------");
        invoices.forEach(invoice -> System.out.println(invoice.printDetails()));
        System.out.println("-------------------------------------------------------------------------------------------------------");

        System.out.println("\nLaporan detail juga disimpan di Report_Summary.txt.");
    }
}