package com.opencart.ui.tests;

import com.opencart.ui.base.BaseTest;
import com.opencart.ui.pages.CartPage;
import com.opencart.ui.pages.LoginPage;
import com.opencart.utils.UserPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import com.opencart.ui.pages.HomePage;
import com.opencart.ui.pages.ProductPage;
import com.opencart.utils.WaitUtils;

import static org.testng.Assert.*;

public class ProductPageTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(ProductPageTest.class);

    private HomePage homePage;
    private ProductPage productPage;
    private CartPage cartPage;
    private LoginPage loginPage;

    // Test data constants
    private static final String PRODUCT_WITH_OPTIONS = "Product 8";
    private static final String PRODUCT_NO_OPTIONS = "iMac";
    private static final String OUT_OF_STOCK_PRODUCT = "MacBook";
    private static final String SIZE_OPTION = "MEDIUM";

    @Override
    public void setupTestData() {
        logger.info("🧹 Initializing pages for product tests...");
        homePage = new HomePage();
        productPage = new ProductPage();
        cartPage = new CartPage();
        loginPage = new LoginPage();

        loginPage.navigateToLoginPage();
        new WaitUtils().waitForPageLoad();

        String email = UserPoolManager.acquireUser();
        String password = getProp().getProperty("testUserPassword");

        loginPage.login(email, password);
        new WaitUtils().waitForPageLoad();
    }

    @Test(description = "TC_001: Product Page Loads Successfully")
    public void validateProductPageLoadsSuccessfully() {
        logger.info("🔁 Running test: TC_001 - Product Page Loads Successfully");

        homePage.searchProduct(PRODUCT_NO_OPTIONS);
        productPage.navigateToProduct(PRODUCT_NO_OPTIONS);

        assertTrue(productPage.isProductPageLoaded(), "Product page should load successfully");
        assertEquals(productPage.getProductTitle(), "iMac", "Product title should match");
        assertTrue(productPage.isPriceDisplayed(), "Product price should be visible");
        assertTrue(productPage.isProductImageDisplayed(), "Product image should be displayed");
        assertTrue(productPage.isBreadcrumbDisplayed(), "Breadcrumb should be visible");
    }

    @Test(description = "TC_002: Verify Product Information Display")
    public void validateProductInformationDisplay() {
        logger.info("🔁 Running test: TC_002 - Verify Product Information Display");

        homePage.searchProduct(PRODUCT_NO_OPTIONS);
        productPage.navigateToProduct(PRODUCT_NO_OPTIONS);

        assertEquals(productPage.getProductTitle(), "iMac", "Product title should be correct");
        assertTrue(productPage.getProductCode().contains("Product 14"), "Product code should be displayed");
        assertTrue(productPage.getAvailability().contains("Out Of Stock"), "Availability should be displayed");
        assertTrue(productPage.getBrand().contains("Apple"),  "Brand should be Apple");
        assertTrue(productPage.isProductImageDisplayed(), "Product image gallery should load");
    }

    @Test(description = "TC_003: Dropdown Option Validation (Required Option)")
    public void validateRequiredOptionValidation() {
        logger.info("🔁 Running test: TC_003 - Dropdown Option Validation (Required Option)");

        homePage.searchProduct(PRODUCT_WITH_OPTIONS);
        productPage.navigateToProduct(PRODUCT_WITH_OPTIONS);

        // Don't select any option and try to add to cart
        productPage.clickAddToCart();

        assertTrue(productPage.isOptionErrorDisplayed(),
                "Error message should be displayed for missing required option");

        String errorMessage = productPage.getOptionErrorMessage();

        assertTrue(errorMessage.contains("Size required!"),
                "Error message should indicate option selection is required");
    }

    @Test(description = "TC_004: Successful Add to Cart with Option")
    public void validateSuccessfulAddToCartWithOption() {
        logger.info("🔁 Running test: TC_004 - Successful Add to Cart with Option");

        homePage.searchProduct(PRODUCT_WITH_OPTIONS);
        productPage.navigateToProduct(PRODUCT_WITH_OPTIONS);

        // Select required option
        productPage.selectSizeOption(SIZE_OPTION);

        // Add to cart
        productPage.clickAddToCart();

        assertTrue(productPage.isSuccessAlertDisplayed(), "Success alert should appear");
        String successMessage = productPage.getSuccessMessage();
        assertTrue(successMessage.contains(PRODUCT_WITH_OPTIONS),
                "Success message should contain product name");
        assertTrue(successMessage.toLowerCase().contains("cart"),
                "Success message should mention cart");
    }

    @Test(description = "TC_005: Add to Cart Without Option (Should Fail)")
    public void validateAddToCartWithoutOption() {
        logger.info("🔁 Running test: TC_005 - Add to Cart Without Option (Should Fail)");

        homePage.searchProduct(PRODUCT_WITH_OPTIONS);
        productPage.navigateToProduct(PRODUCT_WITH_OPTIONS);

        // Try to add to cart without selecting option
        productPage.clickAddToCart();

        assertTrue(productPage.isOptionErrorDisplayed(),
                "Error message should be displayed");
    }

    @Test(description = "TC_006: Add to Cart – Product With No Options")
    public void validateAddToCartNoOptions() {
        logger.info("🔁 Running test: TC_006 - Add to Cart Product With No Options");

        homePage.searchProduct(PRODUCT_NO_OPTIONS);
        productPage.navigateToProduct(PRODUCT_NO_OPTIONS);

        assertFalse(productPage.hasRequiredOptions(),
                "iMac should not have required options");

        productPage.clickAddToCart();

        assertTrue(productPage.isSuccessAlertDisplayed(),
                "Product should be added to cart without dropdown selection");
        String successMessage = productPage.getSuccessMessage();
        assertTrue(successMessage.contains("iMac"),
                "Success message should contain product name");
    }

    @Test(description = "TC_007: Out of Stock Product")
    public void validateOutOfStockProduct() {
        logger.info("🔁 Running test: TC_007 - Out of Stock Product");

        homePage.searchProduct(OUT_OF_STOCK_PRODUCT);
        productPage.navigateToProduct(OUT_OF_STOCK_PRODUCT);

        String availability = productPage.getAvailability();
        assertTrue(availability.contains("Out Of Stock"),
                "Product should show 'Out of Stock' availability");

        // Add to cart and check unavailability
        productPage.clickAddToCart();
        String successMessage = productPage.getSuccessMessage();
        assertTrue(successMessage.contains(OUT_OF_STOCK_PRODUCT),
                "Success message should contain product name");

        cartPage.navigateToCart();
        assertTrue(cartPage.isProductMarkedOutOfStock(OUT_OF_STOCK_PRODUCT), "Product should be marked as out of stock");
    }

    @Test(description = "TC_008: Review Form Validation")
    public void validateReviewFormValidation() {
        logger.info("🔁 Running test: TC_008 - Review Form Validation");

        homePage.searchProduct(PRODUCT_NO_OPTIONS);
        productPage.navigateToProduct(PRODUCT_NO_OPTIONS);
        productPage.clickReviewTab();

        // Submit review with empty name (clear the pre-filled email)
        productPage.fillReviewForm("", "This is a test review for review field!", "5");
        productPage.submitReview();

        assertTrue(productPage.isErrorAlertDisplayed(),
                "Error should be displayed for empty review name");
        String errorMessage = productPage.getErrorMessage();
        assertTrue(errorMessage.contains(" Name must be between 3 and 25 characters!"),
                "Error should mention name length requirements");
    }

    @Test(description = "TC_009: Submit Valid Review")
    public void validateSubmitValidReview() {
        logger.info("🔁 Running test: TC_009 - Submit Valid Review");

        homePage.searchProduct(PRODUCT_NO_OPTIONS);

        productPage.navigateToProduct(PRODUCT_NO_OPTIONS);
        productPage.clickReviewTab();

        // Submit valid review
        productPage.fillReviewForm("TestUser123", "This is a comprehensive test review", "4");
        productPage.submitReview();

        // Check for success or moderation message
        boolean hasSuccessMessage = productPage.isSuccessAlertDisplayed();
        if (hasSuccessMessage) {
            String successMessage = productPage.getSuccessMessage();
            assertTrue(successMessage.contains("Thank you for your review."),
                    "Should show success or moderation message");
        }
    }

    @Test(description = "TC_010: UI Element Visibility")
    public void validateUIElementVisibility() {
        logger.info("🔁 Running test: TC_010 - UI Element Visibility");

        homePage.searchProduct(PRODUCT_NO_OPTIONS);
        productPage.navigateToProduct(PRODUCT_NO_OPTIONS);

        assertTrue(productPage.isProductImageDisplayed(),
                "Product image should be displayed");
        assertTrue(productPage.isAddToCartButtonDisplayed(),
                "Add to Cart button should be present");
        assertTrue(productPage.isPriceDisplayed(),
                "Price should be visible");

        assertFalse(productPage.hasRequiredOptions(),
                "iMac should not have dropdown options");

        assertTrue(productPage.isDescriptionTabFunctional(),
                "Description tab should be functional");
    }

}