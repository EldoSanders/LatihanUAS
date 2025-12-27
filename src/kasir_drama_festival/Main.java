package kasir_drama_festival;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private Scanner scanner;
    private List<Item> items;
    private List<Invoice> invoices;
    private final String ITEMS_FILE = "Items.txt";
    private final String REPORT_FILE = "Report.txt";
    private int nextItemId = 1;

    public Main() {
        scanner = new Scanner(System.in);
        items = new ArrayList<>();
        invoices = new ArrayList<>();
        loadItemsFromFile();
        loadInvoicesFromFile();

        if (items.isEmpty()) {
            seedItems();
            saveItemsToFile();
        } else {
            nextItemId = items.stream().mapToInt(Item::getId).max().orElse(0) + 1;
        }
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                System.out.print("Pilih menu (1-7): ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> addItemMenu();
                    case 2 -> printListOfItem();
                    case 3 -> editItemMenu();
                    case 4 -> deleteItem();
                    case 5 -> searchItems();
                    case 6 -> buyItems();
                    case 7 -> printInvoices();
                    case 8 -> {
                        System.out.println("\nProgram Kasir Selesai. Terima kasih.");
                        running = false;
                    }
                    default ->
                            System.out.println("\n[Error] Pilihan tidak valid. Mohon masukkan angka 1-8.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n[Error] Input harus berupa angka. Silakan coba lagi.");
            } catch (Exception e) {
                System.out.println("\n[Error] Terjadi kesalahan: " + e.getMessage());
            }
            if (running) {
                System.out.print("\nTekan ENTER untuk melanjutkan...");
                scanner.nextLine();
            }
        }
    }

    public void seedItems() {
        items.add(new Ticket(nextItemId++, "Pertunjukan Hamlet", 150000.0, 50, LocalDate.now().plusDays(10)));
        items.add(new Merchandise(nextItemId++, "Kaos Teater", 85000.0, 100, "Kaos"));
        items.add(new Recording(nextItemId++, "Rekaman Pertunjukan Romeo", 120000.0, 30, 150));
        items.add(new Ticket(nextItemId++, "Meet & Greet Aktor", 250000.0, 20, LocalDate.now().plusDays(5)));
        items.add(new Merchandise(nextItemId++, "Poster Limited Edition", 45000.0, 75, "Poster"));
    }

    private void saveItemsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ITEMS_FILE))) {
            oos.writeObject(items);
        } catch (IOException e) {
            System.out.println("\n[Error] Gagal menyimpan data barang: " + e.getMessage());
        }
    }

    private void loadItemsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ITEMS_FILE))) {
            items = (List<Item>) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\n[Error] Gagal memuat data barang: " + e.getMessage());
        }
    }

    private void saveInvoicesToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(REPORT_FILE))) {
            oos.writeObject(invoices);
        } catch (IOException e) {
            System.out.println("\n[Error] Gagal menyimpan laporan: " + e.getMessage());
        }
        generateReportTextFile();
    }

    private void loadInvoicesFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(REPORT_FILE))) {
            invoices = (List<Invoice>) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\n[Error] Gagal memuat invoice: " + e.getMessage());
        }
    }

    private void generateReportTextFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("Report_Summary.txt"))) {
            writer.println("=== Laporan Penjualan Festival Drama ===");
            writer.println("Tanggal Laporan: " + LocalDate.now());

            double totalRevenue = invoices.stream().mapToDouble(Invoice::getTotalAmount).sum();
            writer.printf("Total Pendapatan: Rp %,.2f\n", totalRevenue);

            Map<String, Integer> soldPerCategory = invoices.stream()
                    .collect(Collectors.groupingBy(
                            invoice -> invoice.getItem().getCategory(),
                            Collectors.summingInt(Invoice::getQuantity)
                    ));

            soldPerCategory.forEach((category, totalSold) ->
                    writer.printf("%s: %d\n", category, totalSold));

            invoices.forEach(invoice -> writer.println(invoice.printDetails()));
        } catch (IOException e) {
            System.out.println("\n[Error] Gagal membuat laporan ringkas: " + e.getMessage());
        }
    }

    public void printMenu() {
        System.out.println("\n=== Menu Kasir Mainkrep ===");
        System.out.println("1. Tambah Barang");
        System.out.println("2. Lihat Semua Barang");
        System.out.println("3. Edit Barang");
        System.out.println("4. Hapus Barang");
        System.out.println("5. Cari Barang");
        System.out.println("6. Transaksi Pembelian");
        System.out.println("7. Lihat Laporan Penjualan");
        System.out.println("8. Keluar");
    }

    public void printListOfItem() {
        printListOfItem(null);
    }

    public void printListOfItem(String filter) {
        List<Item> result = items;
        if (filter != null && !filter.isEmpty()) {
            result = items.stream()
                    .filter(i -> i.getName().toLowerCase().contains(filter.toLowerCase())
                            || i.getCategory().toLowerCase().contains(filter.toLowerCase()))
                    .collect(Collectors.toList());
        }

        result.forEach(item -> System.out.println(item.printDetails()));
    }

    public Item getItemById(int id) {
        return items.stream().filter(i -> i.getId() == id).findFirst().orElse(null);
    }

    public void addItemMenu() {
        System.out.print("Kategori (1 Ticket, 2 Merchandise, 3 Recording): ");
        int choice = Integer.parseInt(scanner.nextLine());

        System.out.print("Nama: ");
        String name = scanner.nextLine();

        System.out.print("Harga: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Stok: ");
        int stock = Integer.parseInt(scanner.nextLine());

        Item item = null;

        if (choice == 1) {
            System.out.print("Tanggal Kadaluarsa (YYYY-MM-DD): ");
            item = new Ticket(nextItemId, name, price, stock, LocalDate.parse(scanner.nextLine()));
        } else if (choice == 2) {
            System.out.print("Tipe: ");
            item = new Merchandise(nextItemId, name, price, stock, scanner.nextLine());
        } else if (choice == 3) {
            System.out.print("Durasi: ");
            item = new Recording(nextItemId, name, price, stock, Integer.parseInt(scanner.nextLine()));
        }

        if (item != null) {
            items.add(item);
            nextItemId++;
            saveItemsToFile();
        }
    }

    public void editItemMenu() {
        printListOfItem();
        System.out.print("ID Barang: ");
        int id = Integer.parseInt(scanner.nextLine());
        Item item = getItemById(id);
        if (item == null) return;

        System.out.print("Nama baru: ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) item.setName(name);

        saveItemsToFile();
    }

    public void deleteItem() {
        printListOfItem();
        System.out.print("ID Barang: ");
        int id = Integer.parseInt(scanner.nextLine());
        Item item = getItemById(id);
        if (item != null) {
            items.remove(item);
            saveItemsToFile();
        }
    }

    public void searchItems() {
        System.out.print("Cari: ");
        printListOfItem(scanner.nextLine());
    }

    public void buyItems() {
        printListOfItem();
        System.out.print("ID Barang: ");
        int id = Integer.parseInt(scanner.nextLine());
        Item item = getItemById(id);
        if (item == null) return;

        System.out.print("Jumlah: ");
        int qty = Integer.parseInt(scanner.nextLine());
        if (qty > item.getStock()) return;

        double total = item.getPrice() * qty;
        invoices.add(new Invoice(item, qty, total, LocalDate.now()));
        item.minusStock(qty);

        saveItemsToFile();
        saveInvoicesToFile();
    }

    public void printInvoices() {
        invoices.forEach(i -> System.out.println(i.printDetails()));
    }
}
