package util.factory;

import entity.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyFactory extends AbstractCurrencyFactory {

    @Override
    public Currency createCurrency(ResultSet resultSet) throws SQLException {

        Currency currency = new Currency();

        currency.setId(resultSet.getInt("id"));
        currency.setName(resultSet.getString("FullName"));
        currency.setCode(resultSet.getString("code"));
        currency.setSign(resultSet.getString("sign"));

        return currency;
    }
}
