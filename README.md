# Currency Exchange Project💱

The REST API for describing currencies and their exhange rates, which allows to browse and edit currencies, exchange rates and convert one currency to another.

## Technologies🛠️
![Static Badge](https://img.shields.io/badge/Java-brightgreen)
![Static Badge](https://img.shields.io/badge/Maven-blue)
![Static Badge](https://img.shields.io/badge/Java%20Servlets-orange)
![Static Badge](https://img.shields.io/badge/JDBC-grey)
![Static Badge](https://img.shields.io/badge/SQLite-purple)
![Static Badge](https://img.shields.io/badge/Tomcat-yellow)

## API endpoints🚩

### Currencies

#### GET /currencies

Fetching a list of currencies. 

Response example:

```
[
    {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },   
    {
        "id": 0,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    }
]
```

#### GET /currency/EUR 

Fetching a particular currency. 

Response example:
```
{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```

#### POST /currencies

Adding a new currency to the database. The data is sent in the request body as form fields (x-www-form-urlencoded). The fields of the form are name, code, sign. 

Response example (JSON representation of an inserted record):
```
{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```

### Exhange Rates

#### GET /exchangeRates

Fetching a list of all exchange rates. 

Response example:
```
[
    {
        "id": 0,
        "baseCurrency": {
            "id": 0,
            "name": "United States dollar",
            "code": "USD",
            "sign": "$"
        },
        "targetCurrency": {
            "id": 1,
            "name": "Euro",
            "code": "EUR",
            "sign": "€"
        },
        "rate": 0.99
    }
]
```

#### GET /exchangeRate/USDRUB

Fetching a specific exchange rate. The currency pair defined by consecutive currency codes in the request address.

Response example:
```
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}

```

#### POST /exchangeRates

Adding a new exchange rate to the database. The data is sent in the request body as form fields (x-www-form-urlencoded). The form fields are base Currency Code, targetCurrencyCode, rate. 

Example of form fields:

baseCurrencyCode - USD
targetCurrencyCode - EUR
rate - 0.99

Response example (JSON representation of an inserted record):
```
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```

#### PATCH /exchangeRate/USDRUB

Updating the existing exchange rate in the database. The currency pair is defined by consecutive currency codes in the request address. The data is sent in the request body as form fields (x-www-form-urlencoded). The only field in the form is rate.

Response example (JSON representation of an inserted record):
```
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```

### Currency exchange

#### GET /exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT

Calculation of the transfer of certain amount of money from one currency to another.

Example of a request - GET /exchange?from=USD&to=AUD&amount=10.

Obtaining an exchange rate can take place according to one of three scenarios. Let's say we make a transfer from currency A to currency B:

**Scenario 1**: Use a currency pair AB in the data base, if it exists (direct course)

**Scenario 2**: There is no AB pair, but we have a BA pair. Hence, we take its exchange rate and count the opposite to get AB (reversed course)

**Scenario 3**: There are USD-A and USD-B currency pairs in the data base. So we can calculate the AB rate from these rates (cross course)

Response example:
```
{
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Australian dollar",
        "code": "AUD",
        "sign": "A€"
    },
    "rate": 1.45,
    "amount": 10.00,
    "convertedAmount": 14.50
}

```
