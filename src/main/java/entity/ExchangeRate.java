package entity;

import java.math.BigDecimal;

public class ExchangeRate {

    private int id;
    private int BaseCurrencyId;
    private int TargetCurrencyId;
    private BigDecimal Rate;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBaseCurrencyId() {
        return BaseCurrencyId;
    }

    public void setBaseCurrencyId(int baseCurrencyId) {
        BaseCurrencyId = baseCurrencyId;
    }

    public int getTargetCurrencyId() {
        return TargetCurrencyId;
    }

    public void setTargetCurrencyId(int targetCurrencyId) {
        TargetCurrencyId = targetCurrencyId;
    }

    public BigDecimal getRate() {
        return Rate;
    }

    public void setRate(BigDecimal rate) {
        Rate = rate;
    }


}
