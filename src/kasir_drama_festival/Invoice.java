package kasir_drama_festival;

import java.io.Serializable;
import java.time.LocalDate;
// Implementasi Printable [cite: 105]
public class Invoice implements Printable, Serializable {
    // Atribut [cite: 89, 90]
    private Item item;
    private int quantity;
    private double totalAmount;
    private LocalDate purchasedDate;

    // Constructor [cite: 91-94]
    public Invoice(Item item, int quantity, double totalAmount, LocalDate purchasedDate) {
        this.item = item;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.purchasedDate = purchasedDate;
    }

    // Getters and Setters [cite: 95-104]

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(LocalDate purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    // Implementasi Printable [cite: 105]
    @Override
    public String printDetails() {
        return String.format("| %-15s | %-30s | %-10d | Rp %,15.2f | %s",
                purchasedDate.toString(), item.getName(), quantity, totalAmount, item.getCategory());
    }
}