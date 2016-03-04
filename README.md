# Mollie API client for java

This is a java port of the [PHP Client](https://github.com/mollie/mollie-api-php).

## Requirements ##

+ In order to use the API a [Mollie account](https://www.mollie.com/aanmelden) is required.
+ Java >= 6
+ Gradle build system

## Building ##

```
gradle build
```

## Basic Usage ##

Initializing the Mollie API client, and setting your API key.

```java
    MollieClient client = new MollieClient();
    try {
        client.setApiKey("test_dHar4XY7LxsDOtmnkVtjNVWXLSlXsM");
    } catch (MollieException e) {
        e.printStackTrace();
    }
```

Creating a new payment.

```java
    try {
        Payment payment = client.payments().create(new BigDecimal("10.00"),
            "My first API payment",
            "https://webshop.example.org/order/12345/",
            null
        );
    } catch (MollieException e) {
        e.printStackTrace();
    }
```

_After creation, the payment id is available in the `payment.id` property. You should store this id with your order._
    
Retrieving a payment.

```java
    try {
        Payment payment = client.payments().get(payment.id);

        if (payment.isPaid()) {
            System.out.println("Payment received.");
        }
    } catch (MollieException e) {
        e.printStackTrace();
    }
```

## License ##
[BSD (Berkeley Software Distribution) License](http://www.opensource.org/licenses/bsd-license.php).
Copyright (c) 2015-2015, Impending
