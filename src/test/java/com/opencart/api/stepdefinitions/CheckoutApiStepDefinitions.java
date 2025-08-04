package com.opencart.api.stepdefinitions;

import com.opencart.api.clients.CartApi;
import com.opencart.api.clients.CheckoutApi;
import com.opencart.api.enums.AddressType;
import com.opencart.utils.SessionManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckoutApiStepDefinitions extends BaseApiStepDefinitions {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutApiStepDefinitions.class);

    private final CheckoutApi checkoutApi = new CheckoutApi(SessionManager.getCookieFilter());
    private final CartApi cartApi = new CartApi(SessionManager.getCookieFilter());

    @Given("I start a guest session")
    public void i_start_a_guest_session() {
        logger.info("🔓 Starting guest session and initializing cart...");
        response = SessionManager.startGuestSession();
    }

    @Given("I start authorized session")
    public void i_start_authorized_session() {
        logger.info("🔐 Authorized session is already initialized via Hooks.");
        // No action needed (handled by Hooks)
    }

    @And("I add product with ID {int} and quantity {int} to the cart to proceed checkout")
    public void i_add_product_to_cart(int productId, int quantity) {
        logger.info("🛒 Adding product to cart → ID: {}, Quantity: {}", productId, quantity);
        response = cartApi.addToCart(productId, quantity);
    }

    @When("I fill billing and delivery details")
    public void i_fill_in_guest_billing_and_delivery_details() {
        logger.info("📦 Filling billing and delivery details...");
        AddressType[] addressTypes;

        if (SessionManager.isAuthenticated()) {
            logger.info("🔐 Detected authorized session — filling AUTH billing and shipping...");
            checkoutApi.initAuthCheckout();
            addressTypes = new AddressType[]{
                    AddressType.AUTH_BILLING,
                    AddressType.AUTH_SHIPPING
            };
        } else {
            logger.info("👤 Detected guest session — filling GUEST billing and shipping...");
            checkoutApi.initGuestCheckout();
            addressTypes = new AddressType[]{
                    AddressType.GUEST_BILLING,
                    AddressType.GUEST_SHIPPING
            };
        }

        // Billing
        response = checkoutApi.fillAddressDetails(
                addressTypes[0],
                "John", "Doe", "john@example.com", "123456789",
                "123 Main St", "Almaty", "123456", "81", "1253"
        );

        // Shipping
        response = checkoutApi.fillAddressDetails(
                addressTypes[1],
                "John", "Doe", "john@example.com", "123456789",
                "123 Main St", "Almaty", "123456", "81", "1253"
        );
    }

    @And("I select shipping method {string}")
    public void i_select_shipping_method(String shippingMethod) {
        logger.info("🚚 Selecting shipping method: {}", shippingMethod);
        response = checkoutApi.selectShippingMethod(shippingMethod);
    }

    @And("I select payment method {string}")
    public void i_select_payment_method(String paymentMethod) {
        logger.info("💳 Selecting payment method: {}", paymentMethod);
        response = checkoutApi.selectPaymentMethod(paymentMethod);
    }

    @And("I confirm the order")
    public void i_confirm_the_order() {
        logger.info("🧾 Confirming the order...");
        response = checkoutApi.confirmOrder();
    }

    @And("the order should be successfully placed")
    public void the_order_should_be_successfully_placed() {
        logger.info("✅ Placing the order...");
        response = checkoutApi.placeOrder();
    }
}
