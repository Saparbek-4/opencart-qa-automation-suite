package com.opencart.ui.tests;

import com.opencart.ui.base.BaseTest;
import com.opencart.ui.pages.AccountPage;
import com.opencart.ui.pages.LoginPage;
import com.opencart.utils.UserPoolManager;
import com.opencart.utils.WaitUtils;
import io.qameta.allure.*;

import io.qameta.allure.testng.Tag;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Epic("Account Management")
@Feature("My Account Page Functionality")
@Owner("saparbek.kozhanazar04@gmail.com")
@Tag("regression")
@Tag("ui")
public class AccountPageTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(AccountPageTest.class);
    private AccountPage accountPage;
    private LoginPage loginPage;

    @Override
    public void setupTestData() {
        logger.info("=== Setting up AccountPageTest ===");
        loginPage = new LoginPage();
        accountPage = new AccountPage();

        new LoginPage().navigateToLoginPage();
        new WaitUtils().waitForPageLoad();

        String email = UserPoolManager.acquireUser();
        String password = getProp().getProperty("testUserPassword");

        loginPage.login(email, password);
    }

    @Test(priority = 1, description = "TC_003: My Account Page Loads Successfully",
            groups = {"smoke", "ui", "regression"})
    @Story("User visits the My Account page after login")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-003")
    @Description("Verify that My Account page loads with all key elements visible (header, sidebar, content area).")
    @Tag("smoke")
    public void testMyAccountPageLoadsSuccessfully() {
        logger.info("🧪 Starting TC_003: My Account Page Loads Successfully");
        try {
            Assert.assertTrue(accountPage.isMyAccountHeaderVisible(),
                    "My Account page heading should be visible");

            String headerText = accountPage.getPageHeaderText();
            Assert.assertTrue(headerText.toLowerCase().contains("account") ||
                            headerText.toLowerCase().contains("my account"),
                    "Page header should contain 'Account' or 'My Account'. Actual: " + headerText);

            Assert.assertTrue(accountPage.isRightSidebarVisible(),
                    "Right side should be visible on My Account page");

            Assert.assertTrue(accountPage.isCentralContentVisible(),
                    "Central content area should be visible");

            Assert.assertTrue(accountPage.verifyCurrentUrl("route=account/account"),
                    "URL should contain 'route=account/account'");

        } catch (Exception e) {
            logger.error("❌ TC_001 Failed", e);
            throw e;
        }
    }

    @Test(priority = 2, description = "TC_004: Verify All Account Links Are Visible",
            groups = {"ui", "regression"})
    @Story("User can view all relevant account links")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-004")
    @Description("Verify visibility of Edit, Password, Address Book, Wish List, Orders, Downloads, Returns, etc.")
    public void testVerifyAllAccountLinksAreVisible() {
        logger.info("🧪 Starting TC_004: Verify All Account Links Are Visible");
        try {
            Assert.assertTrue(accountPage.isEditAccountLinkVisible());
            Assert.assertTrue(accountPage.isPasswordLinkVisible());
            Assert.assertTrue(accountPage.isAddressBookLinkVisible());
            Assert.assertTrue(accountPage.isWishListLinkVisible());
            Assert.assertTrue(accountPage.isOrderHistoryLinkVisible());
            Assert.assertTrue(accountPage.isDownloadsLinkVisible());
            Assert.assertTrue(accountPage.isRewardPointsLinkVisible());
            Assert.assertTrue(accountPage.isReturnsLinkVisible());
            Assert.assertTrue(accountPage.isTransactionsLinkVisible());
            Assert.assertTrue(accountPage.isNewsletterLinkVisible());
            Assert.assertTrue(accountPage.isLogoutLinkVisible());

            Assert.assertTrue(accountPage.areAllAccountLinksVisible(),
                    "All account links should be visible");
        } catch (Exception e) {
            logger.error("❌ TC_002 Failed", e);
            throw e;
        }
    }

    @Test(priority = 3, description = "TC_005: Navigate to Edit Account Page",
            groups = {"ui", "regression"})
    @Story("User navigates to Edit Account page")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-005")
    @Description("Click Edit Account and verify redirection to edit page. Field validation is tested separately.")
    public void testNavigateToEditAccountPage() {
        logger.info("🧪 Starting TC_005: Navigate to Edit Account Page");
        try {
            Assert.assertTrue(accountPage.isEditAccountLinkVisible());
            accountPage.clickEditAccount();
            Assert.assertTrue(accountPage.verifyCurrentUrl("route=account/edit"));
        } catch (Exception e) {
            logger.error("❌ TC_003 Failed", e);
            throw e;
        }
    }

    @Test(priority = 4, description = "TC_006: Navigate to Wish List",
            groups = {"ui", "regression"})
    @Story("User navigates to their Wish List")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-006")
    @Description("Verify that clicking the Wish List link redirects to the correct page.")
    public void testNavigateToWishList() {
        logger.info("🧪 Starting TC_006: Navigate to Wish List");
        try {
            Assert.assertTrue(accountPage.isWishListLinkVisible());
            accountPage.clickWishList();
            Assert.assertTrue(accountPage.verifyCurrentUrl("route=account/wishlist"));
        } catch (Exception e) {
            logger.error("❌ TC_004 Failed", e);
            throw e;
        }
    }

    @Test(priority = 5, description = "TC_007: Navigate to Order History",
            groups = {"ui", "regression"})
    @Story("User views order history")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("TC-007")
    @Description("Verify that the Order History link works and redirects correctly.")
    public void testNavigateToOrderHistory() {
        logger.info("🧪 Starting TC_007: Navigate to Order History");
        try {
            Assert.assertTrue(accountPage.isOrderHistoryLinkVisible());
            accountPage.clickOrderHistory();
            Assert.assertTrue(accountPage.verifyCurrentUrl("route=account/order"));
            Assert.assertTrue(accountPage.getCurrentUrl().contains("account/order"));
        } catch (Exception e) {
            logger.error("❌ TC_005 Failed", e);
            throw e;
        }
    }

    @Test(priority = 6, description = "TC_008: Logout Functionality",
            groups = {"ui", "regression"})
    @Story("User logs out from their account")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-008")
    @Description("Click Logout and verify the user is redirected with confirmation message.")
    public void testLogoutFunctionality() {
        logger.info("🧪 Starting TC_008: Logout Functionality");
        try {
            Assert.assertTrue(accountPage.isLogoutLinkVisible());
            accountPage.clickLogout();
            Assert.assertTrue(accountPage.verifyCurrentUrl("route=account/logout"));
            Assert.assertTrue(accountPage.isLogoutMessageDisplayed());

            String logoutMessage = accountPage.getLogoutMessage();
            Assert.assertTrue(
                    logoutMessage.toLowerCase().contains("logged off") ||
                            logoutMessage.toLowerCase().contains("logout") ||
                            logoutMessage.toLowerCase().contains("logged out")
            );
        } catch (Exception e) {
            logger.error("❌ TC_006 Failed", e);
            throw e;
        }
    }
}
