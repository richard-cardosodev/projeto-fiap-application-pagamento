Feature: Payment usecases

  Scenario: Failed fetching payment for update status
    Given a new payment order
    When  attempting to fetch the payment not found
    Then  the payment status should not change its status

    Scenario: Creating a new payment order and checking its status
      Given a new order is created
      When the order is fetched
      Then the order status should be "PENDING"