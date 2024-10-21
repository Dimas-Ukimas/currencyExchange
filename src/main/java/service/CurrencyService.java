package service;

import dao.CurrencyDao;
import dto.CurrencyRequestDto;
import dto.CurrencyResponseDto;
import entity.Currency;
import util.converter.CurrencyConverter;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CurrencyService {

    private final CurrencyDao dao = new CurrencyDao();
    private final CurrencyConverter currencyConverter = new CurrencyConverter();

    public Optional<CurrencyResponseDto> getCurrency(String currencyCode) throws SQLException {

        Optional<Currency> currency = dao.findCurrencyByCode(currencyCode);

        return currency.map(currencyConverter::convertToDto);
    }

    public List<CurrencyResponseDto> getAllCurrencies() throws SQLException {

        List<Currency> currencies = dao.findAllCurrencies();

        return currencies.stream()
                .map(currencyConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<CurrencyResponseDto> saveCurrency(CurrencyRequestDto reqDto) throws SQLException {

        Currency currency = currencyConverter.convertToEntity(reqDto);
        Optional<Currency> savedCurrency = dao.saveCurrency(currency);

        return savedCurrency.map(currencyConverter::convertToDto);
    }
}
