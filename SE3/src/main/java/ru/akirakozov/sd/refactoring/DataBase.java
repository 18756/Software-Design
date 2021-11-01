package ru.akirakozov.sd.refactoring;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DataBase {
    private static final String DATA_BASE_PATH = "jdbc:sqlite:test.db";
    private static Connection connection;
    private static Statement stmt;

    private static void openConnection() throws SQLException {
        connection = DriverManager.getConnection(DATA_BASE_PATH);
        stmt = connection.createStatement();
    }

    private static void closeConnection() throws SQLException {
        stmt.close();
        connection.close();
        connection = null;
        stmt = null;
    }

    public static void makeSqlUpdateQuery(String sql) throws SQLException {
        openConnection();
        stmt.executeUpdate(sql);
        closeConnection();
    }

    public static List<Product> makeSqlGetQuery(String sql) throws SQLException {
        openConnection();
        ResultSet rs = stmt.executeQuery(sql);
        List<Product> products = new ArrayList<>();
        while (rs.next()) {
            String  name = rs.getString("name");
            int price  = rs.getInt("price");
            products.add(new Product(name, price));
        }
        rs.close();
        closeConnection();
        return products;
    }
}
