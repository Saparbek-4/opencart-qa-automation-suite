package com.opencart.utils;

import com.opencart.ui.models.CartSetupResult;
import com.opencart.ui.pages.CartPage;
import com.opencart.ui.pages.HomePage;
import com.opencart.ui.pages.ProductPage;

import java.util.List;

public class CartTestUtils {
    public static CartSetupResult prepareCartWithOnly(
            CartPage cartPage,
            HomePage homePage,
            ProductPage productPage,
            String product,
            int quantity) {

        cartPage.navigateToCart();
        List<String> currentProducts = cartPage.getAllProductNamesInCart();

        boolean hasExpected = currentProducts.stream().anyMatch(p -> p.equalsIgnoreCase(product));
        boolean hasOthers = currentProducts.stream().anyMatch(p -> !p.equalsIgnoreCase(product));

        if (hasOthers) {
            cartPage.removeAllExcept(product);
        }

        String successMsg = "";
        boolean isInCart = false;

        if (!hasExpected) {
            for (int i = 0; i < quantity; i++) {
                homePage.searchProduct(product);
                homePage.addProductToCart(product);
                successMsg = productPage.getSuccessMessage();
                cartPage.navigateToCart();
            }
            isInCart = cartPage.isProductInCart(product);
        } else {
            int currentQty = cartPage.getQuantity(product);
            if (currentQty != quantity) {
                cartPage.updateQuantity(product, quantity);
                successMsg = cartPage.getSuccessMessage();
            }
            isInCart = true;
        }

        return new CartSetupResult(successMsg, isInCart);
    }

}
