package kasir_drama_festival;

import java.io.Serializable;

public class Merchandise extends Item implements Serializable {

    private String type;

    public Merchandise(int id, String name, double price, int stock, String type) {
        super(id, name, price, stock, "Merchandise");
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String printDetails() {
        return String.format(
                "| %-3d | %-20s | %-15s | Rp %,10.2f | %-5d | Tipe: %s",
                getId(), getName(), getCategory(), getPrice(), getStock(), type
        );
    }
}
