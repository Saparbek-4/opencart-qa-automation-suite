package com.opencart.ui.tests;

import com.opencart.ui.base.BaseTest;
import com.opencart.ui.models.WishlistSetupResult;
import com.opencart.ui.pages.*;
import com.opencart.utils.UserPoolManager;
import com.opencart.utils.WaitUtils;
import com.opencart.utils.WishListUtils;
import groovy.util.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WishListTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(WishListTest.class);

    private HomePage homePage;
    private ProductPage productPage;
    private WishlistPage wishlistPage;
    private LoginPage loginPage;

    // Test data
    private static final String PRODUCT_NAME = "iMac";
    private static final String EXPECTED_MODEL = "Product 14";

    @Override
    public void setupTestData() {
        homePage = new HomePage();
        productPage = new ProductPage();
        wishlistPage = new WishlistPage();
        loginPage = new LoginPage();

        loginPage.navigateToLoginPage();
        new WaitUtils().waitForPageLoad();

        String email = UserPoolManager.acquireUser();
        String password = getProp().getProperty("testUserPassword");

        loginPage.login(email, password);
    }

    @Test(description = "TC_WL_001: Add Product to Wish List from Product Page")
    public void testAddProductToWishlist() {
        logger.info("Running test: TC_WL_001 - Add Product to Wish List");
        beforeEachWishlistTest(PRODUCT_NAME);
    }

    @Test(description = "TC_WL_002: View Wish List Contents")
    public void testViewWishlistContents() {
        logger.info("Running test: TC_WL_002 - View Wish List Contents");
        beforeEachWishlistTest(PRODUCT_NAME);

        logger.debug("Validating product model, stock, and action buttons in wishlist...");

        Assert.assertEquals(wishlistPage.getModel(PRODUCT_NAME), EXPECTED_MODEL, "Model mismatch");
        Assert.assertTrue(wishlistPage.getStock(PRODUCT_NAME) >= 0, "Stock not visible");
        Assert.assertTrue(wishlistPage.isAddToCartButtonVisible(PRODUCT_NAME), "🛒 button missing");
        Assert.assertTrue(wishlistPage.isRemoveButtonVisible(PRODUCT_NAME), "❌ button missing");
    }

    @Test(description = "TC_WL_003: Add Product to Cart from Wish List")
    public void testAddWishlistProductToCart() {
        logger.info("Running test: TC_WL_003 - Add Product from Wishlist to Cart");
        beforeEachWishlistTest(PRODUCT_NAME);

        logger.debug("Clicking Add to Cart button from wishlist...");
        wishlistPage.clickAddToCart(PRODUCT_NAME);

        String msg = wishlistPage.getSuccessMessage();
        Assert.assertTrue(msg.contains("added " + PRODUCT_NAME + " to your shopping cart"), "Cart success message not shown");
    }

    @Test(description = "TC_WL_004: Remove Product from Wish List")
    public void testRemoveProductFromWishlist() {
        logger.info("Running test: TC_WL_004 - Remove Product from Wish List");
        beforeEachWishlistTest(PRODUCT_NAME);

        logger.debug("Removing product from wishlist...");
        wishlistPage.removeProduct(PRODUCT_NAME);

        Assert.assertFalse(wishlistPage.isProductInWishlist(PRODUCT_NAME), "Product was not removed");
    }

    @Test(description = "TC_WL_005: Verify Stock Display in Wish List")
    public void testStockDisplayInWishlist() {
        logger.info("Running test: TC_WL_005 - Verify Stock Display in Wish List");
        beforeEachWishlistTest(PRODUCT_NAME);
        int stock = wishlistPage.getStock(PRODUCT_NAME);

        logger.debug("Stock count for {}: {}", PRODUCT_NAME, stock);

        Assert.assertTrue(stock >= 0, "Stock should be a valid number or marked 'Out of Stock'");
    }

    @Test(description = "TC_WL_006: Add Same Product Again to Wish List")
    public void testAddSameProductAgain() {
        logger.info("Running test: TC_WL_006 - Add Same Product Again to Wish List");

        homePage.searchProduct(PRODUCT_NAME);
        homePage.addToWishlist(PRODUCT_NAME); // Add
        assertWishlistSuccessMessage(productPage.getSuccessMessage());

        logger.debug("Adding same product again to check idempotency...");
        homePage.addToWishlist(PRODUCT_NAME); // Add again
        assertWishlistSuccessMessage(productPage.getSuccessMessage());

        wishlistPage.navigateToWishlist();
        int count = wishlistPage.getWishlistProductCount(PRODUCT_NAME);
        logger.debug("Wishlist count for {}: {}", PRODUCT_NAME, count);

        Assert.assertEquals(count, 1, "Product should not be duplicated in wish list");
    }

    private void beforeEachWishlistTest(String product) {
        logger.debug("Preparing wishlist with only product: {}", product);
        WishlistSetupResult setupResult = WishListUtils
                .prepareWishlistWithOnly(wishlistPage, homePage, product);

        assertWishlistSuccessMessage(setupResult.successMessage());
        Assert.assertTrue(setupResult.isInWishlist(), "Product should be in wish list");
    }

    private void assertWishlistSuccessMessage(String actualMessage) {
        String expected = "You have added " + PRODUCT_NAME + " to your wish list!";
        logger.debug("Validating success message: {}", actualMessage);
        Assert.assertTrue(actualMessage.contains(expected),
                "Expected message to contain: '" + expected + "', but got: '" + actualMessage + "'");
    }
}
