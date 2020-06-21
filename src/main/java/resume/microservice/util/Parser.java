package resume.microservice.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import resume.microservice.exception.CantCompleteClientRequestException;

public class Parser {

    private Document doc;

    public Parser(String input) {

        doc = Jsoup.parse(input,"UTF-8");
    }

    public String getData(int i) {

        if ( doc == null )  throw new CantCompleteClientRequestException("doc-parse is null  ");

        StringBuilder result = new StringBuilder();

        Element news = doc.select("div[id=\"news\"]").first();

        Element data = news.select("div[class=\"news_v2_item\"]").get(i).select("div[class=\"news_v2_text\"]").first();

        result.append(data.getElementsByClass("news_v2_title").select("a").text());

        result.append("<br>");

        result.append(data.getElementsByClass("news_v2_preview").text());

        return result.toString();

    }



}
