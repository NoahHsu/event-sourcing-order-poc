# End Point
| server       | swagger url                                                                       |
|--------------|-----------------------------------------------------------------------------------|
| Command side | http://localhost:8081/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config |
| Query side   | http://localhost:8082/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config |

# Aggregate
order
payment
shipment

# Event & Command
- order created
  - create payment 
  - create shipment

- payment created 
- shipment created
  - prepare order

- shipment send out
  - logistic order

- shipment pickup 
  - finish shipment
  - finish payment

- payment settled 
- shipment finished
  - finish order
    
