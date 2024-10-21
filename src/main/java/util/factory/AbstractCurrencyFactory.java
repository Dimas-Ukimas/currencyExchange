package util.factory;

import entity.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractCurrencyFactory {

    public abstract Currency createCurrency(ResultSet resultSet) throws SQLException;
}
