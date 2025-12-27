package kasir_drama_festival;

import java.io.Serializable;

public class Recording extends Item implements Serializable {

    private int durationInMinutes;

    public Recording(int id, String name, double price, int stock, int durationInMinutes) {
        super(id, name, price, stock, "Recording");
        this.durationInMinutes = durationInMinutes;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    @Override
    public String printDetails() {
        return String.format(
                "| %-3d | %-20s | %-15s | Rp %,10.2f | %-5d | Durasi: %d menit",
                getId(), getName(), getCategory(), getPrice(), getStock(), durationInMinutes
        );
    }
}
