package kasir_drama_festival;

import java.io.Serializable;
import java.util.Objects;
// Implementasi Serializable untuk I/O File
// Mengimplementasikan Discountable karena semua Item bisa saja memiliki diskon, tetapi hanya Ticket yang overriding implementasi di Item
// Implementasi Printable untuk memudahkan pencetakan detail
public abstract class Item implements Printable, Discountable, Serializable {
    // Atribut [cite: 107]
    private int id;
    private String name;
    private double price;
    private int stock;
    private String category;

    // Constructor
    public Item(int id, String name, double price, int stock, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    // Getters and Setters [cite: 108-118]

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Business Logic Methods [cite: 118, 119]

    // Mengurangi stok
    public void minusStock(int amount) {
        this.stock -= amount;
    }

    // Menambah stok
    public void addStock(int amount) {
        this.stock += amount;
    }

    // Implementasi default dari Discountable. Diskon 10% untuk > 3 item [cite: 74]
    @Override
    public double countDiscount(int quantity) {
        // Diskon 10% untuk pembelian lebih dari 3 item [cite: 74]
        if (quantity > 3) {
            return 0.10; // 10%
        }
        return 0.0; // 0%
    }

    // Abstract method dari Printable diimplementasikan di subclass
    // @Override
    // public abstract String printDetails();

    // Override equals dan hashCode untuk pencarian/penghapusan yang lebih baik
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        // Item dianggap sama jika ID-nya sama
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}