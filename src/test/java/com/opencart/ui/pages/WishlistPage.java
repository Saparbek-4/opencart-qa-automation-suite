package com.opencart.ui.pages;

import com.opencart.ui.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class WishlistPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(WishlistPage.class);


    private final By wishlistLink = By.id("wishlist-total");

    private final String productRowXpath = "//table[contains(@class,'table-bordered')]//a[normalize-space()='%s']";
    private final String removeButtonXpath = "//a[normalize-space()='%s']/ancestor::tr//a[contains(@data-original-title, 'Remove')]";
    private final String cartButtonXpath = "//a[normalize-space()='%s']/ancestor::tr//button[contains(@data-original-title, 'Add to Cart')]";
    private final String stockQuantityXpath = "//tr[td[a[normalize-space()='%s']]]//td[@class='text-right'][1]";
    private final String productModelXpath = "//form//tr[td[a[normalize-space()='%s']]]//td[@class='text-left'][2]";
    private final By successAlert = By.cssSelector("div.alert-success");
    private final By allProducts = By.xpath("//table[contains(@class,'table-bordered')]//td[contains(@class,'text-left')]/a");

    public WishlistPage() {
        super();
    }

    public void navigateToWishlist() {
        click(wishlistLink);
    }

    public boolean isProductInWishlist(String productName) {
        By productLocator = By.xpath(String.format(productRowXpath, productName));
        return isElementPresent(productLocator);
    }

    public void removeProduct(String productName) {
        By removeButton = By.xpath(String.format(removeButtonXpath, productName));
        click(removeButton);
    }

    public void clickAddToCart(String productName) {
        click(By.xpath(String.format(cartButtonXpath, productName)));
    }

    public boolean isAddToCartButtonVisible(String productName) {
        return isElementPresent(By.xpath(String.format(cartButtonXpath, productName)));
    }

    public boolean isRemoveButtonVisible(String productName) {
        return isElementPresent(By.xpath(String.format(removeButtonXpath, productName)));

    }

    public List<String> getAllProductNamesInWishList() {
        List<WebElement> productElements = driver.get().findElements(allProducts);
        if (productElements.isEmpty()) {
            logger.info("No products found in the wishlist.");
        }
        return productElements.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public int getStock(String productName) {
        try {
            return Integer.parseInt(getText(By.xpath(String.format(stockQuantityXpath, productName))));
        } catch (NumberFormatException e) {
            logger.warn("Stock not a number: might be 'Out of Stock'");
            return 0;
        }
    }

    public int getWishlistProductCount(String productName) {
        List<WebElement> productElements = driver.get().findElements(By.xpath(String.format(productRowXpath, productName)));
        return productElements.size();
    }

    public String getModel(String productName) {
        return getText(By.xpath(String.format(productModelXpath, productName)));

    }

    public String getSuccessMessage() {
        return getText(successAlert).trim();
    }

    public void removeAllExcept(String productToKeep) {
        List<String> allProducts = getAllProductNamesInWishList();

        for (String product : allProducts) {
            if (!product.equalsIgnoreCase(productToKeep)) {
                removeProduct(product);
            }
        }
    }
}
