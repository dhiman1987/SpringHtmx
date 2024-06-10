package com.dhiman.spring.htmx.spring.htmx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/htmx")
@Slf4j
public class HtmxController {

    private final DummyRepository repository;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public HtmxController(DummyRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/home")
    public ModelAndView home(){
        log.debug("Loading htmx home page");
        ModelAndView mav =  new ModelAndView("htmx");
        List<MyTask> tasks = repository.getAllTasks();
        mav.addObject("tasks",getSortedTasks(tasks));
        mav.addObject("isProcessing",isProcessing(tasks));
        return mav;
    }

    @PostMapping("/sumbit-task")
    public ModelAndView submitTask(){
        log.debug("Submitting tasks");
        ModelAndView mav =  new ModelAndView("htmx :: taskDetails");
        repository.deleteTasks();
        List<MyTask> tasks = createTasks();
        for(MyTask task : tasks){
            executor.execute(task);
        }
        log.debug("{} tasks submitted",tasks.size());
        mav.addObject("tasks",getSortedTasks(tasks));
        mav.addObject("isProcessing",true);
        return mav;
    }

    @GetMapping("/check-status")
    public ModelAndView checkStatus(){
        log.debug("Fetching task status");
        ModelAndView mav =  new ModelAndView("htmx :: taskDetails");
        List<MyTask> tasks = repository.getAllTasks();
        mav.addObject("tasks",getSortedTasks(tasks));
        mav.addObject("completionPercentage",getCompletionPercentage(tasks));
        mav.addObject("isProcessing",isProcessing(tasks));
        return mav;
    }

    private int getCompletionPercentage(List<MyTask> tasks){
        long taskCount = tasks.size();
        long taskCompleted = tasks.stream().filter( t -> t.getStatus().equals("COMPLETED")).count();
        int completionPercentage = (int) (((float) taskCompleted / taskCount) * 100);
        log.debug("Completed Task {} of {} = {}%",taskCompleted,taskCount,completionPercentage);
        return completionPercentage;
    }

    private boolean isProcessing(List<MyTask> tasks){
        if(tasks.isEmpty()){
            return false;
        }
        return tasks.stream().anyMatch(t -> t.getStatus().equals("STARTED"));
    }

    private List<MyTask> getSortedTasks(List<MyTask> tasks){
        return tasks.stream()
                .sorted(Comparator.comparingDouble(MyTask::getScore).reversed())
                .collect(Collectors.toList());
    }

    private List<MyTask> createTasks(){
        repository.saveTask(new MyTask("Checking availability",2000, repository));
        repository.saveTask(new MyTask("Finding seller",3000, repository));
        repository.saveTask(new MyTask("Processing order",5000, repository));
        repository.saveTask(new MyTask("Packaging order",1000, repository));
        repository.saveTask(new MyTask("Dispatching item",2000, repository));
        return repository.getAllTasks();
    }
}
