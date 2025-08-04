package com.opencart.ui.pages;

import com.opencart.ui.base.BasePage;
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

    // Locators
    private final String productRowXpath = "//table[contains(@class,'table-bordered')]//td[contains(@class,'text-left')]/a[normalize-space()='%s']";
    private final String productNameXpath = "//form//tr[td[a[normalize-space()='%s']]]//td[@class='text-left'][1]";
    private final String productModelXpath = "//form//tr[td[a[normalize-space()='%s']]]//td[@class='text-left'][2]";
    private final String quantityInputXpath = "//tr[td[a[normalize-space()='%s']]]//input[@type='text' and contains(@class, 'form-control')]";
    private final String unitPriceXpath = "//form//tr[td[a[normalize-space()='%s']]]//td[@class='text-right'][1]";
    private final String totalPriceXpath = "//form//tr[td[a[normalize-space()='%s']]]//td[@class='text-right'][2]";
    private final String updateButtonXpath = "//tr[td[a[normalize-space()='%s']]]//button[@class='btn btn-primary' and contains(@data-original-title, 'Update')]";
    private final String removeButtonXpath = "//tr[td[a[normalize-space()='%s']]]//button[@class='btn btn-danger' and contains(@onclick, 'cart.remove')]";
    private final String outOfStockMarker = "//tr[td[a[normalize-space()='%s']]]//span[@class='text-danger' and normalize-space()='***']";

    private final By emptyCartMessage = By.xpath("//div[@id='content']/p[contains(text(), 'Your shopping cart is empty!')]");
    private final By subtotal = By.xpath("//tr[td[strong[normalize-space()='Sub-Total:']]]/td[@class='text-right'][2]");
    private final By ecoTax = By.xpath("//tr[td[strong[normalize-space()='Eco Tax:']]]/td[@class='text-right'][2]");
    private final By vat = By.xpath("//tr[td[strong[normalize-space()='VAT:']]]/td[@class='text-right'][2]");
    private final By grandTotal = By.xpath("//tr[td[strong[normalize-space()='Total:']]]/td[@class='text-right'][2]");
    private final By checkoutButton = By.linkText("Checkout");
    private final By cartLink = By.cssSelector("a[title='Shopping Cart']");
    private final By allProducts = By.xpath("//table[contains(@class,'table-bordered')]//td[contains(@class,'text-left')]/a");
    private final By emptyCartMessageLocator = By.cssSelector("#content p");
    private final By stockWarning = By.xpath("//div[contains(@class,'alert-danger') and contains(text(), 'not available in the desired quantity')]");
    private final By successAlert = By.cssSelector("div.alert-success");



    public CartPage() {
        super();
    }

    /*---------Product Info---------*/
    public String getProductName(String productName) {
        return getText(By.xpath(String.format(productNameXpath, productName)));
    }

    public String getProductModel(String productName) {
        return getText(By.xpath(String.format(productModelXpath, productName)));
    }

    /*---------Quantity and Price---------*/
    public int getQuantity(String productName) {
        String priceText = getAttribute(By.xpath(String.format(quantityInputXpath, productName)), "value");
        return Integer.parseInt(priceText);
    }

    public String getUnitPrice(String productName) {
        return getText(By.xpath(String.format(unitPriceXpath, productName)));
    }

    public String getTotalPrice(String productName) {
        return getText(By.xpath(String.format(totalPriceXpath, productName)));
    }
    public void updateQuantity(String productName, int newQuantity) {
        type(By.xpath(String.format(quantityInputXpath, productName)), String.valueOf(newQuantity));
        click(By.xpath(String.format(updateButtonXpath, productName)));
    }

    public String getSubtotal() {
        return getText(subtotal);
    }

    public String getEcoTax() {
        return getText(ecoTax);
    }

    public String getVAT() {
        return getText(vat);
    }

    public String getGrandTotal() {
        return getText(grandTotal);
    }

    /*---------Removal Methods---------*/
    public void removeProductBySettingQuantityToZero(String productName) {
        updateQuantity(productName, 0);
    }

    public void removeProductViaDeleteButton(String productName) {
        click(By.xpath(String.format(removeButtonXpath, productName)));
    }

    public void removeProductFromCart(String productName) {
        By removeButton = By.xpath(String.format(removeButtonXpath, productName));
        click(removeButton);
    }

    public void removeAllExcept(String productToKeep) {
        List<String> allProducts = getAllProductNamesInCart();

        for (String product : allProducts) {
            if (!product.equalsIgnoreCase(productToKeep)) {
                removeProductViaDeleteButton(product);
            }
        }
    }

    public void clearCartIfNotEmpty() {
        navigateToCart();

        while (true) {
            List<WebElement> productElements = driver.get().findElements(allProducts);

            if (productElements.isEmpty()) {
                logger.info("Cart is already empty.");
                break;
            }

            try {
                String productName = productElements.get(0).getText().trim();
                logger.info("Removing product from cart: {}", productName);

                removeProductFromCart(productName);

                wait.get().waitUntilInvisibilityOfElementLocated(
                        By.xpath(String.format(productRowXpath, productName))
                );

            } catch (StaleElementReferenceException e) {
                logger.warn("Stale element encountered. Re-fetching cart contents.");
            }
        }
    }

    /*---------Cart Info and Status---------*/
    public boolean isEmptyCartMessageDisplayed() {
        try {
            return isElementDisplayed(emptyCartMessage);
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isProductInCart(String productName) {
        By productLocator = By.xpath(String.format(productRowXpath, productName));
        return isElementPresent(productLocator);
    }

    public boolean isOnlyProductInCart(String expectedProduct) {
        List<String> productsInCart = getAllProductNamesInCart();
        return productsInCart.size() == 1 && productsInCart.get(0).equalsIgnoreCase(expectedProduct);
    }

    public String getEmptyCartMessage() {
        return getText(emptyCartMessageLocator);
    }

    public List<String> getAllProductNamesInCart() {
        List<WebElement> productElements = driver.get().findElements(allProducts);
        if (productElements.isEmpty()) {
            logger.info("No products found in the cart.");
        }
        return productElements.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public boolean isProductMarkedOutOfStock(String productName) {
        return isElementPresent(By.xpath(String.format(outOfStockMarker, productName)));
    }

    public boolean isStockWarningDisplayed() {
        return isElementDisplayed(stockWarning);
    }

    public String getSuccessMessage() {
        return getText(successAlert).trim();
    }

    /*---------Navigation---------*/
    public void proceedToCheckout() {
        click(checkoutButton);
        wait.get().waitForUrlContains("checkout/checkout");
    }

    public void navigateToCart() {
        click(cartLink);
    }

}