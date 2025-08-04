package com.opencart.utils;

import com.opencart.ui.models.WishlistSetupResult;
import com.opencart.ui.pages.HomePage;
import com.opencart.ui.pages.ProductPage;
import com.opencart.ui.pages.WishlistPage;

import java.util.List;

public class WishListUtils {
    public static WishlistSetupResult prepareWishlistWithOnly(
            WishlistPage wishlistPage,
            HomePage homePage,
            ProductPage productPage,
            String product) {

        wishlistPage.navigateToWishlist();
        List<String> currentProducts = wishlistPage.getAllProductNamesInWishList();

        boolean hasExpected = currentProducts.contains(product);
        boolean hasOthers = currentProducts.stream().anyMatch(p -> !p.equalsIgnoreCase(product));

        if (hasOthers) {
            wishlistPage.removeAllExcept(product);
        }

        if (!hasExpected) {
            homePage.searchProduct(product);
            homePage.addToWishlist(product);

            wishlistPage.navigateToWishlist();
        }

        return new WishlistSetupResult(productPage.getSuccessMessage(), wishlistPage.isProductInWishlist(product));
    }
}
