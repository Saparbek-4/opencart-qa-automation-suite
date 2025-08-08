package com.opencart.ui.tests;

import com.opencart.ui.base.BaseTest;
import com.opencart.ui.pages.*;
import com.opencart.utils.UserPoolManager;
import com.opencart.utils.WaitUtils;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Epic("Product Page")
@Feature("Product Display, Cart Interaction, and Review Functionalities")
@Owner("saparbek.kozhanazar04@gmail.com")
@Tag("ui")
@Tag("regression")
public class ProductPageTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(ProductPageTest.class);

    private HomePage homePage;
    private ProductPage productPage;
    private CartPage cartPage;
    private LoginPage loginPage;

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

    @Test(description = "TC_034: Product Page Loads Successfully",
            groups = {"ui", "regression", "smoke"})
    @Story("User opens a product page")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-034")
    @Tag("smoke")
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

    @Test(description = "TC_035 Verify Product Information Display",
            groups = {"ui", "regression"})
    @Story("User views detailed product info")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-035")
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

    @Test(description = "TC_036: Dropdown Option Validation (Required Option)",
            groups = {"ui", "regression"})
    @Story("User tries to add product without selecting required options")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-036")
    public void validateRequiredOptionValidation() {
        logger.info("🔁 Running test: TC_003 - Dropdown Option Validation (Required Option)");
        homePage.searchProduct(PRODUCT_WITH_OPTIONS);
        productPage.navigateToProduct(PRODUCT_WITH_OPTIONS);
        productPage.clickAddToCart();
        assertTrue(productPage.isOptionErrorDisplayed(), "Error message should be displayed for missing required option");
        String errorMessage = productPage.getOptionErrorMessage();
        assertTrue(errorMessage.contains("Size required!"), "Error message should indicate option selection is required");
    }

    @Test(description = "TC_037: Successful Add to Cart with Option",
            groups = {"ui", "regression"})
    @Story("User adds product with required option to cart")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("TC-037")
    public void validateSuccessfulAddToCartWithOption() {
        logger.info("🔁 Running test: TC_004 - Successful Add to Cart with Option");
        homePage.searchProduct(PRODUCT_WITH_OPTIONS);
        productPage.navigateToProduct(PRODUCT_WITH_OPTIONS);
        productPage.selectSizeOption(SIZE_OPTION);
        productPage.clickAddToCart();
        assertTrue(productPage.isSuccessAlertDisplayed(), "Success alert should appear");
        String successMessage = productPage.getSuccessMessage();
        assertTrue(successMessage.contains(PRODUCT_WITH_OPTIONS), "Success message should contain product name");
        assertTrue(successMessage.toLowerCase().contains("cart"), "Success message should mention cart");
    }

    @Test(description = "TC_039: Add to Cart Without Option (Should Fail)",
            groups = {"ui", "regression"})
    @Story("User skips required dropdown and adds to cart")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-039")
    public void validateAddToCartWithoutOption() {
        logger.info("🔁 Running test: TC_005 - Add to Cart Without Option (Should Fail)");
        homePage.searchProduct(PRODUCT_WITH_OPTIONS);
        productPage.navigateToProduct(PRODUCT_WITH_OPTIONS);
        productPage.clickAddToCart();
        assertTrue(productPage.isOptionErrorDisplayed(), "Error message should be displayed");
    }

    @Test(description = "TC_040: Add to Cart – Product With No Options",
            groups = {"ui", "regression"})
    @Story("User adds product that does not require options")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-040")
    public void validateAddToCartNoOptions() {
        logger.info("🔁 Running test: TC_006 - Add to Cart Product With No Options");
        homePage.searchProduct(PRODUCT_NO_OPTIONS);
        productPage.navigateToProduct(PRODUCT_NO_OPTIONS);
        assertFalse(productPage.hasRequiredOptions(), "iMac should not have required options");
        productPage.clickAddToCart();
        assertTrue(productPage.isSuccessAlertDisplayed(), "Product should be added to cart without dropdown selection");
        String successMessage = productPage.getSuccessMessage();
        assertTrue(successMessage.contains("iMac"), "Success message should contain product name");
    }

    @Test(description = "TC_041: Out of Stock Product",
            groups = {"ui", "regression"})
    @Story("User views and interacts with out-of-stock product")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("TC-041")
    public void validateOutOfStockProduct() {
        logger.info("🔁 Running test: TC_007 - Out of Stock Product");
        homePage.searchProduct(OUT_OF_STOCK_PRODUCT);
        productPage.navigateToProduct(OUT_OF_STOCK_PRODUCT);
        String availability = productPage.getAvailability();
        assertTrue(availability.contains("Out Of Stock"), "Product should show 'Out of Stock' availability");
        productPage.clickAddToCart();
        String successMessage = productPage.getSuccessMessage();
        assertTrue(successMessage.contains(OUT_OF_STOCK_PRODUCT), "Success message should contain product name");
        cartPage.navigateToCart();
        assertTrue(cartPage.isProductMarkedOutOfStock(OUT_OF_STOCK_PRODUCT), "Product should be marked as out of stock");
    }

    @Test(description = "TC_042: Review Form Validation",
            groups = {"ui", "regression"})
    @Story("User submits incomplete review")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-042")
    public void validateReviewFormValidation() {
        logger.info("🔁 Running test: TC_008 - Review Form Validation");
        homePage.searchProduct(PRODUCT_NO_OPTIONS);
        productPage.navigateToProduct(PRODUCT_NO_OPTIONS);
        productPage.clickReviewTab();
        productPage.fillReviewForm("", "This is a test review for review field!", "5");
        productPage.submitReview();
        assertTrue(productPage.isErrorAlertDisplayed(), "Error should be displayed for empty review name");
        String errorMessage = productPage.getErrorMessage();
        assertTrue(errorMessage.contains(" Name must be between 3 and 25 characters!"), "Error should mention name length requirements");
    }

    @Test(description = "TC_043: Submit Valid Review",
            groups = {"ui", "regression"})
    @Story("User submits valid review with rating")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-043")
    public void validateSubmitValidReview() {
        logger.info("🔁 Running test: TC_009 - Submit Valid Review");
        homePage.searchProduct(PRODUCT_NO_OPTIONS);
        productPage.navigateToProduct(PRODUCT_NO_OPTIONS);
        productPage.clickReviewTab();
        productPage.fillReviewForm("TestUser123", "This is a comprehensive test review", "4");
        productPage.submitReview();
        boolean hasSuccessMessage = productPage.isSuccessAlertDisplayed();
        if (hasSuccessMessage) {
            String successMessage = productPage.getSuccessMessage();
            assertTrue(successMessage.contains("Thank you for your review."), "Should show success or moderation message");
        }
    }

    @Test(description = "TC_044: UI Element Visibility",
            groups = {"ui", "regression"})
    @Story("User views key UI elements on product page")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-044")
    public void validateUIElementVisibility() {
        logger.info("🔁 Running test: TC_010 - UI Element Visibility");
        homePage.searchProduct(PRODUCT_NO_OPTIONS);
        productPage.navigateToProduct(PRODUCT_NO_OPTIONS);
        assertTrue(productPage.isProductImageDisplayed(), "Product image should be displayed");
        assertTrue(productPage.isAddToCartButtonDisplayed(), "Add to Cart button should be present");
        assertTrue(productPage.isPriceDisplayed(), "Price should be visible");
        assertFalse(productPage.hasRequiredOptions(), "iMac should not have dropdown options");
        assertTrue(productPage.isDescriptionTabFunctional(), "Description tab should be functional");
    }
}
