package com.opencart.api.clients;

public class ApiRoutes {
    public static final String BASE_URI = "http://opencart.abstracta.us";
    public static final String LOGIN = "/index.php?route=account/login";
    public static final String LOGOUT = "/index.php?route=account/logout";
    public static final String REMOVE_CART = "/index.php?route=checkout/cart/remove";
    public static final String HOME = "/index.php?route=common/home";
    public static final String CART = "/index.php?route=checkout/cart";
    public static final String ADD_CART = "/index.php?route=checkout/cart/add";
    public static final String EDIT_CART = "/index.php?route=checkout/cart/edit";
    public static final String GUEST_CHECKOUT = "/index.php?route=checkout/guest";
    public static final String AUTH_CHECKOUT = "/index.php?route=checkout/checkout";
    public static final String ADDRESS_CHECKOUT = "/index.php?route=checkout/%s/save";
    // Shipping Method Routes
    public static final String SHIPPING_METHOD_GET = "/index.php?route=checkout/shipping_method";
    public static final String SHIPPING_METHOD_SAVE = "/index.php?route=checkout/shipping_method/save";
    // Shipping Method Routes
    public static final String PAYMENT_METHOD_GET = "/index.php?route=checkout/payment_method";
    public static final String PAYMENT_METHOD_SAVE = "/index.php?route=checkout/payment_method/save";
    // Order routes
    public static final String CONFIRM_ORDER = "/index.php?route=checkout/confirm";
    public static final String PLACE_ORDER = "/index.php?route=checkout/success";
}
