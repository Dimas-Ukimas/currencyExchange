package service;

import dao.ExchangeRatesDao;
import dto.ExchangeRateRequestDto;
import dto.ExchangeRateResponseDto;
import entity.ExchangeRate;
import util.converter.ExchangeRateConverter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {

    private final ExchangeRatesDao dao = new ExchangeRatesDao();
    private final ExchangeRateConverter converter = new ExchangeRateConverter();

    public List<ExchangeRateResponseDto> getAllExchangeRates() throws SQLException {

        List<ExchangeRate> exchangeRates = dao.findAllExchangeRates();
        List<ExchangeRateResponseDto> exchangeRatesDtoList = new ArrayList<>();

        for (ExchangeRate e : exchangeRates) {
            ExchangeRateResponseDto dto = converter.convertToDto(e);
            exchangeRatesDtoList.add(dto);
        }

        return exchangeRatesDtoList;
    }

    public Optional<ExchangeRateResponseDto> getExchangeRate(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {

        Optional<ExchangeRate> exchangeRate = dao.findExchangeRate(baseCurrencyCode, targetCurrencyCode);

        return exchangeRate.map(converter::convertToDto);
    }

    public Optional<ExchangeRateResponseDto> saveExchangeRate(ExchangeRateRequestDto dto) throws SQLException {

        ExchangeRate exchangeRate = converter.convertToEntity(dto);
        Optional<ExchangeRate> savedExchangeRate = dao.saveExchangeRate(exchangeRate);

        return savedExchangeRate.map(converter::convertToDto);
    }

    public Optional<ExchangeRateResponseDto> updateExchangeRate(ExchangeRateRequestDto dto) throws SQLException {

        ExchangeRate exchangeRate = converter.convertToEntity(dto);
        Optional<ExchangeRate> updatedExchangeRate = dao.updateExchangeRate(exchangeRate);

        return updatedExchangeRate.map(converter::convertToDto);
    }


}
