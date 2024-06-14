package com.dhiman.spring.htmx.spring.htmx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskService {
    private final TaskRepository repository;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<TaskRecord> submitTasks(){
        log.debug("Submitting tasks");
        repository.deleteAll();
        List<MyTask> tasks = createTasks();
        for(MyTask task : tasks){
            executor.execute(task);
        }
        log.debug("{} tasks submitted",tasks.size());
        return getAllTasks();
    }

    public List<TaskRecord> getAllTasks(){
        List<TaskRecord> tasks = new ArrayList<>();
       repository.findAll().forEach( t -> tasks.add(
               new TaskRecord(t.getId(),
                       t.getStatus(),
                       t.getName(),
                       t.getScore())
       ));
       return getSortedTasks(tasks);
    }

    public int getCompletionPercentage(List<TaskRecord> tasks){
        long taskCount = tasks.size();
        long taskCompleted = tasks.stream().filter( t -> t.status().equals("COMPLETED")).count();
        int completionPercentage = (int) (((float) taskCompleted / taskCount) * 100);
        log.debug("Completed Task {} of {} = {}%",taskCompleted,taskCount,completionPercentage);
        return completionPercentage;
    }

    public boolean isProcessing(List<TaskRecord> tasks){
        if(tasks.isEmpty()){
            return false;
        }
        return tasks.stream().anyMatch(t -> (t.status().equals("STARTED") || t.status().equals("NOT_STARTED")));
    }

    private List<TaskRecord> getSortedTasks(List<TaskRecord> tasks){
        return tasks.stream()
                .sorted(Comparator.comparingDouble(TaskRecord::score).reversed())
                .collect(Collectors.toList());
    }

    private List<MyTask> createTasks(){
        List<MyTask> tasks = new ArrayList<>();
        tasks.add(new MyTask("Booking.com",2000, repository));
        tasks.add(new MyTask("Yatra",4000, repository));
        tasks.add(new MyTask("MakeMyTrip",5000, repository));
        tasks.add(new MyTask("Trivago",3000, repository));
        tasks.add(new MyTask("Expedia",2000, repository));
        tasks.add(new MyTask("Omio",3000, repository));
        return tasks;
    }
}
