package com.opencart.ui.pages;

import com.opencart.ui.base.BasePage;
import com.opencart.utils.ConfigReader;
import com.opencart.utils.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class WishlistPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(WishlistPage.class);

    // --- Locators ---
    private final String productRowXpath = "//table[contains(@class,'table-bordered')]//a[normalize-space()='%s']";
    private final String removeButtonXpath = "//a[normalize-space()='%s']/ancestor::tr//a[contains(@data-original-title, 'Remove')]";
    private final String cartButtonXpath = "//a[normalize-space()='%s']/ancestor::tr//button[contains(@data-original-title, 'Add to Cart')]";
    private final String stockQuantityXpath = "//div[@id='content']//table[contains(@class,'table-bordered')]//tr[td[a[normalize-space()='%s']]]/td[4]";
    private final String productModelXpath = "//div[@id='content']//table[contains(@class,'table-bordered')]//tr[td[a[normalize-space()='%s']]]/td[3]";
    private final By successAlert = By.cssSelector("div.alert-success");
    private final By allProducts = By.xpath("//table[contains(@class,'table-bordered')]//td[contains(@class,'text-left')]/a");

    /**
     * Constructor
     */
    public WishlistPage() {
        super();
        logger.info("✅ WishlistPage instance created");
    }

    // ========================
    // 🔹 Navigation
    // ========================

    /**
     * Navigate directly to Wishlist Page
     */
    public void navigateToWishlist() {
        logger.info("🧭 Navigating to Wishlist page");
        DriverFactory.getDriver().get(ConfigReader.getProperty("baseUrl") + "index.php?route=account/wishlist");
    }

    // ========================
    // 🔹 Validation
    // ========================

    /**
     * Checks if a product exists in the wishlist
     */
    public boolean isProductInWishlist(String productName) {
        By productLocator = By.xpath(String.format(productRowXpath, productName));
        return isElementPresent(productLocator);
    }

    /**
     * Checks if 'Add to Cart' button is visible for a given product
     */
    public boolean isAddToCartButtonVisible(String productName) {
        return isElementPresent(By.xpath(String.format(cartButtonXpath, productName)));
    }

    /**
     * Checks if 'Remove' button is visible for a given product
     */
    public boolean isRemoveButtonVisible(String productName) {
        return isElementPresent(By.xpath(String.format(removeButtonXpath, productName)));
    }

    // ========================
    // 🔹 Actions
    // ========================

    /**
     * Removes a product from the wishlist
     */
    public void removeProduct(String productName) {
        logger.info("❌ Removing product from wishlist: {}", productName);
        By removeButton = By.xpath(String.format(removeButtonXpath, productName));
        click(removeButton);
    }

    /**
     * Adds a wishlist product to the cart
     */
    public void clickAddToCart(String productName) {
        logger.info("🛒 Adding product to cart from wishlist: {}", productName);
        click(By.xpath(String.format(cartButtonXpath, productName)));
    }

    /**
     * Removes all wishlist items except the specified one
     */
    public void removeAllExcept(String productToKeep) {
        List<String> allItems = getAllProductNamesInWishList();
        for (String product : allItems) {
            if (!product.equalsIgnoreCase(productToKeep)) {
                logger.info("🧹 Removing '{}' (keeping '{}')", product, productToKeep);
                removeProduct(product);
            }
        }
    }

    // ========================
    // 🔹 Data Extraction
    // ========================

    /**
     * Gets list of all product names in wishlist
     */
    public List<String> getAllProductNamesInWishList() {
        List<WebElement> productElements = driver.get().findElements(allProducts);
        if (productElements.isEmpty()) {
            logger.info("ℹ️ No products found in wishlist.");
        }
        return productElements.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /**
     * Gets product model for a given product
     */
    public String getModel(String productName) {
        return getText(By.xpath(String.format(productModelXpath, productName)));
    }

    /**
     * Gets the current stock quantity of a given wishlist product
     */
    public int getStock(String productName) {
        try {
            return Integer.parseInt(getText(By.xpath(String.format(stockQuantityXpath, productName))));
        } catch (NumberFormatException e) {
            logger.warn("⚠️ Stock is not numeric for '{}': might be Out of Stock", productName);
            return 0;
        }
    }

    /**
     * Gets number of product entries in wishlist with the same name
     */
    public int getWishlistProductCount(String productName) {
        List<WebElement> productElements = driver.get().findElements(By.xpath(String.format(productRowXpath, productName)));
        return productElements.size();
    }

    /**
     * Retrieves the latest success message
     */
    public String getSuccessMessage() {
        return getText(successAlert).trim();
    }
}
