package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.DataBase;
import ru.akirakozov.sd.refactoring.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        Consumer<ResultSet> consumerPrintOneInt = rs -> {
            try {
                if (rs.next()) {
                    response.getWriter().println(rs.getInt(1));
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        };

        if ("max".equals(command)) {
            try {
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h1>Product with max price: </h1>");
                String sql = "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
                List<Product> products = DataBase.getProductsBySql(sql);
                for (Product product : products) {
                    response.getWriter().println(product.name + "\t" + product.price + "</br>");
                }
                response.getWriter().println("</body></html>");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("min".equals(command)) {
            try {
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h1>Product with min price: </h1>");
                String sql = "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
                List<Product> products = DataBase.getProductsBySql(sql);
                for (Product product : products) {
                    response.getWriter().println(product.name + "\t" + product.price + "</br>");
                }
                response.getWriter().println("</body></html>");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("sum".equals(command)) {
            try {
                response.getWriter().println("<html><body>");
                response.getWriter().println("Summary price: ");
                String sql = "SELECT SUM(price) FROM PRODUCT";
                DataBase.makeSqlGetQuery(sql, consumerPrintOneInt);
                response.getWriter().println("</body></html>");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("count".equals(command)) {
            try {
                response.getWriter().println("<html><body>");
                response.getWriter().println("Number of products: ");
                String sql = "SELECT COUNT(*) FROM PRODUCT";
                DataBase.makeSqlGetQuery(sql, consumerPrintOneInt);
                response.getWriter().println("</body></html>");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            response.getWriter().println("Unknown command: " + command);
        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
