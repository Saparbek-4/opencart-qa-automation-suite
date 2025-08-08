package com.opencart.ui.tests;

import com.opencart.ui.base.BaseTest;
import com.opencart.ui.pages.*;
import com.opencart.utils.DriverFactory;
import com.opencart.utils.UserPoolManager;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.opencart.utils.WaitUtils;

import java.util.List;

@Epic("Checkout")
@Feature("Multi-step Checkout Flow")
@Owner("saparbek.kozhanazar04@gmail.com")
//@Tag("regression")
//@Tag("ui")
public class CheckoutTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutTest.class);
    private CheckoutPage checkoutPage;
    private CartPage cartPage;
    private ProductPage productPage;
    private LoginPage loginPage;
    private HomePage homePage;

    // Test data
    private static final String PRODUCT_NAME = "Product 8";
    private static final String SIZE_OPTION = "MEDIUM";
    private static final int QUANTITY = 1;

    @Override
    protected void setupTestData() {
        logger.info("Setting up checkout test data");

        homePage = new HomePage();
        cartPage = new CartPage();
        checkoutPage = new CheckoutPage();
        loginPage = new LoginPage();

        loginPage.navigateToLoginPage();
        new WaitUtils().waitForPageLoad();

        String email = UserPoolManager.acquireUser();
        String password = getProp().getProperty("testUserPassword");

        loginPage.login(email, password);

        homePage.searchProduct(PRODUCT_NAME);

        productPage.navigateToProduct(PRODUCT_NAME);
        productPage.selectSizeOption(SIZE_OPTION);
        productPage.setQuantity(QUANTITY);
        productPage.clickAddToCart();

        cartPage.navigateToCart();

        Assert.assertTrue(cartPage.isProductInCart(PRODUCT_NAME),
                "Product should be in the cart before proceeding to checkout");

        checkoutPage.navigateToCheckoutPage();
        new WaitUtils().waitForPageLoad();
    }


    @Test(priority = 1, description = "TC_016: Validate Billing Details Form Required Fields")
    @Story("User submits billing form without filling required fields")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-016")
    public void testBillingDetailsRequiredFieldsValidation() {
        logger.info("Starting testBillingDetailsRequiredFieldsValidation");

        checkoutPage.expandBillingDetails();
        if (checkoutPage.isNewAddressSelectionRequired("payment")) {
            checkoutPage.selectNewAddress("payment");
        }
        checkoutPage.clickContinue("payment");

        Assert.assertTrue(checkoutPage.isBillingValidationErrorDisplayed(),
                "Validation errors should be displayed for required fields");

        List<String> errors = checkoutPage.getBillingValidationErrors();
        logger.info("Found {} validation errors", errors.size());
        Assert.assertFalse(errors.isEmpty(), "Should have validation errors");

        validateErrorMessages(errors);
    }

    @Test(priority = 2, description = "TC_017: Submit Valid Billing Address")
    @Story("User completes billing form")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-017")
    public void testSubmitValidBillingAddress() {
        logger.info("Starting testSubmitValidBillingAddress");

        completeBillingDetails();
        Assert.assertTrue(checkoutPage.isStep3Visible(), "Should navigate to Step 3: Delivery Details");
    }

    @Test(priority = 3, description = "TC_018: Validate Delivery Details Form")
    @Story("User submits delivery form without filling required fields")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-018")
    public void testValidateDeliveryDetailsForm() {
        logger.info("Starting testValidateDeliveryDetailsForm");

        completeBillingDetails();
        checkoutPage.expandDeliveryDetails();
        checkoutPage.selectNewAddress("shipping");
        checkoutPage.clickContinue("shipping-address");

        Assert.assertTrue(checkoutPage.isStep3Visible(),
                "Should be on Step 3: Delivery Details");
    }

    @Test(priority = 4, description = "TC_019: Submit Valid Delivery Address")
    @Story("User completes delivery form")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-019")
    public void testSubmitValidDeliveryAddress() {
        logger.info("Starting testSubmitValidDeliveryAddress");

        completeBillingDetails();
        completeDeliveryDetails();

        Assert.assertTrue(checkoutPage.isStep4Visible(),
                "Should navigate to Step 4: Delivery Method");
    }

    @Test(priority = 5, description = "TC_020: Select Delivery Method")
    @Story("User selects delivery method")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-020")
    public void testSelectDeliveryMethod() {
        logger.info("Starting testSelectDeliveryMethod");

        completeBillingDetails();
        completeDeliveryDetails();
        completeDeliveryMethod();

        Assert.assertTrue(checkoutPage.isStep5Visible(),
                "Should navigate to Step 5: Payment Method");
    }

    @Test(priority = 6, description = "TC_021: Verify Terms and Conditions Validation")
    @Story("User accepts terms and conditions")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-021")
    public void testTermsAndConditionsValidation() {
        logger.info("Starting testTermsAndConditionsValidation");

        completeBillingDetails();
        completeDeliveryDetails();
        completeDeliveryMethod();

        checkoutPage.selectBankTransfer();
        checkoutPage.clickContinue("payment-method");

        Assert.assertTrue(checkoutPage.isTermsErrorDisplayed(),
                "Terms and conditions error should be displayed");

        String errorMessage = checkoutPage.getTermsErrorMessage();
        logger.info("Terms error message: {}", errorMessage);
        Assert.assertTrue(errorMessage.contains("Terms") && errorMessage.contains("Conditions"),
                "Error message should mention Terms & Conditions");
    }

    @Test(priority = 7, description = "TC_022: Select Payment Method and Accept Terms")
    @Story("User selects payment method and accepts terms")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("TC-022")
    public void testSelectPaymentMethodAndAcceptTerms() {
        logger.info("Starting testSelectPaymentMethodAndAcceptTerms");

        completeBillingDetails();
        completeDeliveryDetails();
        completeDeliveryMethod();
        completePaymentMethod("bank_transfer");

        Assert.assertTrue(checkoutPage.isStep6Visible(),
                "Should navigate to Step 6: Confirm Order");
    }

    @Test(priority = 8, description = "TC_023_COD: Select Cash On Delivery and Accept Terms")
    @Story("User selects payment method as 'cod' and accepts terms")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("TC-023")
    public void testSelectCashOnDeliveryAndAcceptTerms() {
        logger.info("Starting testSelectCashOnDeliveryAndAcceptTerms");

        completeBillingDetails();
        completeDeliveryDetails();
        completeDeliveryMethod();
        completePaymentMethod("cod");

        Assert.assertTrue(checkoutPage.isStep6Visible(),
                "Should navigate to Step 6: Confirm Order");
    }

    @Test(priority = 9, description = "TC_024: Confirm Order")
    @Story("User confirms order")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("TC-024")
    public void testConfirmOrder() {
        logger.info("Starting testConfirmOrder");

        completeBillingDetails();
        completeDeliveryDetails();
        completeDeliveryMethod();
        completePaymentMethod("bank_transfer");

        Assert.assertTrue(checkoutPage.isStep6Visible(),
                "Should be on Step 6: Confirm Order");

        checkoutPage.clickConfirmOrder();

        Assert.assertTrue(checkoutPage.isOrderSuccessful(),
                "Order success page should be displayed");

        String successMessage = checkoutPage.getSuccessMessage();
        logger.info("Order success message: {}", successMessage);
        Assert.assertTrue(successMessage.contains("Your order has been placed"),
                "Success message should confirm order placement");
    }

    @Test(priority = 10, description = "TC_025: Validate Final Amount (Subtotal + Shipping = Total)")
    @Story("User checks final amount before confirming order")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-025")
    public void testValidateFinalAmount() {
        logger.info("Starting testValidateFinalAmount");

        completeBillingDetails();
        completeDeliveryDetails();
        completeDeliveryMethod();
        completePaymentMethod("bank_transfer");

        Assert.assertTrue(checkoutPage.validateTotalCalculation(),
                "Total should equal Subtotal + Shipping cost");

        String shippingCost = checkoutPage.getShippingCost();
        logger.info("Shipping cost: {}", shippingCost);
        Assert.assertTrue(shippingCost.contains("5.00"),
                "Shipping cost should be $5.00 for Flat Rate");
    }

    @Test(priority = 11, description = "TC_026: Post-Order Cart Clearance")
    @Story("User checks cart page for clearance")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("TC-026")
    public void testPostOrderCartClearance() {
        logger.info("Starting testPostOrderCartClearance");

        testConfirmOrder();
        cartPage.navigateToCart();

        String emptyText = cartPage.getEmptyCartMessage();
        logger.info("Cart message: {}", emptyText);
        Assert.assertTrue(emptyText.contains("Your shopping cart is empty!"), "Cart is not empty");
    }

    @Step("Validating error messages: {errors}")
    private void validateErrorMessages(List<String> errors) {
        logger.debug("Validating error messages");
        boolean hasFirstNameError = errors.stream().anyMatch(error ->
                error.contains("First Name must be between 1 and 32 characters"));
        boolean hasLastNameError = errors.stream().anyMatch(error ->
                error.contains("Last Name must be between 1 and 32 characters"));
        boolean hasAddress1Error = errors.stream().anyMatch(error ->
                error.contains("Address 1 must be between 3 and 128 characters"));
        boolean hasCityError = errors.stream().anyMatch(error ->
                error.contains("City must be between 2 and 128 characters"));

        Assert.assertTrue(hasFirstNameError, "First Name validation error should be displayed");
        Assert.assertTrue(hasLastNameError, "Last Name validation error should be displayed");
        Assert.assertTrue(hasAddress1Error, "Address 1 validation error should be displayed");
        Assert.assertTrue(hasCityError, "City validation error should be displayed");
    }

    @Step("Completing billing details")
    private void completeBillingDetails() {
        logger.debug("Completing billing details");
        checkoutPage.expandBillingDetails();
        if (checkoutPage.isNewAddressSelectionRequired("payment")) {
            checkoutPage.selectNewAddress("payment");
        }
        fillAddressFields("payment");
        logger.info("Clicking continue on billing");
        checkoutPage.clickContinue("payment");

        WaitUtils waitUtils = new WaitUtils();
        waitUtils.waitUntilInvisibilityOfElementLocated(By.id("collapse-payment-address"));

        waitUtils.waitForVisibility(By.id("collapse-shipping-address"));

        logger.info("Step 3 (Delivery Details) is now visible");
    }

    @Step("Filling address fields")
    private void fillAddressFields(String type) {
        logger.debug("Filling address fields for type: {}", type);
        checkoutPage.fillAddressDetails(
                type,
                "John",
                "Doe",
                "123 Main Street",
                "New York",
                "United States",
                "New York"
        );
    }

    @Step("Completing delivery details")
    private void completeDeliveryDetails() {
        logger.debug("Completing delivery details");
        checkoutPage.expandDeliveryDetails();
        if (checkoutPage.isNewAddressSelectionRequired("shipping")) {
            checkoutPage.selectNewAddress("shipping");
        }
        fillAddressFields("shipping");
        checkoutPage.clickContinue("shipping-address");

        WaitUtils waitUtils = new WaitUtils();
        waitUtils.waitUntilInvisibilityOfElementLocated(By.id("collapse-shipping-address"));
        waitUtils.waitForVisibility(By.id("collapse-shipping-method"));

        logger.info("Step 4 (Delivery Method) is now visible");
    }

    @Step("Completing delivery method")
    private void completeDeliveryMethod() {
        logger.debug("Completing delivery method");
        checkoutPage.selectFlatRateDelivery();
        checkoutPage.addDeliveryComments("Please deliver between 9 AM - 5 PM");
        checkoutPage.clickContinue("shipping-method");

        WaitUtils waitUtils = new WaitUtils();
        waitUtils.waitUntilInvisibilityOfElementLocated(By.id("collapse-shipping-method"));
        waitUtils.waitForVisibility(By.id("collapse-payment-method"));

        logger.info("Step 5 (Payment Method) is now visible");
    }

    @Step("Completing payment method")
    private void completePaymentMethod(String paymentType) {
        logger.debug("Completing payment method: {}", paymentType);
        if ("bank_transfer".equals(paymentType)) {
            checkoutPage.selectBankTransfer();
        } else {
            checkoutPage.selectCashOnDelivery();
        }
        checkoutPage.acceptTerms();
        checkoutPage.clickContinue("payment-method");

        WaitUtils waitUtils = new WaitUtils();
        waitUtils.waitUntilInvisibilityOfElementLocated(By.id("collapse-payment-method"));
        waitUtils.waitForVisibility(By.id("collapse-checkout-confirm"));

        logger.info("Step 6 (Confirm Order) is now visible");
    }
}