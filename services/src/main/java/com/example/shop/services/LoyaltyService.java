package com.example.shop.services;

import com.example.shop.domain.Customer;
import com.example.shop.domain.CustomerTier;
import com.example.shop.domain.Money;
import java.util.HashMap;
import java.util.Map;

/** Tracks loyalty points and promotes customers between tiers. */
public class LoyaltyService {

    public static final int SILVER_AT = 500;
    public static final int GOLD_AT = 2000;
    public static final int PLATINUM_AT = 5000;

    private final Map<String, Integer> points = new HashMap<>();

    public int accrue(Customer customer, Money spent) {
        int earned = spent.amount().intValue() / 10;
        int total = points.merge(customer.id(), earned, Integer::sum);
        return total;
    }

    public int balance(Customer customer) {
        return points.getOrDefault(customer.id(), 0);
    }

    public boolean redeem(Customer customer, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        int current = balance(customer);
        if (current < amount) {
            return false;
        }
        points.put(customer.id(), current - amount);
        return true;
    }

    public CustomerTier tierFor(int totalPoints) {
        if (totalPoints >= PLATINUM_AT) {
            return CustomerTier.PLATINUM;
        } else if (totalPoints >= GOLD_AT) {
            return CustomerTier.GOLD;
        } else if (totalPoints >= SILVER_AT) {
            return CustomerTier.SILVER;
        }
        return CustomerTier.BRONZE;
    }

    public boolean reevaluate(Customer customer) {
        CustomerTier target = tierFor(balance(customer));
        boolean changed = false;
        while (customer.tier().ordinal() < target.ordinal()) {
            customer.upgrade();
            changed = true;
        }
        while (customer.tier().ordinal() > target.ordinal()) {
            customer.downgrade();
            changed = true;
        }
        return changed;
    }

    public Map<String, Integer> leaderboard(int limit) {
        Map<String, Integer> result = new java.util.LinkedHashMap<>();
        points.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .forEach(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    public void expireAll(int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("percent out of range");
        }
        for (Map.Entry<String, Integer> e : points.entrySet()) {
            e.setValue(e.getValue() - e.getValue() * percent / 100);
        }
    }
}
