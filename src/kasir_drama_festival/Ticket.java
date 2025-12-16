package kasir_drama_festival;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Random;
// Extends Item [cite: 126]
public class Ticket extends Item implements Serializable {
    // Atribut [cite: 128]
    private LocalDate validUntil;

    // Constructor
    public Ticket(int id, String name, double price, int stock, LocalDate validUntil) {
        super(id, name, price, stock, "Ticket");
        this.validUntil = validUntil;
    }

    // Getters and Setters [cite: 133]

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    // Implementasi Printable [cite: 135]
    @Override
    public String printDetails() {
        return String.format("| %-3d | %-20s | %-15s | Rp %,10.2f | %-5d | Berlaku S/D: %s",
                getId(), getName(), getCategory(), getPrice(), getStock(), validUntil.toString());
    }

    // Override Discountable untuk Diskon Flash Sale [cite: 75, 134]
    @Override
    public double countDiscount(int quantity) {
        // Panggil diskon default (10% untuk > 3 item) [cite: 74]
        double defaultDiscount = super.countDiscount(quantity);

        // Diskon Flash Sale pada tanggal dan bulan yang sama [cite: 75]
        LocalDate today = LocalDate.now();
        MonthDay currentMonthDay = MonthDay.from(today);
        MonthDay flashSaleMonthDay = MonthDay.of(5, 5); // Contoh: 5 Mei
        MonthDay flashSale2MonthDay = MonthDay.of(11, 11); // Contoh: 11 November

        if (currentMonthDay.equals(flashSaleMonthDay) || currentMonthDay.equals(flashSale2MonthDay)) {
            // Diskon Flash Sale dengan persentase random [cite: 75]
            Random random = new Random();
            // Diskon antara 5% hingga 20% (contoh)
            double flashSaleDiscount = 0.05 + (0.15 * random.nextDouble());
            // Ambil diskon maksimum antara diskon default dan Flash Sale
            return Math.max(defaultDiscount, flashSaleDiscount);
        }

        return defaultDiscount;
    }
}