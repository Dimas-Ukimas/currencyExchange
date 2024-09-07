package entity;

public class Currency {

    private final int id;
    private final String name;
    private final String code;
    private final String sign;

    private Currency(CurrencyBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.code = builder.code;
        this.sign = builder.sign;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getSign() {
        return sign;
    }

    public static class CurrencyBuilder {
        private int id;
        private String name;
        private String code;
        private String sign;

        public CurrencyBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public CurrencyBuilder setName(String name) {
            this.name = name;
            return this;

        }

        public CurrencyBuilder setCode(String code) {
            this.code = code;
            return this;
        }

        public CurrencyBuilder setSign(String sign) {
            this.sign = sign;
            return this;
        }

        public Currency build() {
            return new Currency(this);
        }
    }

    @Override
    public String toString(){
        return "Currency{"+
                "id="+id+
                ", name="+name+
                ", code="+code+
                ", sign="+sign+
                "}";

    }

}
