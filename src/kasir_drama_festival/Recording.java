package kasir_drama_festival;

import java.io.Serializable;
// Extends Item [cite: 121]
public class Recording extends Item implements Serializable {
    // Atribut [cite: 125]
    private int durationInMinutes;

    // Constructor
    public Recording(int id, String name, double price, int stock, int durationInMinutes) {
        super(id, name, price, stock, "Recording");
        this.durationInMinutes = durationInMinutes;
    }

    // Getters and Setters [cite: 131]

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    // Implementasi Printable [cite: 132]
    @Override
    public String printDetails() {
        return String.format("| %-3d | %-20s | %-15s | Rp %,10.2f | %-5d | Durasi: %d menit",
                getId(), getName(), getCategory(), getPrice(), getStock(), durationInMinutes);
    }
}