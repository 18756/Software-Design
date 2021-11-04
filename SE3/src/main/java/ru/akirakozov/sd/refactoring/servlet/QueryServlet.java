package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.Html;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        String html = "";
        if ("max".equals(command)) {
            html = Html.generateHtmlBySql("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1",
                    false, "Product with max price: ");
        } else if ("min".equals(command)) {
            html = Html.generateHtmlBySql("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1",
                    false, "Product with min price: ");
        } else if ("sum".equals(command)) {
            html = Html.generateHtmlBySql("SELECT SUM(price) FROM PRODUCT",
                    true, "Summary price: ");
        } else if ("count".equals(command)) {
            html = Html.generateHtmlBySql("SELECT COUNT(*) FROM PRODUCT",
                    true, "Number of products: ");
        } else {
            html = "Unknown command: " + command;
        }
        Html.writeHtml(response, html);
    }
}