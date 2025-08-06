package com.opencart.ui.pages;

import com.opencart.ui.base.BasePage;
import com.opencart.utils.ConfigReader;
import com.opencart.utils.DriverFactory;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    // --- Locators ---
    private final By logo = By.cssSelector("#logo a");
    private final By imageSlider = By.id("slideshow0");
    private final By footer = By.cssSelector("footer");

    private final By currencySelector = By.cssSelector("button.btn-link.dropdown-toggle");
    private final By contactNumber = By.xpath("//i[@class='fa fa-phone']/..");
    private final By myAccount = By.linkText("My Account");
    private final By wishList = By.id("wishlist-total");
    private final By shoppingCart = By.cssSelector("a[title='Shopping Cart']");
    private final By checkout = By.linkText("Checkout");

    private final By desktops = By.linkText("Desktops");
    private final By laptops = By.linkText("Laptops & Notebooks");
    private final By tablets = By.linkText("Tablets");

    private final By searchInput = By.name("search");
    private final By searchButton = By.cssSelector("button.btn.btn-default.btn-lg");

    private final By macbookProduct = By.xpath("//h4[a[text()='MacBook']]");
    private final By iPhoneProduct = By.xpath("//h4[a[text()='iPhone']]");
    private final By addToCartButtons = By.xpath("//button[contains(@onclick, 'cart.add')]");

    private final String cartButtonXpath = "//div[@class='product-thumb' and .//h4[a[normalize-space(text())='%s']]]//button[contains(@onclick, 'cart.add')]";
    private final String wishlistButtonXpath = "//div[@class='product-thumb' and .//h4[a[normalize-space(text())='%s']]]//button[contains(@onclick, 'wishlist.add')]";
    private final String productLinkXpath = "//div[@class='product-thumb' and .//h4[a[normalize-space(text())='%s']]]";

    private final By aboutUs = By.linkText("About Us");
    private final By contactUs = By.linkText("Contact Us");
    private final By brands = By.linkText("Brands");
    private final By myAccountFooter = By.linkText("My Account");

    private final By successAlert = By.cssSelector("div.alert-success");

    /**
     * Constructor for HomePage - ensures proper setup
     */
    public HomePage() {
        super();
        logger.info("✅ HomePage object initialized");
    }

    /**
     * Navigates to the home page URL from config
     */
    public void navigateToHomePage() {
        String baseUrl = ConfigReader.getProperty("baseUrl");
        logger.info("Navigating to home page: {}", baseUrl);
        DriverFactory.getDriver().get(baseUrl);
    }

    /**
     * Searches for a product using the top search bar
     * @param keyword product name or keyword
     */
    public void searchProduct(String keyword) {
        logger.info("🔍 Searching for product: {}", keyword);
        type(searchInput, keyword);
        click(searchButton);
    }

    /**
     * Adds product to cart by name
     * @param productName product to add
     */
    public void addProductToCart(String productName) {
        logger.info("🛒 Adding product to cart: {}", productName);
        click(By.xpath(String.format(cartButtonXpath, productName)));
    }

    /**
     * Adds product to wishlist by name
     * @param productName product to add
     */
    public void addToWishlist(String productName) {
        logger.info("💖 Adding product to wishlist: {}", productName);
        click(By.xpath(String.format(wishlistButtonXpath, productName)));
    }

    /**
     * Clicks on product from search results by name
     * @param productName product to select
     */
    public void selectProductFromSearchResults(String productName) {
        logger.info("🧭 Selecting product from search results: {}", productName);
        click(By.xpath(String.format(productLinkXpath, productName)));
    }

    // --- UI Element Validators ---
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

    /**
     * Gets trimmed text from success alert banner
     * @return success message text
     */
    public String getSuccessMessage() {
        String message = getText(successAlert).trim();
        logger.info("✅ Success message displayed: {}", message);
        return message;
    }
}
