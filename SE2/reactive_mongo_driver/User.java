package ru.asmirnov.sd.rxjava.reactive_mongo_driver;

import org.bson.Document;

/**
 * @author asmirnov
 */
public class User {
    public final int id;
    public final String name;
    public final String login;
    public final Currency currency;


    public User(Document doc) {
        this(
                doc.getDouble("id").intValue(),
                doc.getString("name"),
                doc.getString("login"),
                doc.getString("currency")
        );
    }

    public User(int id, String name, String login, String currency) {
        this(id, name, login, getCurrency(currency));
    }

    public User(int id, String name, String login, Currency currency) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }

    private static Currency getCurrency(String currency) {
        switch (currency) {
            case "RUB" : return Currency.RUB;
            case "EUR" : return Currency.EUR;
            case "USD" : return Currency.USD;
            default : throw new RuntimeException("No such currency: " + currency);
        }
    }

    enum Currency {
        RUB,
        EUR,
        USD
    }
}
