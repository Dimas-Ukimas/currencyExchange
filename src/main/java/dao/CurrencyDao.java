package dao;

import entity.Currency;
import util.ConnectionPool;
import util.factory.CurrencyFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao {

   private final CurrencyFactory currencyFactory = new CurrencyFactory();

    public Optional<Currency> saveCurrency(Currency currency) throws SQLException {
        final String query = "INSERT INTO Currencies (FullName, code, sign) VALUES (?,?,?)";

        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, currency.getName());
            preparedStatement.setString(2, currency.getCode());
            preparedStatement.setString(3, currency.getSign());

            try {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                int SQLITE_CONSTRAINT_ERROR_CODE = 19;
                if (e.getErrorCode() != SQLITE_CONSTRAINT_ERROR_CODE) {
                    throw new SQLException();
                }
                return Optional.empty();
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                currency.setId(generatedKeys.getInt(1));
            }
        }
        return Optional.of(currency);
    }


    public Optional<Currency> findCurrencyByCode(String code) throws SQLException {
        final String query = "SELECT * from Currencies WHERE code=?";

        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, code);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                return resultSet.next() ? Optional.of(currencyFactory.createCurrency(resultSet)) : Optional.empty();
            }
        }
    }

    public List<Currency> findAllCurrencies() throws SQLException {
        List<Currency> currencies = new ArrayList<>();

        final String query = "SELECT * from Currencies";

        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                currencies.add(currencyFactory.createCurrency(resultSet));
            }
            return currencies;
        }
    }


}
