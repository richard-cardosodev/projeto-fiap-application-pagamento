Feature: Payment Order fetch

Scenario: Failed fetching a payment details
  Given an unknown order "1b001993-132b-454b-9db8-f669a4bb2571" for payment
  When  attempt to fetch the payment details
  Then  the API call should be handled with an error
  And   the response should display not found payment details for this order

Scenario: Successful fetching a payment list items
  Given that is required to list all the "APPROVED" payments
  When  searching payments by payment status
  Then  the API call should return a list of approved payments

Scenario: Successful fetching a payment order by its code
  Given the payment order with code
  When  searching the payment by its code
  Then  the API should return its details containing the code
