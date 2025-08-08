Feature: Shopping Cart API Functionality

  Background:
    Given I am logged in with a valid session
    And my cart is empty

  @cart @api @regression
  Scenario: View an empty cart
    When I view the cart
    Then the response status code should be 200
    And the response should contain "Your shopping cart is empty!"

  @cart @api @smoke
  Scenario: Add product to cart
    When I add product with ID 41 and quantity 1 to the cart
    Then the response status code should be 200
    And the response JSON should contain "Success: You have added"

  @cart @api @regression
  Scenario: View cart with product
    Given the product with ID 41 and quantity 1 is in the cart
    When I view the cart
    Then the response should contain "iMac"
    And the cart should contain product ID 41 with quantity 1
    And the subtotal should be "$100.00"
    And the eco tax should be "$2.00"
    And the VAT should be "$20.00"
    And the total should be "$122.00"

  @cart @api @regression
  Scenario: Update quantity of product in cart
    Given the product with ID 41 and quantity 1 is in the cart
    When I update product ID 41 to quantity 2
    Then the cart should contain product ID 41 with quantity 2
    And the subtotal should be "$200.00"
    And the eco tax should be "$4.00"
    And the VAT should be "$40.00"
    And the total should be "$244.00"

  @cart @api @regression
  Scenario: Remove product from cart
    Given the product with ID 41 and quantity 1 is in the cart
    When I remove the product with ID 41 from the cart
    Then the response status code should be 200
    And the cart should be empty
