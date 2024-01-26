Feature: Payment creation and fetching endpoints

  Scenario: Failed fetching a payment details
    Given an order "1b001993-132b-454b-9db8-f669a4bb2571" for payment
    When  attempt to fetch the payment details
    Then  the API call should be handled with an error
    And   the response should display not found payment details for this order

  Scenario: Successful fetching payments by status
    Given the payment status
    |status|
    |APPROVED|
    |REJECTED|
    |PENDING |
    |CANCELLED|
    |IN_PROCESS|
    When  searching payments by status
    Then  the API call should return payments foe ach status

  Scenario: Successful fetching a payment order by its code
    Given the payment order with code
    When  searching the payment by its code
    Then  the API should return its details containing the code

# Payment Order Creation

  Scenario: Failed payment order creation (Invalid Payment Details)
    Given the new order arrived for payment with errors
    When the customer initiates the payment order without fixing errors
    Then the payment service should receive a request in attempt to create order
    And the payment order should reject the creation of the payment order

  # complete run: create payment, send to gateway and returns from gateway (approved)
  Scenario: Successful creating and paying an order
    Given a new payment order is created for an order
    When  the api calls the endpoint to send to payment gateway
    Then  the payment order should update its payment details from pending to in process
    And   once the payment is approved the payment order should update its status to approved
