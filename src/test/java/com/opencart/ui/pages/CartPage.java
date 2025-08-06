package com.opencart.ui.pages;

import com.opencart.api.clients.ApiRoutes;
import com.opencart.ui.base.BasePage;
import com.opencart.utils.ConfigReader;
import com.opencart.utils.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(CartPage.class);

    // XPath Locators for dynamic product handling
    private final String productRowXpath = "//table[contains(@class,'table-bordered')]//td[contains(@class,'text-left')]/a[normalize-space()='%s']";
    private final String productNameXpath = "//form//tr[td/a[normalize-space()='%s']]//td[@class='text-left']/a";
    private final String productModelXpath = "//form//tr[td[a[normalize-space()='%s']]]//td[@class='text-left'][2]";
    private final String quantityInputXpath = "//tr[td[a[normalize-space()='%s']]]//input[@type='text' and contains(@class, 'form-control')]";
    private final String unitPriceXpath = "//form//tr[td[a[normalize-space()='%s']]]//td[@class='text-right'][1]";
    private final String totalPriceXpath = "//form//tr[td[a[normalize-space()='%s']]]//td[@class='text-right'][2]";
    private final String updateButtonXpath = "//tr[td[a[normalize-space()='%s']]]//button[@class='btn btn-primary' and contains(@data-original-title, 'Update')]";
    private final String removeButtonXpath = "//tr[td[a[normalize-space()='%s']]]//button[@class='btn btn-danger' and contains(@onclick, 'cart.remove')]";
    private final String outOfStockMarker = "//tr[td[a[normalize-space()='%s']]]//span[@class='text-danger' and normalize-space()='***']";

    // Static locators
    private final By emptyCartMessage = By.xpath("//div[@id='content']/p[contains(text(), 'Your shopping cart is empty!')]");
    private final By emptyCartMessageLocator = By.cssSelector("#content p");
    private final By subtotal = By.xpath("//tr[td[strong[normalize-space()='Sub-Total:']]]/td[@class='text-right'][2]");
    private final By ecoTax = By.xpath("//tr[td[strong[normalize-space()='Eco Tax:']]]/td[@class='text-right'][2]");
    private final By vat = By.xpath("//tr[td[strong[normalize-space()='VAT:']]]/td[@class='text-right'][2]");
    private final By grandTotal = By.xpath("//tr[td[strong[normalize-space()='Total:']]]/td[@class='text-right'][2]");
    private final By checkoutButton = By.linkText("Checkout");
    private final By allProducts = By.xpath("//table[contains(@class,'table-bordered')]//td[contains(@class,'text-left')]/a");
    private final By stockWarning = By.xpath("//div[contains(@class,'alert-danger') and contains(text(), 'not available in the desired quantity')]");
    private final By successAlert = By.cssSelector("div.alert-success");

    public CartPage() {
        super();
        logger.info("CartPage initialized");
    }

    /** Get product name by its label */
    public String getProductName(String productName) {
        return getText(By.xpath(String.format(productNameXpath, productName)));
    }

    /** Get model name of product */
    public String getProductModel(String productName) {
        return getText(By.xpath(String.format(productModelXpath, productName)));
    }

    /** Get current quantity of a product */
    public int getQuantity(String productName) {
        String qty = getAttribute(By.xpath(String.format(quantityInputXpath, productName)), "value");
        return Integer.parseInt(qty);
    }

    /** Get unit price of product as integer */
    public int getUnitPrice(String productName) {
        return priceAsInteger(getText(By.xpath(String.format(unitPriceXpath, productName))));
    }

    /** Get total price of product as integer */
    public int getTotalPrice(String productName) {
        return priceAsInteger(getText(By.xpath(String.format(totalPriceXpath, productName))));
    }

    /** Update quantity of a given product */
    public void updateQuantity(String productName, int newQuantity) {
        logger.info("Updating quantity for '{}': {}", productName, newQuantity);
        type(By.xpath(String.format(quantityInputXpath, productName)), String.valueOf(newQuantity));
        click(By.xpath(String.format(updateButtonXpath, productName)));
    }

    /** Convert price string to integer */
    public int priceAsInteger(String priceText) {
        if (priceText == null || priceText.isEmpty()) {
            throw new IllegalArgumentException("Price text is null or empty");
        }
        String clean = priceText.replaceAll("[^0-9.]", "");
        return Integer.parseInt(clean.split("\\.")[0]);
    }

    /** Remove product from cart by setting quantity to 0 */
    public void removeProductBySettingQuantityToZero(String productName) {
        updateQuantity(productName, 0);
    }

    /** Remove product from cart using delete button */
    public void removeProductViaDeleteButton(String productName) {
        logger.info("Removing product '{}' using delete button", productName);
        click(By.xpath(String.format(removeButtonXpath, productName)));
    }

    /** Remove all products except one */
    public void removeAllExcept(String productToKeep) {
        List<String> allProducts = getAllProductNamesInCart();
        for (String product : allProducts) {
            if (!product.equalsIgnoreCase(productToKeep)) {
                removeProductViaDeleteButton(product);
            }
        }
    }

    /** Clear all products from cart */
    public void clearCartIfNotEmpty() {
        navigateToCart();
        while (true) {
            List<WebElement> productElements = driver.get().findElements(allProducts);
            if (productElements.isEmpty()) {
                logger.info("Cart is already empty");
                break;
            }
            try {
                String productName = productElements.get(0).getText().trim();
                logger.info("Removing product: {}", productName);
                removeProductViaDeleteButton(productName);
                wait.get().waitUntilInvisibilityOfElementLocated(By.xpath(String.format(productRowXpath, productName)));
            } catch (StaleElementReferenceException e) {
                logger.warn("Stale element encountered. Retrying cart update");
            }
        }
    }

    /** Check if empty cart message is shown */
    public boolean isEmptyCartMessageDisplayed() {
        try {
            return isElementDisplayed(emptyCartMessage);
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Check if product is in cart */
    public boolean isProductInCart(String productName) {
        return isElementPresent(By.xpath(String.format(productRowXpath, productName)));
    }

    /** Verify only one product is in cart */
    public boolean isOnlyProductInCart(String expectedProduct) {
        List<String> productsInCart = getAllProductNamesInCart();
        return productsInCart.size() == 1 && productsInCart.get(0).equalsIgnoreCase(expectedProduct);
    }

    /** Get message shown in empty cart */
    public String getEmptyCartMessage() {
        return getText(emptyCartMessageLocator);
    }

    /** Return list of all product names in cart */
    public List<String> getAllProductNamesInCart() {
        List<WebElement> productElements = driver.get().findElements(allProducts);
        if (productElements.isEmpty()) {
            logger.info("No products in cart");
        }
        return productElements.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /** Check if product is marked out of stock */
    public boolean isProductMarkedOutOfStock(String productName) {
        return isElementPresent(By.xpath(String.format(outOfStockMarker, productName)));
    }

    /** Check if quantity warning is shown */
    public boolean isStockWarningDisplayed() {
        return isElementDisplayed(stockWarning);
    }

    /** Get success message from alert */
    public String getSuccessMessage() {
        return getText(successAlert).trim();
    }

    /** Proceed to checkout */
    public void proceedToCheckout() {
        click(checkoutButton);
        wait.get().waitForUrlContains("checkout/checkout");
    }

    /** Navigate to cart page */
    public void navigateToCart() {
        DriverFactory.getDriver().navigate().to(ConfigReader.getProperty("baseUrl") + "index.php?route=checkout/cart");
        logger.info("Navigated to: {}", DriverFactory.getDriver().getCurrentUrl());
    }

    /** Get subtotal text */
    public String getSubtotal() {
        return getText(subtotal);
    }

    /** Get Eco Tax text */
    public String getEcoTax() {
        return getText(ecoTax);
    }

    /** Get VAT text */
    public String getVAT() {
        return getText(vat);
    }

    /** Get grand total text */
    public String getGrandTotal() {
        return getText(grandTotal);
    }
}
