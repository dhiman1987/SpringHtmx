package com.dhiman.spring.htmx.spring.htmx;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Getter
@ToString
public class MyTask implements Runnable{

    private final String id = UUID.randomUUID().toString();
    private final String name;
    private final long executionTime;
    private final TaskRepository repository;
    private TaskEntity task;

    public MyTask(String name, long executionTime, TaskRepository repository) {
        this.name = name;
        this.executionTime = executionTime;
        this.repository = repository;
        this.task = repository.save(new TaskEntity(id,"NOT_STARTED",name,0.0));
    }

    @Override
    public void run() {
        log.info("String Task {} - {}", id,name);
        task.setStatus("STARTED");
        TaskEntity task = repository.save(this.task);
        try {
            Thread.sleep(executionTime);
        } catch (InterruptedException e) {
            log.error(e.toString());
        }
        task.setStatus("COMPLETED");
        task.setScore((Math.random() * (10.0 - 1.0)) + 1.0);
        repository.save(task);
        log.info("Completed Task {} - {}", id,name);
    }
}
