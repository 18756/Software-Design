package ru.asmirnov.sd.rxjava.reactive_mongo_driver;

import java.util.Map;

public class CurrencyChanger {
    private static final Map<User.Currency, Double> rates = Map.of(
            User.Currency.USD, 1.0,
            User.Currency.RUB, 110.0,
            User.Currency.EUR, 0.9
    );

    public static double getRate(User.Currency currency) {
        return rates.get(currency);
    }
}
