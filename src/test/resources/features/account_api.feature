Feature: Account API Functionality (Authenticated)

  Background:
    Given I am logged in with a valid session

  @account
  Scenario: Verify My Account Page Loads
    When I send a GET request to "/index.php?route=account/account"
    Then the response status code should be 200
    And the response should contain "My Account"

  @account
  Scenario: Verify All My Account Section Links Are Present
    When I send a GET request to "/index.php?route=account/account"
    Then the response should contain:
      | Edit Account       |
      | Password           |
      | Address Book       |
      | Wish List          |

  @account
  Scenario: Verify All My Orders Section Links Are Present
    When I send a GET request to "/index.php?route=account/account"
    Then the response should contain:
      | Order History      |
      | Downloads          |
      | Reward Points      |
      | Returns            |
      | Transactions       |
      | Payment Profile    |

  @account
  Scenario: Verify Newsletter Section Link Is Present
    When I send a GET request to "/index.php?route=account/account"
    Then the response should contain:
      | Newsletter         |

  @account
  Scenario: Verify Sidebar Links Are Present
    When I send a GET request to "/index.php?route=account/account"
    Then the response should contain:
      | My Account         |
      | Edit Account       |
      | Password           |
      | Address Book       |
      | Wish List          |
      | Order History      |
      | Downloads          |
      | Recurring payments |
      | Reward Points      |
      | Returns            |
      | Transactions       |
      | Newsletter         |
      | Logout             |

  @account
  Scenario: Navigate to Edit Account Page
    When I send a GET request to "/index.php?route=account/edit"
    Then the response status code should be 200
    And the response should contain:
      | First Name |
      | Last Name  |
      | E-Mail     |
      | Telephone  |

  @account
  Scenario: Logout
    When I send a GET request to "/index.php?route=account/logout"
    Then the response status code should be 302

