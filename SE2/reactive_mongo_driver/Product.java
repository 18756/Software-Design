package ru.asmirnov.sd.rxjava.reactive_mongo_driver;

import org.bson.Document;

/**
 * @author asmirnov
 */
public class Product {
    public final int id;
    public final String name;
    public final double priceInUSD;


    public Product(Document doc) {
        this(
                doc.getDouble("id").intValue(),
                doc.getString("name"),
                doc.getDouble("priceInUSD")
        );
    }

    public Product(int id, String name, double priceInUSD) {
        this.id = id;
        this.name = name;
        this.priceInUSD = priceInUSD;
    }

    public String toString(User.Currency currency) {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + priceInUSD * CurrencyChanger.getRate(currency) + '\'' +
                '}';
    }
}
