package util.factory;

import entity.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;


public class TargetCurrencyFactory extends AbstractCurrencyFactory {

    @Override
    public Currency createCurrency(ResultSet resultSet) throws SQLException {

        Currency targetCurrency = new Currency();

        targetCurrency.setId(resultSet.getInt("TargetCurrencyId"));
        targetCurrency.setCode(resultSet.getString("TargetCurrencyCode"));
        targetCurrency.setName(resultSet.getString("TargetCurrencyName"));
        targetCurrency.setSign(resultSet.getString("TargetCurrencySign"));

        return targetCurrency;

    }
}
