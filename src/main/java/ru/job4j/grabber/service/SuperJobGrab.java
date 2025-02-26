package ru.job4j.grabber.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.stores.Store;

import java.util.List;

public class SuperJobGrab implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        Parse parse = (Parse) context.getJobDetail().getJobDataMap().get("parse");
        Store store = (Store) context.getJobDetail().getJobDataMap().get("store");
        List<Post> posts = parse.fetch();
        posts.forEach(store::save);
    }
}