package com.opencart.ui.pages;

import com.opencart.ui.base.BasePage;
import com.opencart.utils.ConfigReader;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opencart.utils.DriverFactory;

public class AccountPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(AccountPage.class);

    // Page Elements
    private By accountHeader = By.cssSelector("div#content h2");
    private By rightSidebar = By.id("column-right");
    private By centralContent = By.cssSelector("div#content");

    // Account Navigation Links
    private By editAccountLink = By.linkText("Edit Account");
    private By passwordLink = By.linkText("Password");
    private By addressBookLink = By.linkText("Address Book");
    private By wishListLink = By.linkText("Wish List");
    private By orderHistoryLink = By.linkText("Order History");
    private By downloadsLink = By.linkText("Downloads");
    private By rewardPointsLink = By.linkText("Reward Points");
    private By returnsLink = By.linkText("Returns");
    private By transactionsLink = By.linkText("Transactions");
    private By newsletterLink = By.linkText("Newsletter");
    private By logoutLink = By.linkText("Logout");

    // Alternative locators for different layouts
    private By editAccountLinkAlt = By.xpath("//a[contains(@href, 'account/edit')]");
    private By passwordLinkAlt = By.xpath("//a[contains(@href, 'account/password')]");
    private By addressBookLinkAlt = By.xpath("//a[contains(@href, 'account/address')]");
    private By wishListLinkAlt = By.xpath("//a[contains(@href, 'account/wishlist')]");
    private By orderHistoryLinkAlt = By.xpath("//a[contains(@href, 'account/order')]");
    private By downloadsLinkAlt = By.xpath("//a[contains(@href, 'account/download')]");
    private By rewardPointsLinkAlt = By.xpath("//a[contains(@href, 'account/reward')]");
    private By returnsLinkAlt = By.xpath("//a[contains(@href, 'account/return')]");
    private By transactionsLinkAlt = By.xpath("//a[contains(@href, 'account/transaction')]");
    private By newsletterLinkAlt = By.xpath("//a[contains(@href, 'account/newsletter')]");
    private By logoutLinkAlt = By.xpath("//a[contains(@href, 'account/logout')]");

    // Logout confirmation message
    private By logoutMessage = By.cssSelector("div#content p");

    public AccountPage() {
        super();
        logger.info("AccountPage instance created");
    }

    /**
     * Navigate to My Account page
     */
    public void navigateToAccountPage() {
        driver.get().navigate().to(ConfigReader.getProperty("baseUrl") + "index.php?route=account/account");
        logger.info("Navigated to: {}", DriverFactory.getDriver().getCurrentUrl());
    }

    /**
     * Check if My Account page header is visible
     */
    public boolean isMyAccountHeaderVisible() {
        logger.info("Checking if My Account header is visible");
        try {
            boolean isVisible = isElementDisplayed(accountHeader);
            String headerText = getText(accountHeader);
            logger.info("My Account header visibility: {}, Text: '{}'", isVisible, headerText);
            return isVisible;
        } catch (Exception e) {
            logger.error("Failed to check My Account header visibility", e);
            return false;
        }
    }

    /**
     * Get the page header text
     */
    public String getPageHeaderText() {
        logger.info("Getting page header text");
        try {
            String headerText = getText(accountHeader);
            logger.info("Page header text: '{}'", headerText);
            return headerText;
        } catch (Exception e) {
            logger.error("Failed to get page header text", e);
            return "";
        }
    }

    /**
     * Check if left sidebar is visible
     */
    public boolean isRightSidebarVisible() {
        logger.info("Checking if left sidebar is visible");
        try {
            boolean isVisible = isElementDisplayed(rightSidebar);
            logger.info("Left sidebar visibility: {}", isVisible);
            return isVisible;
        } catch (Exception e) {
            logger.error("Failed to check left sidebar visibility", e);
            return false;
        }
    }

    /**
     * Check if central content area is visible
     */
    public boolean isCentralContentVisible() {
        logger.info("Checking if central content is visible");
        try {
            boolean isVisible = isElementDisplayed(centralContent);
            logger.info("Central content visibility: {}", isVisible);
            return isVisible;
        } catch (Exception e) {
            logger.error("Failed to check central content visibility", e);
            return false;
        }
    }

    /**
     * Check if a specific account link is present (with fallback locators)
     */
    private boolean isAccountLinkPresent(By primaryLocator, By alternativeLocator, String linkName) {
        logger.info("Checking if '{}' link is present", linkName);
        try {
            if (isElementPresent(primaryLocator)) {
                logger.info("'{}' link found using primary locator", linkName);
                return true;
            } else if (isElementPresent(alternativeLocator)) {
                logger.info("'{}' link found using alternative locator", linkName);
                return true;
            }
            logger.warn("'{}' link not found with either locator", linkName);
            return false;
        } catch (Exception e) {
            logger.error("Error checking '{}' link presence", linkName, e);
            return false;
        }
    }

    /**
     * Check all account links visibility
     */
    public boolean isEditAccountLinkVisible() {
        return isAccountLinkPresent(editAccountLink, editAccountLinkAlt, "Edit Account");
    }

    public boolean isPasswordLinkVisible() {
        return isAccountLinkPresent(passwordLink, passwordLinkAlt, "Password");
    }

    public boolean isAddressBookLinkVisible() {
        return isAccountLinkPresent(addressBookLink, addressBookLinkAlt, "Address Book");
    }

    public boolean isWishListLinkVisible() {
        return isAccountLinkPresent(wishListLink, wishListLinkAlt, "Wish List");
    }

    public boolean isOrderHistoryLinkVisible() {
        return isAccountLinkPresent(orderHistoryLink, orderHistoryLinkAlt, "Order History");
    }

    public boolean isDownloadsLinkVisible() {
        return isAccountLinkPresent(downloadsLink, downloadsLinkAlt, "Downloads");
    }

    public boolean isRewardPointsLinkVisible() {
        return isAccountLinkPresent(rewardPointsLink, rewardPointsLinkAlt, "Reward Points");
    }

    public boolean isReturnsLinkVisible() {
        return isAccountLinkPresent(returnsLink, returnsLinkAlt, "Returns");
    }

    public boolean isTransactionsLinkVisible() {
        return isAccountLinkPresent(transactionsLink, transactionsLinkAlt, "Transactions");
    }

    public boolean isNewsletterLinkVisible() {
        return isAccountLinkPresent(newsletterLink, newsletterLinkAlt, "Newsletter");
    }

    public boolean isLogoutLinkVisible() {
        return isAccountLinkPresent(logoutLink, logoutLinkAlt, "Logout");
    }

    /**
     * Click on account links with fallback
     */
    private void clickAccountLink(By primaryLocator, By alternativeLocator, String linkName) {
        logger.info("Attempting to click '{}' link", linkName);
        try {
            if (isElementPresent(primaryLocator)) {
                click(primaryLocator);
                logger.info("Successfully clicked '{}' using primary locator", linkName);
            } else if (isElementPresent(alternativeLocator)) {
                click(alternativeLocator);
                logger.info("Successfully clicked '{}' using alternative locator", linkName);
            } else {
                throw new RuntimeException("'" + linkName + "' link not found");
            }
        } catch (Exception e) {
            logger.error("Failed to click '{}' link", linkName, e);
            throw new RuntimeException("Failed to click '" + linkName + "' link", e);
        }
    }

    /**
     * Navigation methods for each account section
     */
    public void clickEditAccount() {
        clickAccountLink(editAccountLink, editAccountLinkAlt, "Edit Account");
    }

    public void clickWishList() {
        clickAccountLink(wishListLink, wishListLinkAlt, "Wish List");
    }

    public void clickOrderHistory() {
        clickAccountLink(orderHistoryLink, orderHistoryLinkAlt, "Order History");
    }

    public void clickLogout() {
        clickAccountLink(logoutLink, logoutLinkAlt, "Logout");
    }

    /**
     * Verify logout confirmation message
     */
    public boolean isLogoutMessageDisplayed() {
        logger.info("Checking if logout confirmation message is displayed");
        try {
            boolean isDisplayed = isElementDisplayed(logoutMessage);
            if (isDisplayed) {
                String message = getText(logoutMessage);
                logger.info("Logout message displayed: '{}'", message);
                return message.contains("logged off") || message.contains("logout");
            }
            logger.warn("Logout message not displayed");
            return false;
        } catch (Exception e) {
            logger.error("Failed to check logout message", e);
            return false;
        }
    }

    /**
     * Get logout confirmation message text
     */
    public String getLogoutMessage() {
        logger.info("Getting logout confirmation message");
        try {
            String message = getText(logoutMessage);
            logger.info("Logout message: '{}'", message);
            return message;
        } catch (Exception e) {
            logger.error("Failed to get logout message", e);
            return "";
        }
    }

    /**
     * Verify current URL matches expected pattern
     */
    public boolean verifyCurrentUrl(String expectedUrlPattern) {
        logger.info("Verifying current URL contains: '{}'", expectedUrlPattern);
        try {
            String currentUrl = getCurrentUrl();
            boolean matches = currentUrl.contains(expectedUrlPattern);
            logger.info("Current URL: '{}', Matches expected pattern: {}", currentUrl, matches);
            return matches;
        } catch (Exception e) {
            logger.error("Failed to verify URL", e);
            return false;
        }
    }

    /**
     * Comprehensive method to verify all account links are present
     */
    public boolean areAllAccountLinksVisible() {
        logger.info("Verifying all account links are visible");

        boolean allLinksVisible = true;

        if (!isEditAccountLinkVisible()) {
            logger.error("Edit Account link is not visible");
            allLinksVisible = false;
        }

        if (!isPasswordLinkVisible()) {
            logger.error("Password link is not visible");
            allLinksVisible = false;
        }

        if (!isAddressBookLinkVisible()) {
            logger.error("Address Book link is not visible");
            allLinksVisible = false;
        }

        if (!isWishListLinkVisible()) {
            logger.error("Wish List link is not visible");
            allLinksVisible = false;
        }

        if (!isOrderHistoryLinkVisible()) {
            logger.error("Order History link is not visible");
            allLinksVisible = false;
        }

        if (!isDownloadsLinkVisible()) {
            logger.error("Downloads link is not visible");
            allLinksVisible = false;
        }

        if (!isRewardPointsLinkVisible()) {
            logger.error("Reward Points link is not visible");
            allLinksVisible = false;
        }

        if (!isReturnsLinkVisible()) {
            logger.error("Returns link is not visible");
            allLinksVisible = false;
        }

        if (!isTransactionsLinkVisible()) {
            logger.error("Transactions link is not visible");
            allLinksVisible = false;
        }

        if (!isNewsletterLinkVisible()) {
            logger.error("Newsletter link is not visible");
            allLinksVisible = false;
        }

        if (!isLogoutLinkVisible()) {
            logger.error("Logout link is not visible");
            allLinksVisible = false;
        }

        logger.info("All account links visible: {}", allLinksVisible);
        return allLinksVisible;
    }
}
