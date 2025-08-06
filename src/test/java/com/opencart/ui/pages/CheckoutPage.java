package com.opencart.ui.pages;

import com.opencart.ui.base.BasePage;
import com.opencart.utils.ConfigReader;
import com.opencart.utils.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckoutPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutPage.class);


    // Locators for all checkout steps and fields (step 2 to step 6)
    private final By step2Header = By.xpath("//h4[contains(text(), 'Step 2: Billing Details')]");
    private final By step2Accordion = By.id("collapse-payment-address");
    private final By useExistingAddressRadio = By.xpath("//input[@name='payment_address'][@value='existing']");
    private final By useNewPaymentAddressRadio = By.xpath("//input[@name='payment_address'][@value='new']");
    private final By useNewShippingAddressRadio = By.xpath("//input[@name='shipping_address'][@value='new']");

    // Validation messages
    private final By firstNameError = By.xpath("//input[@id='input-payment-firstname']/following-sibling::div[@class='text-danger']");
    private final By lastNameError = By.xpath("//input[@id='input-payment-lastname']/following-sibling::div[@class='text-danger']");
    private final By address1Error = By.xpath("//input[@id='input-payment-address-1']/following-sibling::div[@class='text-danger']");
    private final By cityError = By.xpath("//input[@id='input-payment-city']/following-sibling::div[@class='text-danger']");

    // Step 3 Delivery Details
    private final By step3Header = By.xpath("//h4[contains(text(), 'Step 3: Delivery Details')]");
    private final By step3Accordion = By.id("collapse-shipping-address");

    // Step 4 Delivery Method
    private final By step4Header = By.xpath("//h4[contains(text(), 'Step 4: Delivery Method')]");
    private final By step4Accordion = By.id("collapse-shipping-method");
    private final By flatRateOption = By.xpath("//input[@value='flat.flat']");
    private final By deliveryCommentsTextarea = By.xpath("//textarea[@name='comment']");
    private final By deliveryContinueButton = By.id("button-shipping-method");

    // Step 5 Payment Method
    private final By step5Header = By.xpath("//h4[contains(text(), 'Step 5: Payment Method')]");
    private final By step5Accordion = By.id("collapse-payment-method");
    private final By bankTransferOption = By.xpath("//input[@value='bank_transfer']");
    private final By cashOnDeliveryOption = By.xpath("//input[@value='cod']");
    private final By termsCheckbox = By.xpath("//input[@name='agree']");
    private final By paymentContinueButton = By.id("button-payment-method");
    private final By termsError = By.xpath("//div[contains(@class, 'alert-danger') and contains(text(), 'Terms')]");

    // Step 6 Confirm Order
    private final By step6Header = By.xpath("//h4[contains(text(), 'Step 6: Confirm Order')]");
    private final By step6Accordion = By.id("collapse-checkout-confirm");
    private final By confirmOrderButton = By.id("button-confirm");
    private final By orderSubtotal = By.xpath("//tr[td[strong[text()='Sub-Total:']]]/td[last()]");
    private final By shippingCost = By.xpath("//tr[td[strong[text()='Flat Shipping Rate:']]]/td[last()]");
    private final By orderTotal = By.xpath("//tr[td[strong[text()='Total:']]]/td[last()]");
    private final By successMessage = By.xpath("//h1[contains(text(), 'Your order has been placed!')]");

    public CheckoutPage() {
        super();
        logger.info("CheckoutPage initialized");
    }


    /**
     * Dynamically generates input locator by type (payment/shipping) and field name.
     */
    private By inputLocator(String type, String field) {
        return By.id(String.format("input-%s-%s", type, field));
    }

    /**
     * Navigate to Checkout page
     */
    public void navigateToCheckoutPage() {
        DriverFactory.getDriver().navigate().to(ConfigReader.getProperty("baseUrl") + "index.php?route=checkout/checkout");
    }

    /**
     * Expand the Billing/Delivery Details section if not already open
     */
    public void expandBillingDetails() {
        logger.info("[Step 2 - Billing] Attempting to expand billing details");
        if (!isElementDisplayed(step2Accordion)) {
            logger.info("Billing details not expanded, clicking header");
            click(step2Header);
        } else {
            logger.info("Billing details already expanded. It means we don't have default address.");
        }
    }

    public void expandDeliveryDetails() {
        logger.debug("[Step 3 - Billing] Attempting to expand delivery details");
        if (!isElementDisplayed(step3Accordion)) {
            logger.debug("Delivery details not expanded, clicking header");
            click(step3Header);
        } else {
            logger.debug("Delivery details already expanded");
        }
    }

    /**
     * Select "New Address" radio button for Billing or Shipping
     * @param type "payment" or "shipping"
     */
    public void selectNewAddress(String type) {
        By locator = type.equals("payment") ? useNewPaymentAddressRadio : useNewShippingAddressRadio;

        if (isElementPresent(locator)) {
            logger.info("✅ Selecting 'New {} Address' radio option", type);
            click(locator);
        } else {
            logger.info("ℹ️ No '{}' address radio button found – assuming new form shown by default", type);
        }
    }

    /**
     * Fill address fields for Billing or Shipping
     * @param type "payment" or "shipping"
     * @param firstName ...
     * @param lastName ...
     * @param address1 ...
     * @param city ...
     * @param country ...
     * @param region ...
     */
    public void fillAddressDetails(String type, String firstName, String lastName, String address1,
                                   String city, String country, String region) {
        logger.info("📝 Filling {} address details: FirstName={}, LastName={}, Address1={}, City={}, Country={}, Region={}",
                type, firstName, lastName, address1, city, country, region);

        By firstNameInput = inputLocator(type, "firstname");
        By lastNameInput = inputLocator(type, "lastname");
        By address1Input = inputLocator(type, "address-1");
        By cityInput = inputLocator(type, "city");
        By countrySelect = inputLocator(type, "country");
        By regionSelect = inputLocator(type, "zone");

        if (!firstName.isEmpty()) {
            logger.debug("Entering first name: {}", firstName);
            type(firstNameInput, firstName);
        }
        if (!lastName.isEmpty()) {
            logger.debug("Entering last name: {}", lastName);
            type(lastNameInput, lastName);
        }
        if (!address1.isEmpty()) {
            logger.debug("Entering address line 1: {}", address1);
            type(address1Input, address1);
        }
        if (!city.isEmpty()) {
            logger.debug("Entering city: {}", city);
            type(cityInput, city);
        }

        wait.get().waitUntilEnabled(countrySelect);

        if (!country.isEmpty()) {
            logger.debug("Selecting country: {}", country);
            Select countryDropdown = new Select(wait.get().waitForVisibility(countrySelect));
            countryDropdown.selectByVisibleText(country);
            wait.get().waitUntilEnabled(regionSelect);
        }

        if (!region.isEmpty()) {
            logger.debug("Selecting region: {}", region);
            Select regionDropdown = new Select(wait.get().waitForVisibility(regionSelect));
            regionDropdown.selectByVisibleText(region);
        }
    }

    /**
     * Click continue button to proceed the next step
     * @param type one of: "payment", "shipping-address", "payment-method", "shipping-method"
     */
    public void clickContinue(String type) {
        logger.info("Clicking continue button for step: {}", type);
        click(continueLocator(type));
    }

    /**
     * Returns the Continue button locator based on the current step.
     */
    private By continueLocator(String step) {
        return switch (step.toLowerCase()) {
            case "payment" -> By.id("button-payment-address");
            case "payment-method" -> By.id("button-payment-method");
            case "shipping-address" -> By.id("button-shipping-address");
            case "shipping-method" -> By.id("button-shipping-method");
            default -> {
                logger.error("Unsupported step: {}", step);
                throw new IllegalArgumentException("Unsupported step: " + step);
            }
        };
    }

    /**
     * Validations
     */
    public List<String> getBillingValidationErrors() {
        logger.debug("Collecting billing validation errors");
        List<String> errors = new ArrayList<>();

        if (isElementPresent(firstNameError)) {
            String error = getText(firstNameError);
            logger.warn("First name validation error: {}", error);
            errors.add(error);
        }
        if (isElementPresent(lastNameError)) {
            String error = getText(lastNameError);
            logger.warn("Last name validation error: {}", error);
            errors.add(error);
        }
        if (isElementPresent(address1Error)) {
            String error = getText(address1Error);
            logger.warn("Address line 1 validation error: {}", error);
            errors.add(error);
        }
        if (isElementPresent(cityError)) {
            String error = getText(cityError);
            logger.warn("City validation error: {}", error);
            errors.add(error);
        }

        if (errors.isEmpty()) {
            logger.debug("No validation errors found");
        } else {
            logger.warn("Found {} validation errors", errors.size());
        }

        return errors;
    }

    public boolean isBillingValidationErrorDisplayed() {
        boolean hasErrors = isElementPresent(firstNameError) || isElementPresent(lastNameError) ||
                isElementPresent(address1Error) || isElementPresent(cityError);
        logger.debug("Checking for billing validation errors: {}", hasErrors);
        return hasErrors;
    }

    public boolean validateTotalCalculation() {
        logger.info("Validating order total calculation");

        String subtotalText = getOrderSubtotal().replaceAll("[^0-9.]", "");
        String shippingText = getShippingCost().replaceAll("[^0-9.]", "");
        String totalText = getOrderTotal().replaceAll("[^0-9.]", "");

        try {
            double subtotal = Double.parseDouble(subtotalText);
            double shipping = Double.parseDouble(shippingText);
            double total = Double.parseDouble(totalText);

            logger.debug("Subtotal: {}, Shipping: {}, Total: {}", subtotal, shipping, total);

            boolean isValid = Math.abs((subtotal + shipping) - total) < 0.01;
            logger.info("Total calculation is {}valid", isValid ? "" : "not ");

            return isValid;
        } catch (NumberFormatException e) {
            logger.error("Error parsing order amounts: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Visibility of steps
     */
    public boolean isStep3Visible() {
        boolean isVisible = getCurrentUrl().contains("step=3") || isElementDisplayed(step3Accordion);
        logger.debug("Checking if Step 3 is visible: {}", isVisible);
        return isVisible;
    }

    public boolean isStep4Visible() {
        boolean isVisible = getCurrentUrl().contains("step=4") || isElementDisplayed(step4Accordion);
        logger.debug("Checking if Step 4 is visible: {}", isVisible);
        return isVisible;
    }

    public boolean isStep5Visible() {
        boolean isVisible = getCurrentUrl().contains("step=5") || isElementDisplayed(step5Accordion);
        logger.debug("Checking if Step 5 is visible: {}", isVisible);
        return isVisible;
    }

    public boolean isStep6Visible() {
        boolean isVisible = getCurrentUrl().contains("step=6") || isElementDisplayed(step6Accordion);
        logger.debug("Checking if Step 6 is visible: {}", isVisible);
        return isVisible;
    }


    /**
     * Select methods
     */
    public void selectFlatRateDelivery() {
        logger.info("Selecting flat rate delivery option");
        click(flatRateOption);
    }

    public void selectBankTransfer() {
        logger.info("Selecting bank transfer payment method");
        click(bankTransferOption);
    }

    public void acceptTerms() {
        if (!wait.get().waitForVisibility(termsCheckbox).isSelected()) {
            logger.info("Accepting terms and conditions");
            click(termsCheckbox);
        } else {
            logger.debug("Terms and conditions already accepted");
        }
    }

    public void selectCashOnDelivery() {
        logger.info("Selecting cash on delivery payment method");
        click(cashOnDeliveryOption);
    }

    /**
     * Add comment to delivery
     */
    public void addDeliveryComments(String comments) {
        logger.info("Adding delivery comments: {}", comments);
        type(deliveryCommentsTextarea, comments);
    }


    /**
     * Display methods
     */

    public boolean isTermsErrorDisplayed() {
        boolean isDisplayed = isElementPresent(termsError);
        logger.debug("Checking if terms error is displayed: {}", isDisplayed);
        return isDisplayed;
    }


    /**
     * Get success/error messages
     */

    public String getTermsErrorMessage() {
        String message = getText(termsError);
        logger.warn("Terms error message: {}", message);
        return message;
    }

    public boolean isOrderSuccessful() {
        boolean isSuccessful = isElementDisplayed(successMessage);
        logger.info("Checking if order was successful: {}", isSuccessful);
        return isSuccessful;
    }

    public String getSuccessMessage() {
        String message = getText(successMessage);
        logger.info("Order success message: {}", message);
        return message;
    }

    public void clickConfirmOrder() {
        logger.info("Clicking confirm order button");
        click(confirmOrderButton);
    }


    /**
     * Order confirmation numbers
     */
    public String getOrderTotal() {
        String total = getText(orderTotal);
        logger.debug("Order total: {}", total);
        return total;
    }

    public String getOrderSubtotal() {
        String subtotal = getText(orderSubtotal);
        logger.debug("Order subtotal: {}", subtotal);
        return subtotal;
    }

    public String getShippingCost() {
        String shipping = getText(shippingCost);
        logger.debug("Shipping cost: {}", shipping);
        return shipping;
    }



    public boolean isNewAddressSelectionRequired(String type) {
        By locator = type.equals("payment") ? useNewPaymentAddressRadio : useNewShippingAddressRadio;
        return isElementPresent(locator);
    }

}