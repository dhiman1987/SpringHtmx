package com.dhiman.spring.htmx.spring.htmx;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Repository
@Slf4j
public class DummyRepository {
    private static final Map<String,MyTask> taskMap =  new HashMap<>();
    public void saveTask(MyTask myTask){  taskMap.put(myTask.getId(),myTask); }
    public List<MyTask> getAllTasks(){ return taskMap.values().stream().toList();}
    public void deleteTasks() { taskMap.clear(); }
}
