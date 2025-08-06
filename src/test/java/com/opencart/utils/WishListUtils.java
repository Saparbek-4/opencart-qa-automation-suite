package com.opencart.utils;

import com.opencart.ui.models.WishlistSetupResult;
import com.opencart.ui.pages.HomePage;
import com.opencart.ui.pages.WishlistPage;

import java.util.List;

public class WishListUtils {
    public static WishlistSetupResult prepareWishlistWithOnly(
            WishlistPage wishlistPage,
            HomePage homePage,
            String product) {

        wishlistPage.navigateToWishlist();
        List<String> currentProducts = wishlistPage.getAllProductNamesInWishList();

        // Remove everything
        if (!currentProducts.isEmpty()) {
            wishlistPage.removeAllExcept(""); // remove all
        }

        // Add product again
        homePage.searchProduct(product);
        homePage.addToWishlist(product);

        // ✅ Capture success message before redirect
        String successMessage = homePage.getSuccessMessage();

        wishlistPage.navigateToWishlist();
        boolean isInWishlist = wishlistPage.isProductInWishlist(product);

        return new WishlistSetupResult(successMessage, isInWishlist);
    }
}
