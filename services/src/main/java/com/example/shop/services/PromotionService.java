package com.example.shop.services;

import com.example.shop.domain.Money;
import com.example.shop.domain.Order;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/** Promo codes: percent-off, fixed-amount and free-shipping style promotions. */
public class PromotionService {

    public enum Kind { PERCENT, FIXED, MIN_SPEND }

    public static final class Promo {
        final Kind kind;
        final BigDecimal value;
        final BigDecimal minSpend;

        Promo(Kind kind, BigDecimal value, BigDecimal minSpend) {
            this.kind = kind;
            this.value = value;
            this.minSpend = minSpend;
        }
    }

    private final Map<String, Promo> promos = new HashMap<>();
    private final Set<String> usedKeys = new HashSet<>();

    public void registerPercent(String code, int percent) {
        if (percent <= 0 || percent > 100) {
            throw new IllegalArgumentException("percent out of range");
        }
        promos.put(code.toUpperCase(Locale.ROOT), new Promo(Kind.PERCENT, BigDecimal.valueOf(percent), null));
    }

    public void registerFixed(String code, String amount) {
        promos.put(code.toUpperCase(Locale.ROOT), new Promo(Kind.FIXED, new BigDecimal(amount), null));
    }

    public void registerMinSpend(String code, String amount, String minSpend) {
        promos.put(code.toUpperCase(Locale.ROOT),
                new Promo(Kind.MIN_SPEND, new BigDecimal(amount), new BigDecimal(minSpend)));
    }

    public boolean isKnown(String code) {
        return code != null && promos.containsKey(code.toUpperCase(Locale.ROOT));
    }

    public Money discountFor(String code, Order order) {
        Money subtotal = order.subtotal();
        if (!isKnown(code)) {
            return Money.zero(subtotal.currency());
        }
        Promo promo = promos.get(code.toUpperCase(Locale.ROOT));
        switch (promo.kind) {
            case PERCENT:
                return subtotal.percent(promo.value);
            case FIXED: {
                Money fixed = Money.of(promo.value.toPlainString(), subtotal.currency());
                return fixed.compareTo(subtotal) > 0 ? subtotal : fixed;
            }
            case MIN_SPEND: {
                if (subtotal.amount().compareTo(promo.minSpend) < 0) {
                    return Money.zero(subtotal.currency());
                }
                return Money.of(promo.value.toPlainString(), subtotal.currency());
            }
            default:
                throw new IllegalStateException("unreachable");
        }
    }

    public boolean redeemOnce(String code, String customerId) {
        if (!isKnown(code)) {
            return false;
        }
        return usedKeys.add(code.toUpperCase(Locale.ROOT) + ":" + customerId);
    }

    public Set<String> codes() {
        return new java.util.TreeSet<>(promos.keySet());
    }

    public void remove(String code) {
        promos.remove(code.toUpperCase(Locale.ROOT));
    }

    public String describe(String code) {
        if (!isKnown(code)) {
            return "unknown promo";
        }
        Promo p = promos.get(code.toUpperCase(Locale.ROOT));
        switch (p.kind) {
            case PERCENT:
                return p.value + "% off";
            case FIXED:
                return "$" + p.value + " off";
            default:
                return "$" + p.value + " off when spending $" + p.minSpend;
        }
    }
}
