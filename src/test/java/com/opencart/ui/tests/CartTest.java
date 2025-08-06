package com.opencart.ui.tests;


import com.opencart.ui.base.BaseTest;
import com.opencart.ui.models.CartSetupResult;
import com.opencart.ui.pages.CartPage;
import com.opencart.ui.pages.HomePage;
import com.opencart.ui.pages.LoginPage;
import com.opencart.ui.pages.ProductPage;
import com.opencart.utils.CartTestUtils;
import com.opencart.utils.DriverFactory;
import com.opencart.utils.UserPoolManager;
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
    private LoginPage loginPage;

    // Test data constants
    private static final String PRODUCT_NAME = "Product 8";
    private static final String PRODUCT_MODEL = "Product 8";
    private static final String SIZE_OPTION = "MEDIUM";

    @Override
    public void setupTestData() {
        logger.info("🧹 Clearing cart and initializing pages for new test...");
        cartPage = new CartPage();
        cartPage.navigateToCart();

        homePage = new HomePage();
        productPage = new ProductPage();
        loginPage = new LoginPage();

        loginPage.navigateToLoginPage();
        new WaitUtils().waitForPageLoad();

        String email = UserPoolManager.acquireUser();
        String password = getProp().getProperty("testUserPassword");

        loginPage.login(email, password);

        cartPage.navigateToCart();
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
    }

    @Test(description = "TC_003: Increase Quantity and Validate Total")
    public void verifyQuantityUpdate() {
        logger.info("🔁 Running test: TC_003 - Increase Quantity and Validate Total");

        beforeEachCartTest(PRODUCT_NAME, 3);

        int expectedTotal = cartPage.getUnitPrice(PRODUCT_NAME) * 3;
        assertEquals(cartPage.getQuantity(PRODUCT_NAME), 3, "Quantity should be updated to 3");
        assertEquals(cartPage.getTotalPrice(PRODUCT_NAME), expectedTotal, "Total should be unit price × 3");
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


    @Test(description = "TC_006: Proceed to Checkout Button")
    public void verifyProceedToCheckout() {
        logger.info("🔁 Running test: TC_007 - Proceed to Checkout Button");

        beforeEachCartTest(PRODUCT_NAME, 1);
        cartPage.proceedToCheckout();

        String currentUrl = DriverFactory.getDriver().getCurrentUrl();
        logger.debug("Redirected URL after checkout: {}", currentUrl);
        assertTrue(currentUrl.contains("checkout/checkout"), "Should be redirected to checkout page");
    }

    @Test(description = "TC_007: Validate Stock Limit Handling")
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
                .prepareCartWithOnly(cartPage, homePage, productPage, product, quantity, SIZE_OPTION);

        assertCartSuccessMessage(setupResult.successMessage());
        Assert.assertTrue(setupResult.isInCart(), "Product should be in cart");
    }

    private void assertCartSuccessMessage(String actualMessage) {
        String expected1 = "You have added " + PRODUCT_NAME + " to your shopping cart!";
        String expected2 = "You have modified your shopping cart!";
        String expected3 = "Product already in cart with desired quantity!";
        String expected4 = "Product added but no success message displayed.";
        logger.debug("📥 Validating success message: {}", actualMessage);
        Assert.assertTrue(actualMessage.contains(expected1) ||
                        actualMessage.contains(expected2) ||
                        actualMessage.contains(expected3) ||
                        actualMessage.contains(expected4),
                "Expected messages to contain: '" + "\n" + expected1 + "\n OR \n" + expected2 + "\n OR \n" + expected3 + "\n OR \n" + expected4 + "',\n but got: \n'" + actualMessage + "'");
    }
}
