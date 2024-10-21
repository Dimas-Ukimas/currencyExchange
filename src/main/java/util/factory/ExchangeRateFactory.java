package util.factory;

import entity.Currency;
import entity.ExchangeRate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeRateFactory {

    private final BaseCurrencyFactory baseCurrencyFactory = new BaseCurrencyFactory();
    private final TargetCurrencyFactory targetCurrencyFactory = new TargetCurrencyFactory();

    public ExchangeRate createExchangeRate(ResultSet resultSet) throws SQLException {

        ExchangeRate exchangeRate = new ExchangeRate();
        Currency baseCurrency = baseCurrencyFactory.createCurrency(resultSet);
        Currency targetCurrency = targetCurrencyFactory.createCurrency(resultSet);

        exchangeRate.setId(resultSet.getInt("id"));
        exchangeRate.setBaseCurrency(baseCurrency);
        exchangeRate.setTargetCurrency(targetCurrency);
        exchangeRate.setRate(resultSet.getBigDecimal("Rate"));

        return exchangeRate;
    }
}
