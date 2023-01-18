package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabrCareerDateTimeParser implements DateTimeParser {
    private static final String pattern = "uuuu-MM-dd'T'HH:mm:ssXXX";
    @Override
    public LocalDateTime parse(String parse) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime parsedDate = LocalDateTime.parse(parse, formatter);
        return parsedDate;
    }
}