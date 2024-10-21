package util.converter;

import dto.CurrencyRequestDto;
import dto.CurrencyResponseDto;
import entity.Currency;

public class CurrencyConverter implements Converter<Currency, CurrencyRequestDto, CurrencyResponseDto> {


    @Override
    public Currency convertToEntity(CurrencyRequestDto reqDto) {

        Currency currency = new Currency();

        currency.setCode(reqDto.getCode());
        currency.setName(reqDto.getName());
        currency.setSign(reqDto.getSign());

        return currency;
    }

    @Override
    public CurrencyResponseDto convertToDto(Currency currency) {

        CurrencyResponseDto dto = new CurrencyResponseDto();

        dto.setId(currency.getId());
        dto.setCode(currency.getCode());
        dto.setName(currency.getName());
        dto.setSign(currency.getSign());

        return dto;
    }
}
