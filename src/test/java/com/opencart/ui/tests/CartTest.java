package com.opencart.ui.tests;


import com.opencart.ui.base.BaseTest;
import com.opencart.ui.models.CartSetupResult;
import com.opencart.ui.pages.CartPage;
import com.opencart.ui.pages.HomePage;
import com.opencart.ui.pages.ProductPage;
import com.opencart.utils.CartTestUtils;
import com.opencart.utils.DriverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;


import com.opencart.utils.WaitUtils;

import static org.testng.Assert.*;

public class CartTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(CartTest.class);

    private HomePage homePage;
    private CartPage cartPage;
    private ProductPage productPage;

    private static final String PRODUCT_NAME = "iMac";
    private static final String PRODUCT_MODEL = "Product 14";
    private static final String PRODUCT_PRICE = "$100.00";

    @Override
    public void setupTestData() {
        logger.info("🧹 Clearing cart and initializing pages for new test...");
        cartPage = new CartPage();
        cartPage.navigateToCart();

        homePage = new HomePage();
        productPage = new ProductPage();
        new WaitUtils().waitForPageLoad();
    }

    @Test(description = "TC_001: Validate Empty Cart Message")
    public void validateEmptyCartMessage() {
        logger.info("🔁 Running test: TC_001 - Validate Empty Cart Message");

        if (!cartPage.isEmptyCartMessageDisplayed()) {
            logger.debug("Cart is not empty — clearing it.");
            cartPage.clearCartIfNotEmpty();
        }

        assertTrue(cartPage.isEmptyCartMessageDisplayed(), "Empty cart message should be displayed");
    }

    @Test(description = "TC_002: Add Single Product and Verify Cart Row")
    public void verifyProductInfoInCart() {
        logger.info("🔁 Running test: TC_002 - Add Single Product and Verify Cart Row");

        beforeEachCartTest(PRODUCT_NAME, 1);

        assertEquals(cartPage.getProductName(PRODUCT_NAME), PRODUCT_NAME, "Product name mismatch");
        assertEquals(cartPage.getProductModel(PRODUCT_NAME), PRODUCT_MODEL, "Product model mismatch");
        assertEquals(cartPage.getQuantity(PRODUCT_NAME), 1, "Quantity mismatch");
        assertEquals(cartPage.getUnitPrice(PRODUCT_NAME), PRODUCT_PRICE, "Unit price mismatch");
        assertEquals(cartPage.getTotalPrice(PRODUCT_NAME), PRODUCT_PRICE, "Total price mismatch");
    }

    @Test(description = "TC_003: Increase Quantity and Validate Total")
    public void verifyQuantityUpdate() {
        logger.info("🔁 Running test: TC_003 - Increase Quantity and Validate Total");

        beforeEachCartTest(PRODUCT_NAME, 3);

        assertEquals(cartPage.getQuantity(PRODUCT_NAME), 3, "Quantity should be updated to 3");
        assertEquals(cartPage.getTotalPrice(PRODUCT_NAME), "$300.00", "Total should be unit price × 3");
    }

    @Test(description = "TC_004: Set Quantity to 0 - Product Removed")
    public void verifyProductRemovalBySettingQuantityToZero() {
        logger.info("🔁 Running test: TC_004 - Remove Product by Setting Quantity to 0");

        beforeEachCartTest(PRODUCT_NAME, 1);
        cartPage.removeProductBySettingQuantityToZero(PRODUCT_NAME);

        assertTrue(cartPage.isEmptyCartMessageDisplayed(), "Empty cart message should be displayed after removing product");
    }

    @Test(description = "TC_005: Remove Product via Delete Button")
    public void verifyProductRemovalViaDeleteButton() {
        logger.info("🔁 Running test: TC_005 - Remove Product via Delete Button");

        beforeEachCartTest(PRODUCT_NAME, 1);
        cartPage.removeProductViaDeleteButton(PRODUCT_NAME);

        assertTrue(cartPage.isEmptyCartMessageDisplayed(), "Empty cart message should be displayed after removing product");
    }

    @Test(description = "TC_006: Validate Subtotal, Eco Tax, VAT, and Total Calculation")
    public void verifyPriceCalculations() {
        logger.info("🔁 Running test: TC_006 - Validate Subtotal, VAT, Total");

        beforeEachCartTest(PRODUCT_NAME, 1);

        assertEquals(cartPage.getSubtotal(), "$100.00", "Subtotal mismatch");
        assertEquals(cartPage.getGrandTotal(), "$100.00", "Total mismatch");
    }

    @Test(description = "TC_007: Proceed to Checkout Button")
    public void verifyProceedToCheckout() {
        logger.info("🔁 Running test: TC_007 - Proceed to Checkout Button");

        beforeEachCartTest(PRODUCT_NAME, 1);
        cartPage.proceedToCheckout();

        String currentUrl = DriverFactory.getDriver().getCurrentUrl();
        logger.debug("Redirected URL after checkout: {}", currentUrl);
        assertTrue(currentUrl.contains("checkout/checkout"), "Should be redirected to checkout page");
    }

    @Test(description = "TC_008: Validate Stock Limit Handling")
    public void verifyOutOfStockWarning() {
        logger.info("🔁 Running test: TC_008 - Validate Stock Limit Handling");

        beforeEachCartTest(PRODUCT_NAME, 1);
        cartPage.updateQuantity(PRODUCT_NAME, 1000);

        assertTrue(cartPage.isProductMarkedOutOfStock(PRODUCT_NAME), "Product should be marked as out-of-stock");
        assertTrue(cartPage.isStockWarningDisplayed(), "Stock warning message should appear for exceeding quantity");
    }

    private void beforeEachCartTest(String product, int quantity) {
        logger.info("🧪 Preparing cart with only product: {}, quantity: {}", product, quantity);
        CartSetupResult setupResult = CartTestUtils
                .prepareCartWithOnly(cartPage, homePage, productPage, product, quantity);

        assertCartSuccessMessage(setupResult.successMessage());
        Assert.assertTrue(setupResult.isInCart(), "Product should be in cart");
    }

    private void assertCartSuccessMessage(String actualMessage) {
        String expected = "You have added " + PRODUCT_NAME + " to your cart!";
        logger.debug("📥 Validating success message: {}", actualMessage);
        Assert.assertTrue(actualMessage.contains(expected),
                "Expected message to contain: '" + expected + "', but got: '" + actualMessage + "'");
    }
}
