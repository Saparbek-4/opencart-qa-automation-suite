package com.opencart.api.enums;

public enum AddressType {
    GUEST_BILLING("guest"),
    GUEST_SHIPPING("guest_shipping"),
    AUTH_BILLING("payment_address"),
    AUTH_SHIPPING("shipping_address");

    private final String route;

    AddressType(String route) { this.route = route; }

    public String getRoute() { return route; }
}

