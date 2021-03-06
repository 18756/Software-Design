package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class ProductsTest {
    private Server server;
    private final String URL_PREFIX = "http://localhost:8081/";
    private final Random random = new Random();

    @BeforeEach
    public void setUp() throws Exception {
        cleanDataBase();
        server = Main.makeServer();
        server.start();
    }

    @AfterEach
    public void tearDown() throws Exception {
        server.stop();
        cleanDataBase();
    }

    private void cleanDataBase() throws SQLException {
        DataBase.makeSqlUpdateQuery("DROP TABLE IF EXISTS PRODUCT");
    }

    private String makeQuery(String url) {
        try (BufferedReader responseReader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            return responseReader.lines().collect(Collectors.joining());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed url: " + url);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String getProducts(boolean canRepeat) {
        String response = makeQuery(URL_PREFIX + "get-products");
        if (canRepeat) {
            while (random.nextInt(2) == 0) {
                Assertions.assertEquals(response, makeQuery(URL_PREFIX + "get-products"));
            }
        }
        return response;
    }

    private String addProduct(String productName, int price) {
        String response = makeQuery(URL_PREFIX + "add-product?name=" + productName + "&price=" + price);
        Assertions.assertEquals("OK", response);
        return response;
    }

    private String makeCommand(String command, boolean canRepeat) {
        String response = makeQuery(URL_PREFIX + "query?command=" + command);
        if (canRepeat) {
            while (random.nextInt(2) == 0) {
                Assertions.assertEquals(response, makeQuery(URL_PREFIX + "query?command=" + command));
            }
        }
        return response;
    }

    private String makeHtml(String text) {
        return Html.generateHtml(text, null, true, false);
    }

    private void addProducts(List<Product> products) {
        for (Product product : products) {
            addProduct(product.name, product.price);
        }
    }

    @Test
    public void noProductsTest() throws InterruptedException {
        Assertions.assertEquals(makeHtml(""), getProducts(true));
        Assertions.assertEquals(makeHtml("Summary price: 0"), makeCommand("sum", true));
        Assertions.assertEquals(makeHtml("<h1>Product with max price: </h1>"), makeCommand("max", true));
        Assertions.assertEquals(makeHtml("<h1>Product with min price: </h1>"), makeCommand("min", true));
        Assertions.assertEquals(makeHtml("Number of products: 0"), makeCommand("count", true));

    }

    @Test
    public void oneProductTest() throws InterruptedException {
        addProducts(List.of(new Product("p1", 10)));
        Assertions.assertEquals(makeHtml("p1\t10</br>"), getProducts(true));
        Assertions.assertEquals(makeHtml("Summary price: 10"), makeCommand("sum", true));
        Assertions.assertEquals(makeHtml("<h1>Product with max price: </h1>p1\t10</br>"), makeCommand("max", true));
        Assertions.assertEquals(makeHtml("<h1>Product with min price: </h1>p1\t10</br>"), makeCommand("min", true));
        Assertions.assertEquals(makeHtml("Number of products: 1"), makeCommand("count", true));
    }

    @Test
    public void twoProductsTest() throws InterruptedException {
        addProducts(List.of(new Product("p1", 10), new Product("p2", 20)));
        Assertions.assertEquals(makeHtml("p1\t10</br>p2\t20</br>"), getProducts(true));
        Assertions.assertEquals(makeHtml("Summary price: 30"), makeCommand("sum", true));
        Assertions.assertEquals(makeHtml("<h1>Product with max price: </h1>p2\t20</br>"), makeCommand("max", true));
        Assertions.assertEquals(makeHtml("<h1>Product with min price: </h1>p1\t10</br>"), makeCommand("min", true));
        Assertions.assertEquals(makeHtml("Number of products: 2"), makeCommand("count", true));
    }

    @RepeatedTest(3)
    public void randomTest() throws Exception {
        int productsAmount = 1 + random.nextInt(100);
        List<Product> products = new ArrayList<>();
        int sumPrice = 0;
        int maxPrice = Integer.MIN_VALUE;
        Set<String> mostExpensiveProductNames = new HashSet<>();
        int minPrice = Integer.MAX_VALUE;
        Set<String> mostCheapestProductNames = new HashSet<>();
        for (int j = 0; j < productsAmount; j++) {
            Product product = new Product("p" + j, random.nextInt(100));
            products.add(product);
            sumPrice += product.price;
            if (maxPrice == product.price) {
                mostExpensiveProductNames.add(product.name);
            }
            if (maxPrice < product.price) {
                maxPrice = product.price;
                mostExpensiveProductNames.clear();
                mostExpensiveProductNames.add(product.name);
            }
            if (minPrice == product.price) {
                mostCheapestProductNames.add(product.name);
            }
            if (minPrice > product.price) {
                minPrice = product.price;
                mostCheapestProductNames.clear();
                mostCheapestProductNames.add(product.name);
            }
        }
        addProducts(products);
        Assertions.assertEquals(makeHtml("Summary price: " + sumPrice),
                makeCommand("sum", true));

        Assertions.assertEquals(makeHtml("Number of products: " + products.size()),
                makeCommand("count", true));

        List<Product> getProductResponse = parseResponse(
                getProducts(true),
                "<html><body>",
                "</body></html>");
        Assertions.assertEquals(new HashSet<>(products), new HashSet<>(getProductResponse));

        List<Product> maxProductResponse = parseResponse(
                makeCommand("max", true),
                "<html><body><h1>Product with max price: </h1>",
                "</body></html>");

        Assertions.assertTrue(maxProductResponse.size() == 1 &&
                maxProductResponse.get(0).price == maxPrice &&
                mostExpensiveProductNames.contains(maxProductResponse.get(0).name));

        List<Product> minProductResponse = parseResponse(
                makeCommand("min", true),
                "<html><body><h1>Product with min price: </h1>",
                "</body></html>");

        Assertions.assertTrue(minProductResponse.size() == 1 &&
                minProductResponse.get(0).price == minPrice &&
                mostCheapestProductNames.contains(minProductResponse.get(0).name));
    }

    private List<Product> parseResponse(String response, String prefix, String suffix) throws Exception {
        if (response.startsWith(prefix) && response.endsWith(suffix)) {
            response = response.substring(prefix.length(), response.length() - suffix.length());
            String[] productStrings = response.split("</br>");
            List<Product> products = new ArrayList<>();
            for (String productString : productStrings) {
                String[] nameAndPrice = productString.split("\t");
                if (nameAndPrice.length != 2) {
                    throw new Exception("Incorrect response format: " + response);
                }
                products.add(new Product(nameAndPrice[0], Integer.parseInt(nameAndPrice[1])));
            }
            return products;
        } else {
            throw new Exception("Incorrect response format: \n" + response);
        }
    }
}