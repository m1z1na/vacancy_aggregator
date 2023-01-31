package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabrCareerDateTimeParser implements DateTimeParser {
    private static final String PATTERN = "uuuu-MM-dd'T'HH:mm:ssXXX";
    private static final DateTimeFormatter FORMATTER  = DateTimeFormatter.ofPattern(PATTERN);



    @Override
    public LocalDateTime parse(String parse) {
        LocalDateTime parsedDate = LocalDateTime.parse(parse, FORMATTER);
        return parsedDate;
    }
}