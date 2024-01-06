package com.example.widgetandroid;

public class CurrencyDetails {
    private String countryName;
    private String countryURi;
    private String Amount;

    public CurrencyDetails(String countryName, String countryURi, String amount) {
        this.countryName = countryName;
        this.countryURi = countryURi;
        Amount = amount;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryURi() {
        return countryURi;
    }

    public void setCountryURi(String countryURi) {
        this.countryURi = countryURi;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
