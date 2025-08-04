package com.opencart.api.clients;

import com.opencart.api.enums.AddressType;
import com.opencart.utils.SpecFactory;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.response.Response;

public class CheckoutApi {

    private final CookieFilter cookieFilter;

    public CheckoutApi(CookieFilter cookieFilter) {
        this.cookieFilter = cookieFilter;
    }

    public Response initGuestCheckout() {
        return SpecFactory
                .getRequestSpec(cookieFilter)
                .post(ApiRoutes.GUEST_CHECKOUT)
                .then()
                .spec(SpecFactory.htmlResponseWithStatus(200))
                .extract().response();
    }

    public Response initAuthCheckout() {
        return SpecFactory
                .getRequestSpec(cookieFilter)
                .post(ApiRoutes.AUTH_CHECKOUT)
                .then()
                .spec(SpecFactory.htmlResponseWithStatus(200))
                .extract().response();
    }

    public Response fillAddressDetails(AddressType type, String firstName, String lastName, String email, String telephone,
                                       String address1, String city, String postcode,
                                       String country_id, String region_id) {

        return SpecFactory
                .getFormRequestSpec(cookieFilter)
                .formParam("firstname", firstName)
                .formParam("lastname", lastName)
                .formParam("email", email)
                .formParam("telephone", telephone)
                .formParam("address_1", address1)
                .formParam("city", city)
                .formParam("postcode", postcode)
                .formParam("country_id", country_id)
                .formParam("zone_id", region_id)
                .post(String.format(ApiRoutes.ADDRESS_CHECKOUT, type.getRoute()));
    }

    public Response selectShippingMethod(String method) {
        // Step 1: Get available shipping methods
        SpecFactory
                .getRequestSpec(cookieFilter)
                .get(ApiRoutes.SHIPPING_METHOD_GET)
                .then()
                .spec(SpecFactory.htmlResponseWithStatus(200));
        // Step 2: Save selected shipping
        return SpecFactory
                .getFormRequestSpec(cookieFilter)
                .formParam("shipping_method", method)
                .formParam("comment", "Fast shipping")
                .post(ApiRoutes.SHIPPING_METHOD_SAVE)
                .then()
                .spec(SpecFactory.jsonResponseWithStatus(200))
                .extract().response();
    }

    public Response selectPaymentMethod(String method) {
        // Step 1: Get available payment methods
        SpecFactory
                .getRequestSpec(cookieFilter)
                .get(ApiRoutes.PAYMENT_METHOD_GET)
                .then()
                .spec(SpecFactory.htmlResponseWithStatus(200));
        // Step 2: Save selected payment
        return SpecFactory
                .getFormRequestSpec(cookieFilter)
                .formParam("payment_method", method)
                .formParam("agree", "1")
                .formParam("comment", "Fast shipping")
                .post(ApiRoutes.PAYMENT_METHOD_SAVE)
                .then()
                .spec(SpecFactory.jsonResponseWithStatus(200))
                .extract().response();
    }

    public Response confirmOrder() {
        return SpecFactory
                .getRequestSpec(cookieFilter)
                .post(ApiRoutes.CONFIRM_ORDER)
                .then()
                .spec(SpecFactory.htmlResponseWithStatus(200))
                .extract().response();
    }

    public Response placeOrder() {
        return SpecFactory
                .getRequestSpec(cookieFilter)
                .get(ApiRoutes.PLACE_ORDER)
                .then()
                .spec(SpecFactory.htmlResponseWithStatus(200))
                .extract().response();

    }
}


