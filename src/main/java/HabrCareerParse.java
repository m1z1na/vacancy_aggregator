import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.DateTimeParser;
import utils.HabrCareerDateTimeParser;

import java.io.IOException;

public class HabrCareerParse {

    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }
    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);
    private static final int MAX_PAGE = 5;

    public static void main(String[] args) throws IOException {
        HabrCareerParse careerHabr = new HabrCareerParse(new HabrCareerDateTimeParser());
        for (int pageIterator = 1; pageIterator <= MAX_PAGE; pageIterator = pageIterator + 1) {
            Connection connection = Jsoup.connect(PAGE_LINK + pageIterator);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                String date = row.select(".basic-date").first().attr("datetime");
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                System.out.printf("%s %s %s  %n", vacancyName, link, careerHabr.dateTimeParser.parse(date));

                try {
                    System.out.printf(careerHabr.retrieveDescription(link));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            );

        }
    }


    private String retrieveDescription(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Elements rows = document.select(".vacancy-description__text");
        return rows.text();

    }
}


