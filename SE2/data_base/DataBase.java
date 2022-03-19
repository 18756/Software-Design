package ru.asmirnov.sd.rxjava.data_base;

import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoDatabase;
import org.bson.Document;
import ru.asmirnov.sd.rxjava.reactive_mongo_driver.Product;
import ru.asmirnov.sd.rxjava.reactive_mongo_driver.User;
import rx.Observable;
import rx.subjects.PublishSubject;


public class DataBase {
    private static final String USER = "user";
    private static final String PRODUCT = "product";

    public MongoClient client = createMongoClient();
    private final MongoDatabase database = client.getDatabase("test");

    public PublishSubject<User> userAddSubject = PublishSubject.create();
    public PublishSubject<Product> productAddSubject = PublishSubject.create();

    public DataBase() {
        database.createCollection(USER);
        database.createCollection(PRODUCT);
        userAddSubject.subscribe(this::addUser);
        productAddSubject.subscribe(this::addProduct);
    }

    private void addProduct(Product product) {
        Document document = new Document();
        document.put("id", product.id);
        document.put("name", product.name);
        document.put("priceInUSD", product.priceInUSD);
        database.getCollection(PRODUCT).insertOne(document);
    }

    private void addUser(User user) {
        Document document = new Document();
        document.put("id", user.id);
        document.put("name", user.name);
        document.put("login", user.login);
        document.put("currency", user.currency);
        database.getCollection(USER).insertOne(document);
    }

    public Observable<Product> getProducts() {
        return database.getCollection(PRODUCT).find().toObservable().map(Product::new);
    }

    public Observable<User> getUserById(int id) {
        return database.getCollection(USER).find().toObservable()
                .filter(doc -> doc.getDouble("id").intValue() == id)
                .map(User::new);
    }

    private MongoClient createMongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }
}
