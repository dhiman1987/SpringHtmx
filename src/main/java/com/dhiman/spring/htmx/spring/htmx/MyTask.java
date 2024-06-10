package com.dhiman.spring.htmx.spring.htmx;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Getter
public class MyTask implements Runnable{

    private final String id = UUID.randomUUID().toString();
    private String status = "NOT_STARTED";
    private final String name;
    private final long executionTime;
    private double score = 0.0;
    private final DummyRepository repository;

    public MyTask(String name, long executionTime, DummyRepository repository) {
        this.name = name;
        this.executionTime = executionTime;
        this.repository = repository;
    }

    @Override
    public void run() {
        log.info("String Task {} - {}", id,name);
        status="STARTED";
        try {
            Thread.sleep(executionTime);
        } catch (InterruptedException e) {
            log.error(e.toString());
        }
        score = ((Math.random() * (10.0 - 1.0)) + 1.0);
        status="COMPLETED";
        repository.saveTask(this);
        log.info("Completed Task {} - {}", id,name);
    }
}
