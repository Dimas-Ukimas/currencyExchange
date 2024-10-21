package util.factory;

import entity.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseCurrencyFactory extends AbstractCurrencyFactory {

    @Override
    public Currency createCurrency(ResultSet resultSet) throws SQLException {

        Currency baseCurrency = new Currency();

        baseCurrency.setId(resultSet.getInt("BaseCurrencyId"));
        baseCurrency.setCode(resultSet.getString("BaseCurrencyCode"));
        baseCurrency.setName(resultSet.getString("BaseCurrencyName"));
        baseCurrency.setSign(resultSet.getString("BaseCurrencySign"));

        return baseCurrency;
    }
}
