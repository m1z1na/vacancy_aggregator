package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabrCareerDateTimeParser implements DateTimeParser {
    private static final String pattern = "uuuu-MM-dd'T'HH:mm:ssXXX";
    private static final DateTimeFormatter formatter  = DateTimeFormatter.ofPattern(pattern);



    @Override
    public LocalDateTime parse(String parse) {
        LocalDateTime parsedDate = LocalDateTime.parse(parse, formatter);
        return parsedDate;
    }
}