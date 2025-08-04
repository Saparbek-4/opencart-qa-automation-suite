package com.opencart.api.clients;

import com.opencart.utils.SpecFactory;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CartApi {

    private final CookieFilter cookieFilter;

    public CartApi(CookieFilter cookieFilter) {
        this.cookieFilter = cookieFilter;
    }

    public Response getCart() {
        return SpecFactory
                .getRequestSpec(cookieFilter)
                .get(ApiRoutes.CART);
    }

    public Response addToCart(int productId, int quantity) {
        return SpecFactory
                .getFormRequestSpec(cookieFilter)
                .accept(ContentType.JSON)
                .header("X-Requested-With", "XMLHttpRequest")
                .formParam("product_id", productId)
                .formParam("quantity", quantity)
                .post(ApiRoutes.ADD_CART)
                .then()
                .spec(SpecFactory.jsonResponseWithStatus(200))
                .extract()
                .response();
    }

    public Response removeFromCart(String key) {
        return SpecFactory
                .getFormRequestSpec(cookieFilter)
                .formParam("key", key)
                .post(ApiRoutes.REMOVE_CART);
    }

    public void removeByProductId(int productId) {
        String key = extractCartItemKey(productId);
        removeFromCart(key);
    }

    public void removeAllItems() {
        Matcher matcher = Pattern.compile("name=\"quantity\\[(.*?)\\]\"").matcher(getCart().asString());

        while (matcher.find()) {
            String key = matcher.group(1);
            removeFromCart(key);
        }
    }

    public void updateQuantity(int productId, int newQty) {
        String key = extractCartItemKey(productId);

        SpecFactory
                .getFormRequestSpec(cookieFilter)
                .formParam("quantity[" + key + "]", newQty)
                .post(ApiRoutes.EDIT_CART);
    }

    public String extractCartItemKey(int productId) {
        String html = getCart().asString();
        Document doc = Jsoup.parse(html);
        Element input = doc.selectFirst("input[name^=quantity][value][name*=quantity][value=1]");
        if (input != null) {
            String nameAttr = input.attr("name"); // example: quantity[379320]
            return nameAttr.substring(nameAttr.indexOf('[') + 1, nameAttr.indexOf(']'));
        }
        throw new RuntimeException("Cart item key not found for product ID: " + productId);
    }


}
