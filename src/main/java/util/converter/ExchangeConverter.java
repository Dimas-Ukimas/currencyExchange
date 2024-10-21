package util.converter;

import dto.ExchangeResponseDto;
import entity.ExchangeRate;

import java.math.BigDecimal;

public class ExchangeConverter {

    public ExchangeResponseDto convertToDto(ExchangeRate exchangeRate, BigDecimal amount, BigDecimal convertedAmount) {

        ExchangeResponseDto dto = new ExchangeResponseDto();

        dto.setBaseCurrency(exchangeRate.getBaseCurrency());
        dto.setTargetCurrency(exchangeRate.getTargetCurrency());
        dto.setRate(exchangeRate.getRate());
        dto.setAmount(amount);
        dto.setConvertedAmount(convertedAmount);

        return dto;
    }
}
