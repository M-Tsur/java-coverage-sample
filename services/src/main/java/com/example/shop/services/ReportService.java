package com.example.shop.services;

import com.example.shop.common.StringUtils;
import com.example.shop.domain.Money;
import com.example.shop.domain.Order;
import com.example.shop.domain.OrderLine;
import com.example.shop.domain.OrderStatus;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** Builds sales reports from orders. */
public class ReportService {

    private final OrderService orders;

    public ReportService(OrderService orders) {
        this.orders = orders;
    }

    private List<Order> billable() {
        List<Order> result = new ArrayList<>();
        for (Order o : orders.all()) {
            if (o.status() != OrderStatus.CANCELLED) {
                result.add(o);
            }
        }
        return result;
    }

    public Money totalRevenue() {
        Money sum = Money.zero("USD");
        for (Order o : billable()) {
            sum = sum.add(orders.totalFor(o));
        }
        return sum;
    }

    public Map<String, Money> revenueByCategory() {
        Map<String, Money> result = new TreeMap<>();
        for (Order o : billable()) {
            for (OrderLine line : o.lines()) {
                result.merge(line.product().category(), line.total(), Money::add);
            }
        }
        return result;
    }

    public List<String> topCustomers(int limit) {
        Map<String, Money> spend = new TreeMap<>();
        for (Order o : billable()) {
            spend.merge(o.customer().name(), orders.totalFor(o), Money::add);
        }
        List<Map.Entry<String, Money>> entries = new ArrayList<>(spend.entrySet());
        entries.sort(Map.Entry.<String, Money>comparingByValue(Comparator.reverseOrder()));
        List<String> names = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, entries.size()); i++) {
            names.add(entries.get(i).getKey());
        }
        return names;
    }

    public Map<OrderStatus, Integer> countByStatus() {
        Map<OrderStatus, Integer> result = new TreeMap<>();
        for (Order o : orders.all()) {
            result.merge(o.status(), 1, Integer::sum);
        }
        return result;
    }

    public String summary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Orders: ").append(orders.all().size()).append('\n');
        sb.append("Revenue: ").append(totalRevenue()).append('\n');
        for (Map.Entry<String, Money> e : revenueByCategory().entrySet()) {
            sb.append("  ").append(StringUtils.padRight(StringUtils.capitalize(e.getKey()), 12, ' '))
                    .append(StringUtils.padLeft(e.getValue().amount().toPlainString(), 10, ' ')).append('\n');
        }
        return sb.toString();
    }

    public String toCsv() {
        StringBuilder sb = new StringBuilder("orderId,customer,status,total\n");
        for (Order o : orders.all()) {
            sb.append(StringUtils.csvEscape(o.id())).append(',')
                    .append(StringUtils.csvEscape(o.customer().name())).append(',')
                    .append(o.status()).append(',')
                    .append(orders.totalFor(o).amount().toPlainString()).append('\n');
        }
        return sb.toString();
    }
}
