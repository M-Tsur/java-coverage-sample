package com.example.shop.domain;

import com.example.shop.common.StringUtils;
import com.example.shop.common.Validator;

/** A registered customer. */
public final class Customer {

    private final String id;
    private final String name;
    private final String email;
    private CustomerTier tier;

    public Customer(String id, String name, String email, CustomerTier tier) {
        this.id = Validator.requireNonBlank(id, "id");
        this.name = StringUtils.toTitleCase(Validator.requireNonBlank(name, "name"));
        this.email = Validator.requireValidEmail(email);
        this.tier = tier == null ? CustomerTier.BRONZE : tier;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String email() {
        return email;
    }

    public CustomerTier tier() {
        return tier;
    }

    public void upgrade() {
        tier = tier.upgrade();
    }

    public void downgrade() {
        tier = tier.downgrade();
    }
}
