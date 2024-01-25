Feature: Payment Order Creation

  Scenario: Successful payment order creation
    Given the new order arrived for payment
    When the customer initiates the payment order
    Then the payment service should receive a request to create a payment order
    And the payment order should successfully create the payment order
    #And the payment order should change it to approved


  Scenario: Failed payment order creation (Invalid Payment Details)
    Given the new order arrived for payment with errors
    When the customer initiates the payment order without fixing errors
    Then the payment service should receive a request in attempt to create order
    And the payment order should reject the creation of the payment order
   # And the payment order should keep it on pending Status
   # And the customer should receive an error message about invalid payment details

  # complete run: create payment, send to gateway and returns from gateway (approved)
  Scenario: Successful creating and paying an order
    Given a new payment order is created for an order
    When  the api calls the endpoint to send to payment gateway
    Then  the payment order should update its payment details from pending to in process
    And   once the payment is approved the payment order should update its status to approved