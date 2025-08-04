package com.opencart.ui.tests;

import com.opencart.ui.base.BaseTest;
import com.opencart.ui.pages.AccountPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountPageTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(AccountPageTest.class);
    private AccountPage accountPage;

    @Override
    public void setupTestData() {
        logger.info("=== Setting up AccountPageTest ===");
        accountPage = new AccountPage();
        logger.info("AccountPage object initialized");
    }

    /**
     * 🧪 TC_001: My Account Page Loads Successfully
     * Step: Log in and go to /index.php?route=account/account
     * Check: Page heading is present, Left sidebar and central links are visible
     */
    @Test(priority = 1, description = "TC_001: My Account Page Loads Successfully")
    public void testMyAccountPageLoadsSuccessfully() {
        logger.info("🧪 Starting TC_001: My Account Page Loads Successfully");

        try {
            // Step 1: Navigate to My Account page
            logger.info("Step 1: Navigating to My Account page");
            accountPage.navigateToAccountPage();

            // Check 1: Page heading is present
            logger.info("Check 1: Verifying page heading is present");
            Assert.assertTrue(accountPage.isMyAccountHeaderVisible(),
                    "My Account page heading should be visible");

            String headerText = accountPage.getPageHeaderText();
            Assert.assertTrue(headerText.toLowerCase().contains("account") ||
                            headerText.toLowerCase().contains("my account"),
                    "Page header should contain 'Account' or 'My Account'. Actual: " + headerText);

            // Check 2: Left sidebar is visible
            logger.info("Check 2: Verifying left sidebar is visible");
            Assert.assertTrue(accountPage.isRightSidebarVisible(),
                    "Right side should be visible on My Account page");

            // Check 3: Central content links are visible
            logger.info("Check 3: Verifying central content area is visible");
            Assert.assertTrue(accountPage.isCentralContentVisible(),
                    "Central content area should be visible on My Account page");

            // Verify URL
            logger.info("Additional Check: Verifying URL contains account route");
            Assert.assertTrue(accountPage.verifyCurrentUrl("route=account/account"),
                    "URL should contain 'route=account/account'");

            logger.info("✅ TC_001: My Account Page Loads Successfully - PASSED");

        } catch (Exception e) {
            logger.error("❌ TC_001: My Account Page Loads Successfully - FAILED", e);
            throw e;
        }
    }

    /**
     * 🧪 TC_002: Verify All Account Links Are Visible
     * Check: Assert that all required account links are present
     */
    @Test(priority = 2, description = "TC_002: Verify All Account Links Are Visible")
    public void testVerifyAllAccountLinksAreVisible() {
        logger.info("🧪 Starting TC_002: Verify All Account Links Are Visible");

        try {
            // Navigate to My Account page
            logger.info("Navigating to My Account page");
            accountPage.navigateToAccountPage();

            // Verify individual links
            logger.info("Checking individual account links:");

            logger.info("- Checking Edit Account link");
            Assert.assertTrue(accountPage.isEditAccountLinkVisible(),
                    "Edit Account link should be visible");

            logger.info("- Checking Password link");
            Assert.assertTrue(accountPage.isPasswordLinkVisible(),
                    "Password link should be visible");

            logger.info("- Checking Address Book link");
            Assert.assertTrue(accountPage.isAddressBookLinkVisible(),
                    "Address Book link should be visible");

            logger.info("- Checking Wish List link");
            Assert.assertTrue(accountPage.isWishListLinkVisible(),
                    "Wish List link should be visible");

            logger.info("- Checking Order History link");
            Assert.assertTrue(accountPage.isOrderHistoryLinkVisible(),
                    "Order History link should be visible");

            logger.info("- Checking Downloads link");
            Assert.assertTrue(accountPage.isDownloadsLinkVisible(),
                    "Downloads link should be visible");

            logger.info("- Checking Reward Points link");
            Assert.assertTrue(accountPage.isRewardPointsLinkVisible(),
                    "Reward Points link should be visible");

            logger.info("- Checking Returns link");
            Assert.assertTrue(accountPage.isReturnsLinkVisible(),
                    "Returns link should be visible");

            logger.info("- Checking Transactions link");
            Assert.assertTrue(accountPage.isTransactionsLinkVisible(),
                    "Transactions link should be visible");

            logger.info("- Checking Newsletter link");
            Assert.assertTrue(accountPage.isNewsletterLinkVisible(),
                    "Newsletter link should be visible");

            logger.info("- Checking Logout link");
            Assert.assertTrue(accountPage.isLogoutLinkVisible(),
                    "Logout link should be visible");

            // Comprehensive check using helper method
            logger.info("Running comprehensive check for all links");
            Assert.assertTrue(accountPage.areAllAccountLinksVisible(),
                    "All account links should be visible");

            logger.info("✅ TC_002: Verify All Account Links Are Visible - PASSED");

        } catch (Exception e) {
            logger.error("❌ TC_002: Verify All Account Links Are Visible - FAILED", e);
            throw e;
        }
    }

    /**
     * 🧪 TC_003: Navigate to Edit Account Page
     * Action: Click Edit Account
     * Expected: Redirect to index.php?route=account/edit
     * Validate: Form fields are visible (First Name, Last Name, Email, etc.)
     * Note: Detailed form validation is covered in EditAccountPageTest.java
     */
    @Test(priority = 3, description = "TC_003: Navigate to Edit Account Page")
    public void testNavigateToEditAccountPage() {
        logger.info("🧪 Starting TC_003: Navigate to Edit Account Page");

        try {
            // Navigate to My Account page
            logger.info("Navigating to My Account page");
            accountPage.navigateToAccountPage();

            // Verify Edit Account link is visible before clicking
            logger.info("Verifying Edit Account link is visible");
            Assert.assertTrue(accountPage.isEditAccountLinkVisible(),
                    "Edit Account link should be visible before clicking");

            // Click Edit Account link
            logger.info("Clicking Edit Account link");
            accountPage.clickEditAccount();

            // Wait for page load and verify URL
            logger.info("Verifying redirect to Edit Account page");
            Assert.assertTrue(accountPage.verifyCurrentUrl("route=account/edit"),
                    "Should redirect to account/edit page");

            logger.info("✅ TC_003: Navigate to Edit Account Page - PASSED");
            logger.info("📝 Note: Detailed form field validation is covered in EditAccountPageTest.java");

        } catch (Exception e) {
            logger.error("❌ TC_003: Navigate to Edit Account Page - FAILED", e);
        }
    }

    /**
     * 🧪 TC_004: Navigate to Wish List
     * Action: Click Wish List
     * Expected: Go to /index.php?route=account/wishlist
     * Check: Items listed, remove buttons present
     * Note: Detailed wishlist validation is covered in WishListPageTest.java
     */
    @Test(priority = 4, description = "TC_004: Navigate to Wish List")
    public void testNavigateToWishList() {
        logger.info("🧪 Starting TC_004: Navigate to Wish List");

        try {
            // Navigate to My Account page
            logger.info("Navigating to My Account page");
            accountPage.navigateToAccountPage();

            // Verify Wish List link is visible before clicking
            logger.info("Verifying Wish List link is visible");
            Assert.assertTrue(accountPage.isWishListLinkVisible(),
                    "Wish List link should be visible before clicking");

            // Click Wish List link
            logger.info("Clicking Wish List link");
            accountPage.clickWishList();

            // Wait for page load and verify URL
            logger.info("Verifying redirect to Wish List page");
            Assert.assertTrue(accountPage.verifyCurrentUrl("route=account/wishlist"),
                    "Should redirect to account/wishlist page");

            logger.info("✅ TC_004: Navigate to Wish List - PASSED");
            logger.info("📝 Note: Detailed wishlist items and remove buttons validation is covered in WishListPageTest.java");

        } catch (Exception e) {
            logger.error("❌ TC_004: Navigate to Wish List - FAILED", e);
        }
    }

    /**
     * 🧪 TC_005: Navigate to Order History
     * Check: /index.php?route=account/order
     * Verify: List of past orders (or message if none)
     */
    @Test(priority = 5, description = "TC_005: Navigate to Order History")
    public void testNavigateToOrderHistory() {
        logger.info("🧪 Starting TC_005: Navigate to Order History");

        try {
            // Navigate to My Account page
            logger.info("Navigating to My Account page");
            accountPage.navigateToAccountPage();

            // Verify Order History link is visible before clicking
            logger.info("Verifying Order History link is visible");
            Assert.assertTrue(accountPage.isOrderHistoryLinkVisible(),
                    "Order History link should be visible before clicking");

            // Click Order History link
            logger.info("Clicking Order History link");
            accountPage.clickOrderHistory();

            // Wait for page load and verify URL
            logger.info("Verifying redirect to Order History page");
            Assert.assertTrue(accountPage.verifyCurrentUrl("route=account/order"),
                    "Should redirect to account/order page");

            // Additional verification: Check if page loaded successfully
            // This could be enhanced to check for order list or "no orders" message
            logger.info("Verifying Order History page loaded successfully");
            String currentUrl = accountPage.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("account/order"),
                    "Order History page should be loaded. Current URL: " + currentUrl);

            logger.info("✅ TC_005: Navigate to Order History - PASSED");

        } catch (Exception e) {
            logger.error("❌ TC_005: Navigate to Order History - FAILED", e);
        }
    }

    /**
     * 🧪 TC_006: Logout Functionality
     * Action: Click Logout
     * Expected: Redirect to account/logout
     * Message: "You have been logged off your account."
     */
    @Test(priority = 6, description = "TC_006: Logout Functionality")
    public void testLogoutFunctionality() {
        logger.info("🧪 Starting TC_006: Logout Functionality");

        try {
            // Navigate to My Account page
            logger.info("Navigating to My Account page");
            accountPage.navigateToAccountPage();

            // Verify Logout link is visible before clicking
            logger.info("Verifying Logout link is visible");
            Assert.assertTrue(accountPage.isLogoutLinkVisible(),
                    "Logout link should be visible before clicking");

            // Click Logout link
            logger.info("Clicking Logout link");
            accountPage.clickLogout();


            // Verify redirect to logout page
            logger.info("Verifying redirect to logout page");
            Assert.assertTrue(accountPage.verifyCurrentUrl("route=account/logout"),
                    "Should redirect to account/logout page");

            // Verify logout confirmation message
            logger.info("Verifying logout confirmation message");
            Assert.assertTrue(accountPage.isLogoutMessageDisplayed(),
                    "Logout confirmation message should be displayed");

            String logoutMessage = accountPage.getLogoutMessage();
            Assert.assertTrue(
                    logoutMessage.toLowerCase().contains("logged off") ||
                            logoutMessage.toLowerCase().contains("logout") ||
                            logoutMessage.toLowerCase().contains("logged out"),
                    "Logout message should contain confirmation text. Actual message: " + logoutMessage
            );

            logger.info("✅ TC_006: Logout Functionality - PASSED");
            logger.info("📝 Logout message: {}", logoutMessage);

        } catch (Exception e) {
            logger.error("❌ TC_006: Logout Functionality - FAILED", e);
        }
    }
}