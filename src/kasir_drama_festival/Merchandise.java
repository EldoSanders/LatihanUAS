package kasir_drama_festival;

import java.io.Serializable;
// Extends Item [cite: 120]
public class Merchandise extends Item implements Serializable {
    // Atribut [cite: 123]
    private String type; // Contoh: Kaos, Poster

    // Constructor
    public Merchandise(int id, String name, double price, int stock, String type) {
        super(id, name, price, stock, "Merchandise");
        this.type = type;
    }

    // Getters and Setters [cite: 129, 130]

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Implementasi Printable [cite: 130]
    @Override
    public String printDetails() {
        return String.format("| %-3d | %-20s | %-15s | Rp %,10.2f | %-5d | Tipe: %s",
                getId(), getName(), getCategory(), getPrice(), getStock(), type);
    }
}