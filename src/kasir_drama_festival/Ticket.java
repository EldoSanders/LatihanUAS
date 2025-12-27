package kasir_drama_festival;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Random;

public class Ticket extends Item implements Serializable {

    private LocalDate validUntil;

    public Ticket(int id, String name, double price, int stock, LocalDate validUntil) {
        super(id, name, price, stock, "Ticket");
        this.validUntil = validUntil;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    @Override
    public String printDetails() {
        return String.format(
                "| %-3d | %-20s | %-15s | Rp %,10.2f | %-5d | Berlaku S/D: %s",
                getId(), getName(), getCategory(), getPrice(), getStock(), validUntil.toString()
        );
    }

    @Override
    public double countDiscount(int quantity) {
        double defaultDiscount = super.countDiscount(quantity);

        LocalDate today = LocalDate.now();
        MonthDay currentMonthDay = MonthDay.from(today);
        MonthDay flashSaleMonthDay = MonthDay.of(5, 5);
        MonthDay flashSale2MonthDay = MonthDay.of(11, 11);

        if (currentMonthDay.equals(flashSaleMonthDay) || currentMonthDay.equals(flashSale2MonthDay)) {
            Random random = new Random();
            double flashSaleDiscount = 0.05 + (0.15 * random.nextDouble());
            return Math.max(defaultDiscount, flashSaleDiscount);
        }

        return defaultDiscount;
    }
}
