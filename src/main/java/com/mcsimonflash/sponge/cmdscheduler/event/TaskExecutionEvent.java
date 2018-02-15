package com.mcsimonflash.sponge.cmdscheduler.event;

import com.mcsimonflash.sponge.cmdscheduler.task.AdvancedTask;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;

public class TaskExecutionEvent extends AbstractEvent {

    //TODO: What the heck does this class even do?

    private final AdvancedTask task;
    private final Cause cause;

    /**
     * Creates a new execution event for the given task and cause. The cause is
     * implemented by {@code Cause.source(PluginContainer container).build()}
     * and contains the container of the plugin that started or forced the task.
     *
     * @param task the task being executed
     * @param cause the cause of the execution
     */
    public TaskExecutionEvent(AdvancedTask task, Cause cause) {
        this.task = task;
        this.cause = cause;
    }

    /**
     * Returns the {@link AdvancedTask} that is being executed. Attempting to
     * start, stop, or force the task results in undefined behavior.
     *
     * @return the task being execution
     */
    public AdvancedTask getTask() {
        return task;
    }

    /**
     * Returns the {@link Cause} of the execution. This is implemented by
     * {@code Cause.source(PluginContainer container).build()} and contains the
     * container of the plugin that started or forced the task.
     *
     * @return the cause of the execution
     */
    @Override
    public Cause getCause() {
        return cause;
    }

}