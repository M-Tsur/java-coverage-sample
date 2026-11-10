package com.example.shop.domain;

/** Loyalty tiers with their discount percentage. */
public enum CustomerTier {
    BRONZE(0), SILVER(5), GOLD(10), PLATINUM(15);

    private final int discountPercent;

    CustomerTier(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int discountPercent() {
        return discountPercent;
    }

    public CustomerTier upgrade() {
        CustomerTier[] all = values();
        return this == all[all.length - 1] ? this : all[ordinal() + 1];
    }

    public CustomerTier downgrade() {
        return this == BRONZE ? this : values()[ordinal() - 1];
    }
}
