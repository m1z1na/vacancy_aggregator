package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class HabrCareerDateTimeParserTest {

    @Test
     void checkDate(){
        HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
        assertThat(parser.parse("2011-12-03T10:15:30+01:00")).isEqualTo("2011-12-03T10:15:30");
    }

}