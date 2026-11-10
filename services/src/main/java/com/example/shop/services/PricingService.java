package com.example.shop.services;

import com.example.shop.common.MathUtils;
import com.example.shop.domain.Customer;
import com.example.shop.domain.Money;
import com.example.shop.domain.Order;
import java.math.BigDecimal;

/** Computes discounts, tax and final totals for orders. */
public class PricingService {

    public static final int BULK_THRESHOLD = 10;
    public static final int BULK_DISCOUNT_PERCENT = 5;

    private final BigDecimal taxRatePercent;

    public PricingService(BigDecimal taxRatePercent) {
        if (taxRatePercent.signum() < 0) {
            throw new IllegalArgumentException("tax rate must not be negative");
        }
        this.taxRatePercent = taxRatePercent;
    }

    public Money tierDiscount(Customer customer, Money subtotal) {
        return subtotal.percent(BigDecimal.valueOf(customer.tier().discountPercent()));
    }

    public Money bulkDiscount(Order order) {
        if (order.itemCount() >= BULK_THRESHOLD) {
            return order.subtotal().percent(BigDecimal.valueOf(BULK_DISCOUNT_PERCENT));
        }
        return Money.zero(order.subtotal().currency());
    }

    public Money totalDiscount(Order order) {
        Money subtotal = order.subtotal();
        Money discount = tierDiscount(order.customer(), subtotal).add(bulkDiscount(order));
        // never discount more than 30% of the subtotal
        Money cap = subtotal.percent(BigDecimal.valueOf(30));
        return discount.compareTo(cap) > 0 ? cap : discount;
    }

    public Money tax(Money taxable) {
        return taxable.percent(taxRatePercent);
    }

    public Money finalTotal(Order order) {
        Money afterDiscount = order.subtotal().subtract(totalDiscount(order));
        return afterDiscount.add(tax(afterDiscount));
    }

    public Money splitEvenly(Money total, int parts) {
        if (parts <= 0) {
            throw new IllegalArgumentException("parts must be positive");
        }
        BigDecimal each = total.amount().divide(BigDecimal.valueOf(parts), 2, java.math.RoundingMode.HALF_UP);
        return Money.of(MathUtils.roundHalfUp(each, 2).toPlainString(), total.currency());
    }
}
