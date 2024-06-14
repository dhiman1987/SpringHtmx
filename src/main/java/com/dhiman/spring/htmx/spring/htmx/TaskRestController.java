package com.dhiman.spring.htmx.spring.htmx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
@Slf4j
public class TaskRestController {
    private final TaskService taskService;
    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }
    @PostMapping("/submit")
    public TaskStatusRecord submitTasks(){
        return new TaskStatusRecord(taskService.submitTasks(), true, 0);
    }

    @GetMapping("/status")
    public TaskStatusRecord getTaskStatus(){
        List<TaskRecord> tasks = taskService.getAllTasks();
        return new TaskStatusRecord(
                tasks,
                taskService.isProcessing(tasks),
                taskService.getCompletionPercentage(tasks));
    }

}
