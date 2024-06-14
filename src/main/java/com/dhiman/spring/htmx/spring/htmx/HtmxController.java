package com.dhiman.spring.htmx.spring.htmx;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/htmx")
@Slf4j
@AllArgsConstructor
public class HtmxController {

    private final TaskService taskService;

    @GetMapping("/home")
    public ModelAndView home(){
        log.debug("Loading htmx home page");
        ModelAndView mav =  new ModelAndView("htmx");
        List<TaskRecord> tasks = taskService.getAllTasks();
        mav.addObject("tasks",tasks);
        mav.addObject("isProcessing",taskService.isProcessing(tasks));
        return mav;
    }

    @PostMapping("/submit-task")
    public ModelAndView submitTask(){
        log.debug("Submitting tasks");
        ModelAndView mav =  new ModelAndView("htmx :: taskDetails");
        mav.addObject("tasks",taskService.submitTasks());
        mav.addObject("isProcessing",true);
        return mav;
    }

    @GetMapping("/check-status")
    public ModelAndView checkStatus(){
        log.debug("Fetching task status");
        ModelAndView mav =  new ModelAndView("htmx :: taskDetails");
        List<TaskRecord> tasks = taskService.getAllTasks();
        log.debug("Tasks {}",tasks);
        mav.addObject("tasks",tasks);
        mav.addObject("completionPercentage",taskService.getCompletionPercentage(tasks));
        mav.addObject("isProcessing",taskService.isProcessing(tasks));
        return mav;
    }

}
