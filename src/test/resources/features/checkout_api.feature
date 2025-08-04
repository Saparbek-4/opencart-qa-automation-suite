Feature: Checkout API Functionality

  As a logged-in/guest user
  I want to complete the checkout process via API
  So that I can place an order without registering

  @checkout @guest
  Scenario: Guest checkout with valid product and details
    Given I start a guest session
    And I add product with ID 41 and quantity 1 to the cart to proceed checkout
    And I fill billing and delivery details
    And I select shipping method "flat.flat"
    And I select payment method "bank_transfer"
    When I confirm the order
    Then the response should contain "Bank Transfer Instructions"
    And the order should be successfully placed
    And the response should contain "Your order has been successfully processed"

  @checkout @auth
  Scenario: Authenticated checkout with valid product and details
    Given I start authorized session
    And I add product with ID 41 and quantity 1 to the cart to proceed checkout
    And I fill billing and delivery details
    And I select shipping method "flat.flat"
    And I select payment method "bank_transfer"
    When I confirm the order
    Then the response should contain "Bank Transfer Instructions"
    And the order should be successfully placed
    And the response should contain "Your order has been successfully processed"
