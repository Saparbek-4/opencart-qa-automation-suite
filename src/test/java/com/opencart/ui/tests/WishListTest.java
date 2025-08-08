package com.opencart.ui.tests;

import com.opencart.ui.base.BaseTest;
import com.opencart.ui.models.WishlistSetupResult;
import com.opencart.ui.pages.*;
import com.opencart.utils.UserPoolManager;
import com.opencart.utils.WaitUtils;
import com.opencart.utils.WishListUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;

@Epic("Wishlist")
@Feature("Wishlist Functionality")
@Owner("saparbek.kozhanazar04@gmail.com")
@Tag("regression")
@Tag("ui")
public class WishListTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(WishListTest.class);

    private HomePage homePage;
    private ProductPage productPage;
    private WishlistPage wishlistPage;
    private LoginPage loginPage;

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

    @Test(description = "TC_045: Add Product to Wish List from Product Page",
            groups = {"ui", "regression"})
    @Story("User adds product to wishlist from Product Page")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-045")
    public void testAddProductToWishlist() {
        logger.info("Running test: TC_045 - Add Product to Wish List");
        beforeEachWishlistTest(PRODUCT_NAME);
    }

    @Test(description = "TC_046: View Wish List Contents",
            groups = {"ui", "regression"})
    @Story("User views product details in wishlist")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-046")
    public void testViewWishlistContents() {
        logger.info("Running test: TC_046 - View Wish List Contents");
        beforeEachWishlistTest(PRODUCT_NAME);

        Assert.assertEquals(wishlistPage.getModel(PRODUCT_NAME), EXPECTED_MODEL, "Model mismatch");
        Assert.assertTrue(wishlistPage.getStock(PRODUCT_NAME) >= 0, "Stock not visible");
        Assert.assertTrue(wishlistPage.isAddToCartButtonVisible(PRODUCT_NAME), "🛒 button missing");
        Assert.assertTrue(wishlistPage.isRemoveButtonVisible(PRODUCT_NAME), "❌ button missing");
    }

    @Test(description = "TC_047: Add Product to Cart from Wish List",
            groups = {"ui", "regression"})
    @Story("User adds product to cart directly from wishlist")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-047")
    public void testAddWishlistProductToCart() {
        logger.info("Running test: TC_047 - Add Product from Wishlist to Cart");
        beforeEachWishlistTest(PRODUCT_NAME);

        wishlistPage.clickAddToCart(PRODUCT_NAME);

        String msg = wishlistPage.getSuccessMessage();
        Assert.assertTrue(msg.contains("added " + PRODUCT_NAME + " to your shopping cart"), "Cart success message not shown");
    }

    @Test(description = "TC_048: Remove Product from Wish List",
            groups = {"ui", "regression"})
    @Story("User removes product from wishlist")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-048")
    public void testRemoveProductFromWishlist() {
        logger.info("Running test: TC_048 - Remove Product from Wish List");
        beforeEachWishlistTest(PRODUCT_NAME);

        wishlistPage.removeProduct(PRODUCT_NAME);

        Assert.assertFalse(wishlistPage.isProductInWishlist(PRODUCT_NAME), "Product was not removed");
    }

    @Test(description = "TC_049: Verify Stock Display in Wish List",
            groups = {"ui", "regression"})
    @Story("User views stock status of wishlist items")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("TC-049")
    public void testStockDisplayInWishlist() {
        logger.info("Running test: TC_049 - Verify Stock Display in Wish List");
        beforeEachWishlistTest(PRODUCT_NAME);

        int stock = wishlistPage.getStock(PRODUCT_NAME);
        Assert.assertTrue(stock >= 0, "Stock should be a valid number or marked 'Out of Stock'");
    }

    @Test(description = "TC_050: Add Same Product Again to Wish List",
            groups = {"ui", "regression"})
    @Story("User tries to add same product twice to wishlist")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("TC-050")
    public void testAddSameProductAgain() {
        logger.info("Running test: TC_050 - Add Same Product Again to Wish List");

        homePage.searchProduct(PRODUCT_NAME);
        homePage.addToWishlist(PRODUCT_NAME);
        assertWishlistSuccessMessage(productPage.getSuccessMessage());

        homePage.addToWishlist(PRODUCT_NAME);
        assertWishlistSuccessMessage(productPage.getSuccessMessage());

        wishlistPage.navigateToWishlist();
        int count = wishlistPage.getWishlistProductCount(PRODUCT_NAME);

        Assert.assertEquals(count, 1, "Product should not be duplicated in wish list");
    }

    @Step("Preparing wishlist with only {product}")
    private void beforeEachWishlistTest(String product) {
        WishlistSetupResult setupResult = WishListUtils
                .prepareWishlistWithOnly(wishlistPage, homePage, product);

        assertWishlistSuccessMessage(setupResult.successMessage());
        Assert.assertTrue(setupResult.isInWishlist(), "Product should be in wish list");
    }

    @Step("Asserting success message: {actualMessage}")
    private void assertWishlistSuccessMessage(String actualMessage) {
        String expected = "You have added " + PRODUCT_NAME + " to your wish list!";
        Assert.assertTrue(actualMessage.contains(expected),
                "Expected message to contain: '" + expected + "', but got: '" + actualMessage + "'");
    }
}
