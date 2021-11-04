package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.DataBase;
import ru.akirakozov.sd.refactoring.HtmlWriter;
import ru.akirakozov.sd.refactoring.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        HtmlWriter htmlWriter = new HtmlWriter(response.getWriter());

        if ("max".equals(command)) {
            try {
                String sql = "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
                List<Product> products = DataBase.getProductsBySql(sql);
                htmlWriter.writeHtml("Product with max price: ", products, true, true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("min".equals(command)) {
            try {
                String sql = "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
                List<Product> products = DataBase.getProductsBySql(sql);
                htmlWriter.writeHtml("Product with min price: ", products, true, true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("sum".equals(command)) {
            try {
                String sql = "SELECT SUM(price) FROM PRODUCT";
                int res = DataBase.getIntBySql(sql);
                htmlWriter.writeHtml("Summary price: " + res, null, true, false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("count".equals(command)) {
            try {
                String sql = "SELECT COUNT(*) FROM PRODUCT";
                int res = DataBase.getIntBySql(sql);
                htmlWriter.writeHtml("Number of products: " + res, null, true, false);
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
