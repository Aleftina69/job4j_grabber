package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class HabrCareerDateTimeParser implements DateTimeParser {
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'H:mm:ss");

    @Override
    public LocalDateTime parse(String parse) {
        if (parse == null) {
            throw new IllegalArgumentException("Invalid date format: null");
        }
        try {
            return LocalDateTime.parse(parse, INPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + parse, e);
        }
    }
}