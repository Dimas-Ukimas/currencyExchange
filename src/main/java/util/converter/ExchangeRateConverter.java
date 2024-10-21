package util.converter;

import dto.ExchangeRateRequestDto;
import dto.ExchangeRateResponseDto;
import entity.Currency;
import entity.ExchangeRate;

public class ExchangeRateConverter implements Converter<ExchangeRate, ExchangeRateRequestDto, ExchangeRateResponseDto> {

    @Override
    public ExchangeRate convertToEntity(ExchangeRateRequestDto dto) {

        ExchangeRate exchangeRate = new ExchangeRate();
        Currency baseCurrency = new Currency();
        Currency targetCurrency = new Currency();

        baseCurrency.setCode(dto.getBaseCurrencyCode());
        targetCurrency.setCode(dto.getTargetCurrencyCode());
        exchangeRate.setBaseCurrency(baseCurrency);
        exchangeRate.setTargetCurrency(targetCurrency);
        exchangeRate.setRate(dto.getRate());

        return exchangeRate;
    }

    @Override
    public ExchangeRateResponseDto convertToDto(ExchangeRate exchangeRate) {

        ExchangeRateResponseDto dto = new ExchangeRateResponseDto();

        dto.setId(exchangeRate.getId());
        dto.setBaseCurrency(exchangeRate.getBaseCurrency());
        dto.setTargetCurrency(exchangeRate.getTargetCurrency());
        dto.setRate(exchangeRate.getRate());

        return dto;
    }
}
