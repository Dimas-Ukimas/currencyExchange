package dao;

import entity.ExchangeRate;
import util.ConnectionPool;
import util.factory.ExchangeRateFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ExchangeRatesDao {

    ExchangeRateFactory exchangeRateFactory = new ExchangeRateFactory();

    public List<ExchangeRate> findAllExchangeRates() throws SQLException {

        final String query = "SELECT\n" +
                "    ExchangeRates.id AS id,\n" +
                "    bc.id AS BaseCurrencyId,\n" +
                "    bc.code AS BaseCurrencyCode,\n" +
                "    bc.FullName AS BaseCurrencyName,\n" +
                "    bc.sign AS BaseCurrencySign,\n" +
                "    tc.id AS TargetCurrencyId,\n" +
                "    tc.code AS TargetCurrencyCode,\n" +
                "    tc.FullName AS TargetCurrencyName,\n" +
                "    tc.sign AS TargetCurrencySign,\n" +
                "    ExchangeRates.rate AS rate\n" +
                "FROM ExchangeRates\n" +
                "         JOIN currencies bc ON ExchangeRates.BaseCurrencyId = bc.id\n" +
                "         JOIN currencies tc ON ExchangeRates.TargetCurrencyId = tc.id";
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                ExchangeRate exchangeRate = exchangeRateFactory.createExchangeRate(resultSet);
                exchangeRates.add(exchangeRate);
            }
        }

        return exchangeRates;
    }

    public Optional<ExchangeRate> findExchangeRate(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {

        String query = "SELECT\n" +
                "    ExchangeRates.id AS id,\n" +
                "    bc.id AS BaseCurrencyId,\n" +
                "    bc.Code AS BaseCurrencyCode,\n" +
                "    bc.FullName AS BaseCurrencyName,\n" +
                "    bc.sign AS BaseCurrencySign,\n" +
                "    tc.id AS TargetCurrencyId,\n" +
                "    tc.Code AS TargetCurrencyCode,\n" +
                "    tc.FullName AS TargetCurrencyName,\n" +
                "    tc.sign AS TargetCurrencySign,\n" +
                "    ExchangeRates.Rate AS rate\n" +
                "FROM ExchangeRates\n" +
                "         JOIN Currencies bc ON ExchangeRates.BaseCurrencyId = bc.ID\n" +
                "         JOIN Currencies tc ON ExchangeRates.TargetCurrencyId = tc.ID\n" +
                "WHERE bc.Code = ? AND tc.Code = ?";

        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, baseCurrencyCode);
            preparedStatement.setString(2, targetCurrencyCode);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                return resultSet.next() ? Optional.of(exchangeRateFactory.createExchangeRate(resultSet)) : Optional.empty();
            }
        }
    }

    public ExchangeRate saveExchangeRate(ExchangeRate exchangeRate) throws SQLException {

        String currencyCheckQuery = "SELECT COUNT(*) FROM Currencies WHERE Code IN (?,?)";

        String insertQuery = "INSERT into ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate)\n" +
                "VALUES ((SELECT id FROM Currencies WHERE Code = ?), \n" +
                "        (SELECT id FROM Currencies WHERE Code = ?),\n" +
                "        ?)";

        String selectQuery = "SELECT\n" +
                "    ExchangeRates.id AS id,\n" +
                "    bc.id AS BaseCurrencyId,\n" +
                "    bc.Code AS BaseCurrencyCode,\n" +
                "    bc.FullName AS BaseCurrencyName,\n" +
                "    bc.sign AS BaseCurrencySign,\n" +
                "    tc.id AS TargetCurrencyId,\n" +
                "    tc.Code AS TargetCurrencyCode,\n" +
                "    tc.FullName AS TargetCurrencyName,\n" +
                "    tc.sign AS TargetCurrencySign,\n" +
                "    ExchangeRates.Rate AS rate\n" +
                "FROM ExchangeRates\n" +
                "         JOIN Currencies bc ON ExchangeRates.BaseCurrencyId = bc.ID\n" +
                "         JOIN Currencies tc ON ExchangeRates.TargetCurrencyId = tc.ID\n" +
                "WHERE bc.Code = ? AND tc.Code = ?";

        try (Connection connection = ConnectionPool.get();
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement currencyCheckStatement = connection.prepareStatement(currencyCheckQuery)) {

            insertStatement.setString(1, exchangeRate.getBaseCurrency().getCode());
            insertStatement.setString(2, exchangeRate.getTargetCurrency().getCode());
            insertStatement.setBigDecimal(3, exchangeRate.getRate());

            selectStatement.setString(1, exchangeRate.getBaseCurrency().getCode());
            selectStatement.setString(2, exchangeRate.getTargetCurrency().getCode());

            currencyCheckStatement.setString(1, exchangeRate.getBaseCurrency().getCode());
            currencyCheckStatement.setString(2, exchangeRate.getTargetCurrency().getCode());

            try (ResultSet resultSet = currencyCheckStatement.executeQuery()) {

                int rowCount = resultSet.getInt(1);
                if (rowCount < 2) {
                    throw new SQLException("404");
                }
            }

            insertStatement.executeUpdate();

            try (ResultSet resultSet = selectStatement.executeQuery()) {

                return exchangeRateFactory.createExchangeRate(resultSet);
            }
        }
    }

    public Optional<ExchangeRate> updateExchangeRate(ExchangeRate exchangeRate) throws SQLException {

        String updateQuery = "UPDATE ExchangeRates\n" +
                "SET Rate = ? \n" +
                "WHERE BaseCurrencyId = (SELECT id FROM Currencies WHERE Code = ?)\n" +
                "AND TargetCurrencyId = (SELECT id FROM Currencies WHERE Code = ?) ";

        String selectQuery = "SELECT\n" +
                "    ExchangeRates.id AS id,\n" +
                "    bc.id AS BaseCurrencyId,\n" +
                "    bc.Code AS BaseCurrencyCode,\n" +
                "    bc.FullName AS BaseCurrencyName,\n" +
                "    bc.sign AS BaseCurrencySign,\n" +
                "    tc.id AS TargetCurrencyId,\n" +
                "    tc.Code AS TargetCurrencyCode,\n" +
                "    tc.FullName AS TargetCurrencyName,\n" +
                "    tc.sign AS TargetCurrencySign,\n" +
                "    ExchangeRates.Rate AS rate\n" +
                "FROM ExchangeRates\n" +
                "         JOIN Currencies bc ON ExchangeRates.BaseCurrencyId = bc.ID\n" +
                "         JOIN Currencies tc ON ExchangeRates.TargetCurrencyId = tc.ID\n" +
                "WHERE bc.Code = ? AND tc.Code = ?";

        try (Connection connection = ConnectionPool.get();
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {

            updateStatement.setBigDecimal(1, exchangeRate.getRate());
            updateStatement.setString(2, exchangeRate.getBaseCurrency().getCode());
            updateStatement.setString(3, exchangeRate.getTargetCurrency().getCode());

            selectStatement.setString(1, exchangeRate.getBaseCurrency().getCode());
            selectStatement.setString(2, exchangeRate.getTargetCurrency().getCode());

            int rowCount = updateStatement.executeUpdate();

            if (rowCount == 0) {

                return Optional.empty();
            }

            try (ResultSet resultSet = selectStatement.executeQuery()) {

                return Optional.of(exchangeRateFactory.createExchangeRate(resultSet));
            }
        }
    }
}
