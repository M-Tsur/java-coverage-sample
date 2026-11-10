package com.example.shop.services;

import com.example.shop.common.Validator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** In-memory stock tracking keyed by SKU. */
public class InventoryService {

    private final Map<String, Integer> stock = new HashMap<>();

    public void add(String sku, int quantity) {
        Validator.requireValidSku(sku);
        Validator.requirePositive(quantity, "quantity");
        stock.merge(sku, quantity, Integer::sum);
    }

    public int available(String sku) {
        return stock.getOrDefault(sku, 0);
    }

    public boolean has(String sku, int quantity) {
        return available(sku) >= quantity;
    }

    public void remove(String sku, int quantity) {
        Validator.requirePositive(quantity, "quantity");
        int current = available(sku);
        if (current < quantity) {
            throw new InsufficientStockException(sku, quantity, current);
        }
        if (current == quantity) {
            stock.remove(sku);
        } else {
            stock.put(sku, current - quantity);
        }
    }

    public List<String> lowStock(int threshold) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> e : new TreeMap<>(stock).entrySet()) {
            if (e.getValue() <= threshold) {
                result.add(e.getKey());
            }
        }
        return result;
    }

    public int totalUnits() {
        return stock.values().stream().mapToInt(Integer::intValue).sum();
    }

    public Map<String, Integer> snapshot() {
        return Collections.unmodifiableMap(new TreeMap<>(stock));
    }
}
