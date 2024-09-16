package entity;

public class Currency {

    private int id;
    private String name;
    private String code;
    private String sign;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

//    public static class CurrencyBuilder {
//        private int id;
//        private String name;
//        private String code;
//        private String sign;
//
//        public CurrencyBuilder setId(int id) {
//            this.id = id;
//            return this;
//        }
//
//        public CurrencyBuilder setName(String name) {
//            this.name = name;
//            return this;
//
//        }
//
//        public CurrencyBuilder setCode(String code) {
//            this.code = code;
//            return this;
//        }
//
//        public CurrencyBuilder setSign(String sign) {
//            this.sign = sign;
//            return this;
//        }
//
//        public Currency build() {
//            return new Currency(this);
//        }
//    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", name=" + name +
                ", code=" + code +
                ", sign=" + sign +
                "}";

    }

}
