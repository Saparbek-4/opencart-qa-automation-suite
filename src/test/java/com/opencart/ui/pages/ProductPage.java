package com.opencart.ui.pages;

import com.opencart.ui.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(ProductPage.class);

    // --- Locators ---
    private final String productLink = "//div[@class='product-thumb' and .//h4[a[normalize-space(text())='%s']]]//h4/a";
    private final By productTitle = By.xpath("//div[@id='content']//h1");
    private final By productPrice = By.xpath("//ul[contains(@class, 'list-unstyled')]//h2[contains(text(), '$')]");
    private final By productImage = By.cssSelector(".thumbnail img");
    private final By breadcrumb = By.cssSelector("ul.breadcrumb");
    private final By productCode = By.xpath("//li[contains(text(), 'Product Code:')]");
    private final By availability = By.xpath("//li[contains(text(), 'Availability:')]");
    private final By brand = By.xpath("//li[contains(text(), 'Brand') or contains(text(), 'Brands')]");
    private final By taxInfo = By.xpath("//li[contains(text(), 'Ex Tax:')]");

    // --- Cart ---
    private final By addToCartButton = By.id("button-cart");
    private final By successAlert = By.cssSelector(".alert.alert-success");
    private final By errorAlert = By.cssSelector(".alert.alert-danger");

    // --- Options ---
    private final By requiredOptionDropdown = By.cssSelector("select[required]");
    private final By optionDropdown = By.cssSelector("select[name*='option']");
    private final By optionError = By.xpath("//div[@class='text-danger' and contains(text(), 'Size required!')]");
    private final By quantityInput = By.xpath("//input[@type='text' and contains(@id, 'input-quantity')]");

    // --- Review ---
    private final By reviewTab = By.xpath("//a[@href='#tab-review']");
    private final By reviewNameInput = By.cssSelector("#input-name");
    private final By reviewTextInput = By.cssSelector("#input-review");
    private final By reviewSubmitButton = By.cssSelector("#button-review");

    // --- Description tab ---
    private final By descriptionTab = By.xpath("//a[@href='#tab-description']");

    /**
     * Constructor
     */
    public ProductPage() {
        super();
        logger.info("✅ ProductPage instance created");
    }

    // ========================
    // 🔹 Navigation
    // ========================

    /**
     * Navigate to product from listing page
     */
    public void navigateToProduct(String productName) {
        logger.info("🧭 Navigating to product: {}", productName);
        click(By.xpath(String.format(productLink, productName)));
    }

    // ========================
    // 🔹 Product Information
    // ========================

    public String getProductTitle() {
        return getText(productTitle);
    }

    public String getProductPrice() {
        return getText(productPrice);
    }

    public String getTaxInfo() {
        try {
            return getText(taxInfo);
        } catch (Exception e) {
            logger.warn("No tax info found");
            return "No tax info available";
        }
    }

    public boolean isProductImageDisplayed() {
        return isElementDisplayed(productImage);
    }

    public boolean isBreadcrumbDisplayed() {
        return isElementDisplayed(breadcrumb);
    }

    public String getProductCode() {
        return getText(productCode);
    }

    public String getAvailability() {
        return getText(availability);
    }

    public String getBrand() {
        try {
            String brandText = getText(brand);
            if (brandText.contains("Brands ")) {
                return brandText.substring(brandText.indexOf("Brands ") + 7);
            }
            return brandText;
        } catch (Exception e) {
            logger.warn("Brand not found");
            return "Brand not found";
        }
    }

    // ========================
    // 🔹 Cart Operations
    // ========================

    /**
     * Clicks the "Add to Cart" button
     */
    public void clickAddToCart() {
        logger.info("🛒 Clicking Add to Cart");
        click(addToCartButton);
    }

    /**
     * Sets quantity before adding to cart
     */
    public void setQuantity(int quantity) {
        logger.debug("Setting product quantity: {}", quantity);
        type(quantityInput, String.valueOf(quantity));
    }

    public String getSuccessMessage() {
        try {
            return getText(successAlert).trim();
        } catch (Exception e) {
            logger.warn("No success message found");
            return "";
        }
    }

    public String getErrorMessage() {
        try {
            return getText(errorAlert).trim();
        } catch (Exception e) {
            logger.warn("No error message found");
            return "";
        }
    }

    public boolean isSuccessAlertDisplayed() {
        return isElementDisplayed(successAlert);
    }

    public boolean isErrorAlertDisplayed() {
        return isElementDisplayed(errorAlert);
    }

    // ========================
    // 🔹 Product Options
    // ========================

    public boolean hasRequiredOptions() {
        return isElementPresent(requiredOptionDropdown);
    }

    public void selectSizeOption(String size) {
        if (isElementPresent(optionDropdown)) {
            WebElement dropdown = wait.get().waitForVisibility(optionDropdown);
            Select select = new Select(dropdown);
            logger.info("🔽 Selecting size option: {}", size);
            select.selectByValue(String.valueOf(size.equalsIgnoreCase("medium") ? 13 : 14));
        }
    }

    public boolean isOptionErrorDisplayed() {
        return isElementDisplayed(optionError);
    }

    public String getOptionErrorMessage() {
        try {
            return getText(optionError);
        } catch (Exception e) {
            return "";
        }
    }

    // ========================
    // 🔹 Review Section
    // ========================

    public void clickReviewTab() {
        logger.info("📝 Opening review tab");
        click(reviewTab);
    }

    public void fillReviewForm(String name, String reviewText, String rating) {
        logger.info("📝 Filling review: Name={}, Rating={}", name, rating);

        if (!name.isEmpty()) type(reviewNameInput, name);
        type(reviewTextInput, reviewText);

        if (!rating.isEmpty()) {
            By ratingRadio = By.cssSelector("input[name='rating'][value='" + rating + "']");
            click(ratingRadio);
        }
    }

    public void submitReview() {
        logger.info("📤 Submitting product review");
        click(reviewSubmitButton);
    }

    // ========================
    // 🔹 UI Element Visibility
    // ========================

    public boolean isAddToCartButtonDisplayed() {
        return isElementDisplayed(addToCartButton);
    }

    public boolean isPriceDisplayed() {
        return isElementDisplayed(productPrice);
    }

    public boolean isDescriptionTabFunctional() {
        try {
            click(descriptionTab);
            By descriptionContent = By.id("tab-description");
            return isElementDisplayed(descriptionContent);
        } catch (Exception e) {
            logger.warn("Description tab not functional");
            return false;
        }
    }

    // ========================
    // 🔹 Page Validation
    // ========================

    public boolean isProductPageLoaded() {
        try {
            wait.get().waitForVisibility(productTitle);
            wait.get().waitForVisibility(productPrice);
            logger.info("✅ Product page successfully loaded");
            return true;
        } catch (Exception e) {
            logger.error("❌ Product page failed to load", e);
            return false;
        }
    }
}
