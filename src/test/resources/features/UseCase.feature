Feature: Payment usecase

  Scenario: Failed fetching payment for updated status
    Given a new payment order
    When  attempting to fetch the payment not found
    Then  the payment status should not change its status