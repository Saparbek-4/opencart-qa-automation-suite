package com.opencart.ui.tests;

import com.opencart.ui.base.BaseTest;
import com.opencart.ui.pages.LoginPage;
import com.opencart.utils.UserPoolManager;
import com.opencart.ui.pages.HomePage;
import com.opencart.utils.WaitUtils;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Home Page")
@Feature("UI Elements and Functional Components")
@Owner("saparbek.kozhanazar04@gmail.com")
@Tag("ui")
@Tag("regression")
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

    @Test(priority = 1, description = "TC_027: Home Page Load and Layout Verification",
            groups = {"ui", "regression"})
    @Story("User sees home page with all core elements")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-027")
    public void testHomePageLoadAndLayout() {
        Assert.assertTrue(homePage.isLogoVisible(), "Logo not visible");
        Assert.assertTrue(homePage.isSliderVisible(), "Image slider not visible");
        Assert.assertTrue(homePage.isFooterVisible(), "Footer not visible");
    }

    @Test(priority = 2, description = "TC_028: Header Functionality Verification",
            groups = {"ui", "regression"})
    @Story("User checks the header section for functional components")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-028")
    public void testHeaderFunctionality() {
        Assert.assertTrue(homePage.isCurrencySelectorVisible(), "Currency selector not visible");
        Assert.assertTrue(homePage.isContactNumberVisible(), "Contact number not visible");
        Assert.assertTrue(homePage.isMyAccountVisible(), "My Account not visible");
        Assert.assertTrue(homePage.isWishListVisible(), "Wish List not visible");
        Assert.assertTrue(homePage.isShoppingCartVisible(), "Shopping Cart not visible");
    }

    @Test(priority = 3, description = "TC_029: Navigation Menu Verification",
            groups = {"ui", "regression"})
    @Story("User verifies the visibility of main category menu items")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-029")
    public void testNavigationMenu() {
        Assert.assertTrue(homePage.isDesktopsVisible(), "Desktops not visible");
        Assert.assertTrue(homePage.isLaptopsVisible(), "Laptops not visible");
        Assert.assertTrue(homePage.isTabletsVisible(), "Tablets not visible");
    }

    @Test(priority = 4, description = "TC_030: Search Functionality",
            groups = {"ui", "regression"})
    @Story("User uses the search bar to find products")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-030")
    public void testSearchFunctionality() {
        homePage.searchProduct("iPhone");
        Assert.assertTrue(homePage.getCurrentUrl().contains("search=iPhone"), "Search did not work properly");
    }

    @Test(priority = 5, description = "TC_031: Featured Products Section",
            groups = {"ui", "regression"})
    @Story("User views featured products on homepage")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-031")
    public void testFeaturedProducts() {
        Assert.assertTrue(homePage.isMacbookVisible(), "MacBook not found");
        Assert.assertTrue(homePage.isIPhoneVisible(), "iPhone not found");
        Assert.assertTrue(homePage.areAddToCartButtonsVisible(), "Add to Cart buttons missing");
    }

    @Test(priority = 6, description = "TC_032: Add to Cart Functionality",
            groups = {"ui", "regression"})
    @Story("User adds a product to the cart from homepage")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("TC-032")
    public void testAddToCartFunctionality() {
        homePage.searchProduct("iPhone");
        homePage.addProductToCart("iPhone");
        Assert.assertTrue(homePage.getSuccessMessage()
                        .contains("You have added iPhone to your shopping cart!"),
                "Cart did not update after adding iPhone");
    }

    @Test(priority = 7, description = "TC_033: Footer Section and External Links",
            groups = {"ui", "regression"})
    @Story("User verifies the footer links and info sections")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("TC-033")
    public void testFooterLinks() {
        Assert.assertTrue(homePage.isAboutUsVisible(), "About Us link missing");
        Assert.assertTrue(homePage.isContactUsVisible(), "Contact Us link missing");
        Assert.assertTrue(homePage.isBrandsVisible(), "Brands link missing");
        Assert.assertTrue(homePage.isMyAccountFooterVisible(), "My Account link in footer missing");
    }
}
