package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class HabrCareerDateTimeParserTest {
    private final HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();

    @Test
    public void testParseValidDateWithoutOffset() {
        String inputDate = "2023-03-15T10:30:00";
        LocalDateTime expectedDateTime = LocalDateTime.of(2023, 3, 15, 10, 30);
        LocalDateTime actualDateTime = parser.parse(inputDate);
        assertEquals(expectedDateTime, actualDateTime);
    }

    @Test
    public void testParseValidDateWithSeconds() {
        String inputDate = "2023-03-15T10:30:45";
        LocalDateTime expectedDateTime = LocalDateTime.of(2023, 3, 15, 10, 30, 45);
        LocalDateTime actualDateTime = parser.parse(inputDate);
        assertEquals(expectedDateTime, actualDateTime);
    }

    @Test
    public void testParseDateWithSingleDigitHour() {
        String inputDate = "2023-03-15T1:05:00";
        LocalDateTime expectedDateTime = LocalDateTime.of(2023, 3, 15, 1, 5);
        LocalDateTime actualDateTime = parser.parse(inputDate);
        assertEquals(expectedDateTime, actualDateTime);
    }

    @Test
    public void testParseInvalidDate() {
        String inputDate = "invalid-date";
        assertThrows(IllegalArgumentException.class, () -> parser.parse(inputDate));
    }

    @Test
    public void testParseNullDate() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(null));
    }
}