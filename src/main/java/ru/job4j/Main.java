package ru.job4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.service.*;
import ru.job4j.grabber.stores.JdbcStore;

import java.sql.SQLException;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            var config = new Config();
            config.load("application.properties");
            var store = new JdbcStore(config);
            var parse = new HabrCareerParse();
            var scheduler = new SchedulerManager();
            scheduler.init();
            scheduler.load(Integer.parseInt(config.get("rabbit.interval")),
                    SuperJobGrab.class,
                    store,
                    parse);
            new Web(store).start(Integer.parseInt(config.get("server.port")));
        } catch (SQLException e) {
            LOGGER.error("Database connection error", e);
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid number format in configuration", e);
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred", e);
        }
    }
}