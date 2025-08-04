package com.opencart.api.stepdefinitions;

import com.opencart.api.clients.CartApi;
import com.opencart.utils.SessionManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class CartApiStepDefinitions extends BaseApiStepDefinitions {

    private static final Logger logger = LoggerFactory.getLogger(CartApiStepDefinitions.class);
    private final CartApi cartApi = new CartApi(SessionManager.getCookieFilter());

    @Given("my cart is empty")
    public void my_cart_is_empty() {
        logger.info("🧹 Clearing all items from the cart...");
        cartApi.removeAllItems();
    }

    @When("I view the cart")
    public void i_view_the_cart() {
        logger.info("🛒 Viewing current cart...");
        response = cartApi.getCart();
    }

    @When("I add product with ID {int} and quantity {int} to the cart")
    public void i_add_product_to_cart(int productId, int quantity) {
        logger.info("➕ Adding product ID {} with quantity {} to cart...", productId, quantity);
        response = cartApi.addToCart(productId, quantity);
    }

    @Given("the product with ID {int} and quantity {int} is in the cart")
    public void the_product_is_in_cart(int productId, int quantity) {
        logger.info("🔁 Ensuring product ID {} with quantity {} is in the cart...", productId, quantity);
        cartApi.removeAllItems();
        response = cartApi.addToCart(productId, quantity);
        validateStatusCode(200);
    }

    @Given("the product with ID {int} is already in the cart")
    public void the_product_is_already_in_cart(int productId) {
        logger.info("🧩 Ensuring product ID {} is already in the cart (default quantity 1)...", productId);
        the_product_is_in_cart(productId, 1);
    }

    @When("I update product ID {int} to quantity {int}")
    public void i_update_product_quantity(int productId, int quantity) {
        logger.info("🔄 Updating product ID {} to quantity {}...", productId, quantity);
        cartApi.updateQuantity(productId, quantity);
    }

    @When("I remove the product with ID {int} from the cart")
    public void i_remove_product_from_cart(int productId) {
        logger.info("❌ Removing product ID {} from the cart...", productId);
        cartApi.removeByProductId(productId);
    }

    @Then("the cart should contain product ID {int} with quantity {int}")
    public void cart_should_contain_product_with_quantity(int productId, int quantity) {
        logger.info("🔎 Verifying product ID {} with quantity {} is in the cart...", productId, quantity);
        String html = getCartHtml();
        assertThat(html, containsString("product_id=" + productId));
        assertThat(html, containsString("value=\"" + quantity + "\""));
    }

    @Then("the subtotal should be {string}")
    public void subtotal_should_be(String subtotal) {
        logger.info("💰 Verifying subtotal: {}", subtotal);
        assertCartContains("<strong>Sub-Total:</strong>", subtotal);
    }

    @Then("the eco tax should be {string}")
    public void eco_tax_should_be(String ecoTax) {
        logger.info("🧾 Verifying eco tax: {}", ecoTax);
        assertCartContains("<strong>Eco Tax (-2.00):</strong>", ecoTax);
    }

    @Then("the VAT should be {string}")
    public void vat_should_be(String vat) {
        logger.info("📊 Verifying VAT: {}", vat);
        assertCartContains("<strong>VAT (20%):</strong>", vat);
    }

    @Then("the total should be {string}")
    public void total_should_be(String total) {
        logger.info("💸 Verifying total: {}", total);
        assertCartContains("<strong>Total:</strong>", total);
    }

    @Then("the cart should be empty")
    public void cart_should_be_empty() {
        logger.info("🈳 Verifying the cart is empty...");
        assertThat(getCartHtml(), containsString("Your shopping cart is empty!"));
    }

    private String getCartHtml() {
        return cartApi.getCart().asString();
    }

    private void assertCartContains(String label, String value) {
        String html = getCartHtml();
        assertThat(html, containsString(label));
        assertThat(html, containsString(value));
    }
}
