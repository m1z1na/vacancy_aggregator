package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Model.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HabrCareerParse implements Parse {

    private static DateTimeParser dateTimeParser;// = new HabrCareerDateTimeParser();
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);
    private static final int MAX_PAGE = 5;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }


    public static void main(String[] args) {
        HabrCareerParse careerHabr = new HabrCareerParse(new HabrCareerDateTimeParser());
        for (int pageIterator = 1; pageIterator <= MAX_PAGE; pageIterator = pageIterator + 1) {
            careerHabr.list(PAGE_LINK + pageIterator);
        }
    }


    @Override
    public List<Post> list(String link){
        try {
        List<Post> list = new ArrayList();
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Elements rows = document.select(".vacancy-card__inner");
        rows.forEach(row -> {
                    list.add(getElement(row));
                }
        );
        return list;
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    private String getDescriprion(String link) {
        try {
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Elements rows = document.select(".vacancy-description__text");
        return rows.text();
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    private Post getElement(Element row) {
        Element titleElement = row.select(".vacancy-card__title").first();
        Element linkElement = titleElement.child(0);
        String linkDescr = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
        return new Post(titleElement.text(),
                String.format("%s%s", SOURCE_LINK, linkElement.attr("href")),
                getDescriprion(linkDescr),
                HabrCareerParse.dateTimeParser.parse(row.select(".basic-date").first().attr("datetime"))
        );
    }
}
