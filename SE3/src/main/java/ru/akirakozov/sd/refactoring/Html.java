package ru.akirakozov.sd.refactoring;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Html {
    public static String generateHtmlBySql(String sql, boolean isIntRes, String prefix) {
        try {
            List<Product> products = null;
            if (isIntRes) {
                prefix += DataBase.getIntBySql(sql);
            } else {
                products = DataBase.getProductsBySql(sql);
            }
            return generateHtml(prefix, products, true, !isIntRes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateHtml(String prefix, List<Product> products, boolean isNeedHtmlBodyTags, boolean isNeedH1Tag) {
        StringBuilder html = new StringBuilder();
        if (isNeedHtmlBodyTags) {
            html.append("<html><body>");
        }
        if (prefix.length() != 0) {
            if (isNeedH1Tag) {
                html.append("<h1>").append(prefix).append("</h1>");
            } else {
                html.append(prefix);
            }
        }
        if (products != null) {
            for (Product product : products) {
                html.append(product.name).append("\t").append(product.price).append("</br>");
            }
        }
        if (isNeedHtmlBodyTags) {
            html.append("</body></html>");
        }
        return html.toString();
    }

    public static void writeHtml(HttpServletResponse response, String html) throws IOException {
        response.getWriter().println(html);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
