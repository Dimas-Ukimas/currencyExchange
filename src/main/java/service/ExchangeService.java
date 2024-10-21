package service;

import dao.ExchangeRatesDao;
import dto.ExchangeRequestDto;
import dto.ExchangeResponseDto;
import entity.Currency;
import entity.ExchangeRate;
import util.converter.ExchangeConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Optional;

public class ExchangeService {

    private final ExchangeRatesDao dao = new ExchangeRatesDao();
    private final ExchangeConverter converter = new ExchangeConverter();

    public Optional<ExchangeResponseDto> doExchange(ExchangeRequestDto dto) throws SQLException {

        String baseCurrencyCode = dto.getBaseCurrencyCode();
        String targetCurrencyCode = dto.getTargetCurrencyCode();
        BigDecimal amount = dto.getAmount();

        Optional<ExchangeRate> exchangeResult = dao.findExchangeRate(baseCurrencyCode, targetCurrencyCode);

        if (exchangeResult.isEmpty()) {
            Optional<ExchangeRate> reversedExchangeRate = dao.findExchangeRate(targetCurrencyCode, baseCurrencyCode);

            if (reversedExchangeRate.isPresent()) {
                ExchangeRate directExchangeRate = getDirectExchangeRate(reversedExchangeRate.get());
                exchangeResult = Optional.of(directExchangeRate);

            } else {
                Optional<ExchangeRate> usdToBaseCurrency = dao.findExchangeRate("USD", baseCurrencyCode);
                Optional<ExchangeRate> usdToTargetCurrency = dao.findExchangeRate("USD", targetCurrencyCode);

                if (usdToBaseCurrency.isPresent() && usdToTargetCurrency.isPresent()) {
                    ExchangeRate crossExchangeRate = getCrossExchangeRate(usdToBaseCurrency.get(), usdToTargetCurrency.get());
                    exchangeResult = Optional.of(crossExchangeRate);
                }
            }
        }

        if (exchangeResult.isPresent()) {
            BigDecimal convertedAmount = exchangeResult.get().getRate().multiply(amount).setScale(6, RoundingMode.HALF_UP);

            return exchangeResult.map(exchangeRate -> converter.convertToDto(exchangeRate, amount, convertedAmount));
        }
        return Optional.empty();
    }

    private static ExchangeRate getCrossExchangeRate(ExchangeRate usdToBaseCurrency, ExchangeRate usdToTargetCurrency) {

        ExchangeRate crossExchangeRate = new ExchangeRate();

        Currency baseCurrency = usdToBaseCurrency.getTargetCurrency();
        Currency targetCurrency = usdToTargetCurrency.getTargetCurrency();
        BigDecimal crossRateThroughUsd = usdToTargetCurrency.getRate().divide(usdToBaseCurrency.getRate(), 6, RoundingMode.HALF_UP);


        crossExchangeRate.setBaseCurrency(baseCurrency);
        crossExchangeRate.setTargetCurrency(targetCurrency);
        crossExchangeRate.setRate(crossRateThroughUsd);

        return crossExchangeRate;
    }

    private static ExchangeRate getDirectExchangeRate(ExchangeRate reversedExchangeRate) {

        ExchangeRate directExchangeRate = new ExchangeRate();

        Currency baseCurrency = reversedExchangeRate.getTargetCurrency();
        Currency targetCurrency = reversedExchangeRate.getBaseCurrency();
        BigDecimal directRate = BigDecimal.ONE.divide(reversedExchangeRate.getRate(), 6, RoundingMode.HALF_UP);


        directExchangeRate.setBaseCurrency(baseCurrency);
        directExchangeRate.setTargetCurrency(targetCurrency);
        directExchangeRate.setRate(directRate);

        return directExchangeRate;
    }
}
