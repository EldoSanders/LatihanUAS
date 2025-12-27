package kasir_drama_festival;

import java.io.Serializable;
import java.util.Objects;

public abstract class Item implements Printable, Discountable, Serializable {

    private int id;
    private String name;
    private double price;
    private int stock;
    private String category;

    public Item(int id, String name, double price, int stock, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

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

    public void minusStock(int amount) {
        this.stock -= amount;
    }

    public void addStock(int amount) {
        this.stock += amount;
    }

    @Override
    public double countDiscount(int quantity) {
        if (quantity > 3) {
            return 0.10;
        }
        return 0.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
