package ru.job4j.grabber.service;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import ru.job4j.grabber.model.Post;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static final Logger LOGGER = Logger.getLogger(HabrCareerParse.class);
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PREFIX = "/vacancies?page=";
    private static final String SUFFIX = "&q=Java%20developer&type=all";
    private static final int PAGE_COUNT = 5;

    @Override
    public List<Post> fetch() {
        var result = new ArrayList<Post>();
        try {
            for (int i = 0; i < PAGE_COUNT; i++) {
                String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, i, SUFFIX);
                var connection = Jsoup.connect(fullLink);
                var document = connection.get();
                var rows = document.select(".vacancy-card__inner");

                rows.forEach(row -> {
                    var titleElement = row.select(".vacancy-card__title").first();
                    var linkElement = titleElement.child(0);
                    String vacancyName = titleElement.text();
                    String link = String.format("%s%s", SOURCE_LINK,
                            linkElement.attr("href"));
                    var dataElement = row.select("time").first();
                    String date = dataElement.attr("datetime");
                    ZonedDateTime vacancyDateTime = ZonedDateTime.parse(date);
                    long timestamp = vacancyDateTime.toInstant().toEpochMilli();
                    var post = new Post();
                    post.setTitle(vacancyName);
                    post.setLink(link);
                    post.setTime(timestamp);
                    result.add(post);
                });
            }
        } catch (IOException e) {
            LOGGER.error("When load page", e);
        }
        return result;
    }
}