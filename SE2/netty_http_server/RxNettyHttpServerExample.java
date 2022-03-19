package ru.asmirnov.sd.rxjava.netty_http_server;

import io.reactivex.netty.protocol.http.server.HttpServer;
import ru.asmirnov.sd.rxjava.data_base.DataBase;
import ru.asmirnov.sd.rxjava.reactive_mongo_driver.Product;
import ru.asmirnov.sd.rxjava.reactive_mongo_driver.User;
import rx.Observable;

import java.util.List;
import java.util.Map;

/**
 * @author asmirnov
 */
public class RxNettyHttpServerExample {
    private final static DataBase dataBase = new DataBase();

    public static void main(final String[] args) {

        HttpServer
                .newServer(8080)
                .start((req, resp) -> {
                    String action = req.getDecodedPath().substring(1);
                    Map<String, List<String>> params = req.getQueryParameters();
                    switch (action) {
                        case "addUser": {
                            addUser(
                                    Integer.parseInt(getParam("id", params)),
                                    getParam("name", params),
                                    getParam("login", params),
                                    getParam("currency", params)
                            );
                            return resp.writeString(Observable.just("user added"));
                        }
                        case "addProduct": {
                            addProduct(
                                    Integer.parseInt(getParam("id", params)),
                                    getParam("name", params),
                                    Double.parseDouble(getParam("priceInUSD", params))
                            );
                            return resp.writeString(Observable.just("product added"));
                        }
                        case "getProducts": return resp.writeString(
                                getProducts(Integer.parseInt(getParam("id", params)))
                        );
                        default: return resp.writeString(Observable.just("No such action: " + action));
                    }
                })
                .awaitShutdown();
    }

    private static void addProduct(int id, String name, double priceInUSD) {
        Product newProduct = new Product(id, name, priceInUSD);
        dataBase.productAddSubject.onNext(newProduct);
    }

    private static void addUser(int id, String name, String login, String currency) {
        User newUser = new User(id, name, login, currency);
        dataBase.userAddSubject.onNext(newUser);
    }

    private static Observable<String> getProducts(int userId) {
        Observable<User> user = dataBase.getUserById(userId);
        Observable<Product> products = dataBase.getProducts();
        return user.flatMap(u -> products.map(p -> p.toString(u.currency)));
    }

    private static String getParam(String key, Map<String, List<String>> params) {
        if (!params.containsKey(key)) {
            throw new RuntimeException("No such parameter: " + key);
        }
        return params.get(key).get(0);
    }
}
