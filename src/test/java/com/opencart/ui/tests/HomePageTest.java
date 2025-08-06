package com.opencart.ui.tests;

import com.opencart.ui.base.BaseTest;
import com.opencart.ui.pages.LoginPage;
import com.opencart.utils.UserPoolManager;
import groovy.util.logging.Log;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.opencart.ui.pages.HomePage;
import com.opencart.utils.WaitUtils;

public class HomePageTest extends BaseTest {

    private HomePage homePage;
    private LoginPage loginPage;

    @Override
    public void setupTestData() {
        homePage = new HomePage();
        loginPage = new LoginPage();

        loginPage.navigateToLoginPage();
        new WaitUtils().waitForPageLoad();

        String email = UserPoolManager.acquireUser();
        String password = getProp().getProperty("testUserPassword");

        loginPage.login(email, password);

        homePage.navigateToHomePage();
        new WaitUtils().waitForPageLoad();
    }

    @Test(priority = 1, description = "TC_001: Home Page Load and Layout Verification")
    public void testHomePageLoadAndLayout() {
        Assert.assertTrue(homePage.isLogoVisible(), "Logo not visible");
        Assert.assertTrue(homePage.isSliderVisible(), "Image slider not visible");
        Assert.assertTrue(homePage.isFooterVisible(), "Footer not visible");
    }

    @Test(priority = 2, description = "TC_002: Header Functionality Verification")
    public void testHeaderFunctionality() {
        Assert.assertTrue(homePage.isCurrencySelectorVisible(), "Currency selector not visible");
        Assert.assertTrue(homePage.isContactNumberVisible(), "Contact number not visible");
        Assert.assertTrue(homePage.isMyAccountVisible(), "My Account not visible");
        Assert.assertTrue(homePage.isWishListVisible(), "Wish List not visible");
        Assert.assertTrue(homePage.isShoppingCartVisible(), "Shopping Cart not visible");
    }

    @Test(priority = 3, description = "TC_003: Navigation Menu Verification")
    public void testNavigationMenu() {
        Assert.assertTrue(homePage.isDesktopsVisible(), "Desktops not visible");
        Assert.assertTrue(homePage.isLaptopsVisible(), "Laptops not visible");
        Assert.assertTrue(homePage.isTabletsVisible(), "Tablets not visible");
    }

    @Test(priority = 4, description = "TC_004: Search Functionality")
    public void testSearchFunctionality() {
        homePage.searchProduct("iPhone");
        Assert.assertTrue(homePage.getCurrentUrl().contains("search=iPhone"), "Search did not work properly");
    }

    @Test(priority = 5, description = "TC_005: Featured Products Section")
    public void testFeaturedProducts() {
        Assert.assertTrue(homePage.isMacbookVisible(), "MacBook not found");
        Assert.assertTrue(homePage.isIPhoneVisible(), "iPhone not found");
        Assert.assertTrue(homePage.areAddToCartButtonsVisible(), "Add to Cart buttons missing");
    }

    @Test(priority = 6, description = "TC_006: Add to Cart Functionality")
    public void testAddToCartFunctionality() {
        homePage.searchProduct("iPhone");
        homePage.addProductToCart("iPhone");
        Assert.assertTrue(homePage.getSuccessMessage()
                .contains("You have added iPhone to your shopping cart!"),
                "Cart did not update after adding iPhone");
    }

    @Test(priority = 7, description = "TC_007: Footer Section and External Links")
    public void testFooterLinks() {
        Assert.assertTrue(homePage.isAboutUsVisible(), "About Us link missing");
        Assert.assertTrue(homePage.isContactUsVisible(), "Contact Us link missing");
        Assert.assertTrue(homePage.isBrandsVisible(), "Brands link missing");
        Assert.assertTrue(homePage.isMyAccountFooterVisible(), "My Account link in footer missing");
    }
}

