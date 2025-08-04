package com.opencart.ui.pages;

import com.opencart.ui.base.BasePage;
import org.openqa.selenium.By;

public class HomePage extends BasePage {

    // --- Locators ---
    private final By logo = By.cssSelector("#logo a");
    private final By imageSlider = By.id("slideshow0");
    private final By footer = By.cssSelector("footer");

    private final By currencySelector = By.cssSelector("button.btn-link.dropdown-toggle");
    private final By contactNumber = By.xpath("//i[@class='fa fa-phone']/..");
    private final By myAccount = By.linkText("My Account");
    private final By wishList =  By.id("wishlist-total");
    private final By shoppingCart =  By.cssSelector("a[title='Shopping Cart']");
    private final By checkout = By.linkText("Checkout");

    private final By desktops = By.linkText("Desktops");
    private final By laptops = By.linkText("Laptops & Notebooks");
    private final By tablets = By.linkText("Tablets");

    private final By searchInput = By.name("search");
    private final By searchButton = By.cssSelector("button.btn.btn-default.btn-lg");

    private final By macbookProduct = By.xpath("//h4[a[text()='MacBook']]");
    private final By iPhoneProduct = By.xpath("//h4[a[text()='MacBook']]");
    private final By addToCartButtons = By.xpath("//button[contains(@onclick, 'cart.add')]");

    private final String cartButtonXpath = "//div[@class='product-thumb' and .//h4[a[normalize-space(text())='%s']]]//button[contains(@onclick, 'cart.add')]";
    private final String wishlistButtonXpath = "//div[@class='product-thumb' and .//h4[a[normalize-space(text())='%s']]]//button[contains(@onclick, 'wishlist.add')]";
    private final String productLinkXpath = "//div[@class='product-thumb' and .//h4[a[normalize-space(text())='%s']]]";
    private final By aboutUs = By.linkText("About Us");
    private final By contactUs = By.linkText("Contact Us");
    private final By brands = By.linkText("Brands");
    private final By myAccountFooter = By.linkText("My Account");

    private final By successAlert = By.cssSelector("div.alert-success");

    // --- Constructor ---
    public HomePage() {
        super();
    }

    // --- Actions ---
    public void searchProduct(String keyword) {
        type(searchInput, keyword);
        click(searchButton);
    }

    // --- Getters for validation ---
    public boolean isLogoVisible() { return isElementDisplayed(logo); }
    public boolean isSliderVisible() { return isElementDisplayed(imageSlider); }
    public boolean isFooterVisible() { return isElementDisplayed(footer); }

    public boolean isCurrencySelectorVisible() { return isElementDisplayed(currencySelector); }
    public boolean isContactNumberVisible() { return isElementDisplayed(contactNumber); }
    public boolean isMyAccountVisible() { return isElementDisplayed(myAccount); }
    public boolean isWishListVisible() { return isElementDisplayed(wishList); }
    public boolean isShoppingCartVisible() { return isElementDisplayed(shoppingCart); }

    public boolean isDesktopsVisible() { return isElementDisplayed(desktops); }
    public boolean isLaptopsVisible() { return isElementDisplayed(laptops); }
    public boolean isTabletsVisible() { return isElementDisplayed(tablets); }

    public boolean isMacbookVisible() { return isElementDisplayed(macbookProduct); }
    public boolean isIPhoneVisible() { return isElementDisplayed(iPhoneProduct); }
    public boolean areAddToCartButtonsVisible() { return isElementDisplayed(addToCartButtons); }

    public boolean isAboutUsVisible() { return isElementDisplayed(aboutUs); }
    public boolean isContactUsVisible() { return isElementDisplayed(contactUs); }
    public boolean isBrandsVisible() { return isElementDisplayed(brands); }
    public boolean isMyAccountFooterVisible() { return isElementDisplayed(myAccountFooter); }

    public String getSuccessMessage() {
        return getText(successAlert).trim();
    }

    public void addProductToCart(String productName) {
        click(By.xpath(String.format(cartButtonXpath, productName)));
    }

    public void addToWishlist(String productName) {
        click(By.xpath(String.format(wishlistButtonXpath, productName)));
    }

    public void selectProductFromSearchResults(String productName) {
        click(By.xpath(String.format(productLinkXpath, productName)));
    }
}

