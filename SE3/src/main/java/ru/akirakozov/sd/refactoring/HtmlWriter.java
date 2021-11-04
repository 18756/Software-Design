package ru.akirakozov.sd.refactoring;

import java.io.PrintWriter;
import java.util.List;

public class HtmlWriter {
    private final PrintWriter writer;

    public HtmlWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public void writeHtmlBySql(String sql, boolean isIntRes, String prefix) {
        try {
            List<Product> products = null;
            if (isIntRes) {
                prefix += DataBase.getIntBySql(sql);
            } else {
                products = DataBase.getProductsBySql(sql);
            }
            writeHtml(prefix, products, true, !isIntRes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeHtml(String prefix, List<Product> products, boolean isNeedHtmlBodyTags, boolean isNeedH1Tag) {
        if (isNeedHtmlBodyTags) {
            writer.println("<html><body>");
        }
        if (prefix.length() != 0) {
            if (isNeedH1Tag) {
                writer.println("<h1>" + prefix + "</h1>");
            } else {
                writer.println(prefix);
            }
        }
        if (products != null) {
            for (Product product : products) {
                writer.println(product.name + "\t" + product.price + "</br>");
            }
        }
        if (isNeedHtmlBodyTags) {
            writer.println("</body></html>");
        }
    }
}
