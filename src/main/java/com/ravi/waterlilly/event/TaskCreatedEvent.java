package com.ravi.waterlilly.event;

import com.ravi.waterlilly.model.Task;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

// Event for task creation
@Getter
public class TaskCreatedEvent extends ApplicationEvent {
    private final Task task;
    private final String targetType;
    private final String targetName;

    public TaskCreatedEvent(Object source, Task task, String targetType, String targetName) {
        super(source);
        this.task = task;
        this.targetType = targetType;
        this.targetName = targetName;
    }
}