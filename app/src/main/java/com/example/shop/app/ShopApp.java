package com.example.shop.app;

import com.example.shop.domain.Customer;
import com.example.shop.domain.CustomerTier;
import com.example.shop.domain.Money;
import com.example.shop.domain.Order;
import com.example.shop.domain.Product;
import com.example.shop.services.InventoryService;
import com.example.shop.services.OrderService;
import com.example.shop.services.PricingService;
import com.example.shop.services.ReportService;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/** Wires all services together and runs a small demo scenario. */
public class ShopApp {

    private final InventoryService inventory = new InventoryService();
    private final OrderService orders = new OrderService(inventory, new PricingService(new BigDecimal("8.5")));
    private final ReportService reports = new ReportService(orders);

    public ShopApp() {
        seed();
    }

    private void seed() {
        inventory.add("LAP-0001", 5);
        inventory.add("MOU-0002", 50);
        inventory.add("BOK-0003", 20);
    }

    public Product laptop() {
        return new Product("LAP-0001", "Laptop", Money.usd("1200.00"), "electronics");
    }

    public Product mouse() {
        return new Product("MOU-0002", "Mouse", Money.usd("25.00"), "electronics");
    }

    public Product book() {
        return new Product("BOK-0003", "Java Book", Money.usd("40.00"), "books");
    }

    public String runDemo() {
        Customer alice = new Customer("c1", "alice smith", "alice@example.com", CustomerTier.GOLD);
        Customer bob = new Customer("c2", "bob jones", "bob@example.com", CustomerTier.BRONZE);

        Map<Product, Integer> first = new LinkedHashMap<>();
        first.put(laptop(), 1);
        first.put(mouse(), 2);
        Order o1 = orders.place(alice, first);

        Map<Product, Integer> second = new LinkedHashMap<>();
        second.put(book(), 3);
        Order o2 = orders.place(bob, second);

        orders.pay(o1.id());
        orders.ship(o1.id());
        orders.cancel(o2.id());

        return reports.summary();
    }

    public InventoryService inventory() {
        return inventory;
    }

    public OrderService orders() {
        return orders;
    }

    public ReportService reports() {
        return reports;
    }
}
