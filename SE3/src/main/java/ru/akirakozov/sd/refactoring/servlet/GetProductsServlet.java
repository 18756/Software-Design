package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.Html;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String html = Html.generateHtmlBySql("SELECT * FROM PRODUCT", false, "");
        Html.writeHtml(response, html);
    }
}
