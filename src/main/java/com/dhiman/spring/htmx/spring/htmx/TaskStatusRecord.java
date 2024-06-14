package com.dhiman.spring.htmx.spring.htmx;

import java.util.List;

public record TaskStatusRecord(
       List<TaskRecord> tasks,
       boolean isProcessing,
       int completionPercentage){
}
