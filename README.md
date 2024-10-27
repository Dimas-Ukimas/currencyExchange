# Currency Exchange Project💱

![Money GIF](https://private-user-images.githubusercontent.com/82829980/380516930-7acef5de-ed26-440c-88cc-a94e869a0484.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MzAwNDk3ODUsIm5iZiI6MTczMDA0OTQ4NSwicGF0aCI6Ii84MjgyOTk4MC8zODA1MTY5MzAtN2FjZWY1ZGUtZWQyNi00NDBjLTg4Y2MtYTk0ZTg2OWEwNDg0LmdpZj9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNDEwMjclMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjQxMDI3VDE3MTgwNVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWJjNDEzZDAwMGYzYTcxNjU5NDdhZWM2NzdjZjM4YWFhNjY0MmExMzQ0OWQ2ZWM1Y2IwOWIwOGMxOGU2YzQ1ZmQmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.IVKL5OFlH68EFmKL4oH15xHM6qOTWHnYGoDsJseo0M0)

## Overview📝

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
