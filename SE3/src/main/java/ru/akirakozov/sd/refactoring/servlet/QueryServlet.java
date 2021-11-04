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

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        HtmlWriter htmlWriter = new HtmlWriter(response.getWriter());

        if ("max".equals(command)) {
            htmlWriter.writeHtmlBySql("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1",
                    false, "Product with max price: ");
        } else if ("min".equals(command)) {
            htmlWriter.writeHtmlBySql("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1",
                    false, "Product with min price: ");
        } else if ("sum".equals(command)) {
            htmlWriter.writeHtmlBySql("SELECT SUM(price) FROM PRODUCT",
                    true, "Summary price: ");
        } else if ("count".equals(command)) {
            htmlWriter.writeHtmlBySql("SELECT COUNT(*) FROM PRODUCT",
                    true, "Number of products: ");
        } else {
            response.getWriter().println("Unknown command: " + command);
        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
