package com.mcsimonflash.sponge.cmdscheduler.task;

import com.google.common.base.Preconditions;
import com.mcsimonflash.sponge.cmdscheduler.event.TaskExecutionEvent;
import com.mcsimonflash.sponge.cmdscheduler.schedule.ClassicSchedule;
import com.mcsimonflash.sponge.cmdscheduler.schedule.CronSchedule;
import com.mcsimonflash.sponge.cmdscheduler.schedule.Schedule;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.ResettableBuilder;
import org.spongepowered.api.util.Tristate;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class AdvancedTask {

    //TODO: Should we keep a registration of tasks?
    //TODO: Find a way for CronSchedule to run without starting/stopping
    //TODO: Add a 'lock' setting that prevents this task from being started
    //TODO: Add a 'singleton' setting that only allows this task to be run once.
    //TODO: Differentiate between active and running tasks
    //Active tasks have been started, but may have been filtered out
    //Running tasks are submitted and have a Task object

    private final String name;
    private final Consumer<AdvancedTask> executor;
    private final Schedule schedule;
    private final Tristate async;
    private Task task;

    private AdvancedTask(String name, Consumer<AdvancedTask> executor, Schedule schedule, Tristate async) {
        this.name = name;
        this.executor = executor;
        this.schedule = schedule;
        this.async = async;
    }

    //TODO: Avoid running tasks that are unused in the next... 24 hours?
    /**
     * Starts the task and handles any changes to support Sponge's
     * {@link Task} class.
     *
     * If the task is already started, this call will be ignored.
     *
     * @param container the plugin container starting this task
     */
    public void start(PluginContainer container) {
        if (task == null) {
            Task.Builder builder = Task.builder();
            long delay = schedule.getDelay();
            if (delay < 0) {
                //TODO: Reschedule
                return;
            } else if (schedule instanceof ClassicSchedule && ((ClassicSchedule) schedule).isDelayTicks()) {
                builder.delayTicks(delay);
            } else {
                builder.delay(delay, TimeUnit.MILLISECONDS);
            }
            long interval = schedule.getInterval();
            if (interval > 0) {
                if (schedule instanceof ClassicSchedule && ((ClassicSchedule) schedule).isIntervalTicks()) {
                    builder.intervalTicks(interval);
                } else {
                    builder.interval(interval, TimeUnit.MILLISECONDS);
                }
            }
            Consumer<Task> consumer = st -> execute(container);
            switch (async) {
                case UNDEFINED:
                    consumer = st -> Task.builder().name("Magnetar Synchronous Wrapper: " + name).execute(t -> execute(container)).submit(container);
                case TRUE: //Fallthrough
                    builder.async();
            }
            if (schedule instanceof CronSchedule) {
                Consumer<Task> copy = consumer;
                consumer = st -> {
                    copy.accept(st);
                    if (task != null) {
                        task.cancel();
                        task = null;
                        this.start(container);
                    }
                };
            }
            task = builder.name(name).execute(consumer).submit(container);
        }
    }

    /**
     * Stops the task. If the task is not running, this call will be ignored.
     *
     * @param container the plugin container stopping this task
     */
    public void stop(PluginContainer container) {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    /**
     * Execute the consumer of this task.
     *
     * @param container the plugin container that forced this task
     */
    public void execute(PluginContainer container) {
        try {
            //TODO: Benefit behind throwing an event?
            //TODO: Allow the event to be cancelable?
            Sponge.getEventManager().post(new TaskExecutionEvent(this, Cause.of(EventContext.empty(), container)));
            executor.accept(this);
            //TODO: Better method for handling exceptions
        } catch (Throwable t) {
            container.getLogger().error("An error occurred attempting to execute an AdvancedTask for this plugin.", t);
        }
    }

    /**
     * @return the task name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the task executor
     */
    public Consumer<AdvancedTask> getExecutor() {
        return executor;
    }

    /**
     * @return the task schedule
     */
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * @return the task async state
     */
    public Tristate getAsync() {
        return async;
    }

    //TODO: Implement (see above)
    /**
     * @return if the task is active (currently the same as isRunning)
     */
    public boolean isActive() {
        return isRunning();
    }

    /**
     * @return if the task is running
     */
    public boolean isRunning() {
        return task != null;
    }

    @Override
    public String toString() {
        return "MagnetarTask{name=" + name + ",schedule=" + schedule + ",async=" + async.toString().toLowerCase() + "}";
    }

    /**
     * Creates a new builder for a {@link AdvancedTask}.
     *
     * @return the new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements ResettableBuilder<AdvancedTask, Builder> {

        @Nullable private String name;
        @Nullable private Consumer<AdvancedTask> executor;
        @Nullable private Schedule schedule;
        private Tristate async = Tristate.UNDEFINED;

        //TODO: Include a from(Task) method?
        /**
         * Sets this builder to have the same executor, schedule, and async
         * state as the give {@link AdvancedTask value}. The name must still be set.
         *
         * @param value the existing MyTask
         * @return this builder
         */
        @Override
        public Builder from(AdvancedTask value) {
            executor = value.getExecutor();
            schedule = value.getSchedule();
            async = value.getAsync();
            return this;
        }

        /**
         * Sets the name, executor, and schedule to be undefined. The async
         * state is reverted to {@link Tristate#UNDEFINED}.
         *
         * @return this builder
         */
        public Builder reset() {
            this.name = null;
            this.executor = null;
            this.schedule = null;
            this.async = Tristate.UNDEFINED;
            return this;
        }

        //TODO: Check for duplicate name
        /**
         * Sets the name of this task. The task name must be defined for every
         * task, including those copied using {@link ResettableBuilder#from}.
         *
         * @param name the name to be set
         * @return this builder for chaining
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the executor for this task, which consumes the resulting
         * {@link AdvancedTask}.
         *
         * @param executor the executor to be set
         * @return this builder for chaining
         */
        public Builder executor(Consumer<AdvancedTask> executor) {
            this.executor = executor;
            return this;
        }

        //TODO: Include source argument (note: requires an online check or something)
        //TODO: Check if the task is async?
        /**
         * Sets the executor for this task to process the given command as the
         * server console.
         *
         * If the command starts with a "/", it will be replaced.
         *
         * @param command the command to be processed
         */
        public Builder command(String command) {
            String copy = command.startsWith("/") ? command.substring(1) : command;
            return executor(mt -> Sponge.getCommandManager().process(Sponge.getServer().getConsole(), copy));
        }

        //TODO: If no schedule is given, should this task run once and stop?
        /**
         * Sets the schedule used for this task.
         *
         * @param schedule the schedule to be set
         * @return this builder for chaining
         */
        public Builder schedule(Schedule schedule) {
            this.schedule = schedule;
            return this;
        }

        /**
         * Sets the async state using a {@link Tristate}. By default, the state
         * is {@link Tristate#UNDEFINED}
         *
         * TRUE: The task is scheduled asynchronously
         * FALSE: The task is scheduled synchronously
         * UNDEFINED: The task is schedule asynchronously, but the executor is
         * wrapped inside a synchronous task. This ensure that the task runs in
         * sync with the server, but increases performance by handling the
         * logistics in another thread.
         *
         * @param async the async state to be set
         * @return this builder for chaining
         */
        public Builder async(Tristate async) {
            this.async = async;
            return this;
        }

        //TODO: What if the task is async and has a non-async schedule/executor?
        /**
         * Builds and returns the task that correlates to this builder.
         *
         * Unlike the Sponge Task, building does not submit the task. It must
         * be started independently using {@link #start(PluginContainer)}.
         *
         * @return the MyTask for this builder
         * @throws IllegalArgumentException if name, executor, or schedule are
         *     not defined or name is empty
         */
        public AdvancedTask build() throws IllegalArgumentException {
            Preconditions.checkArgument(name != null && !name.isEmpty(), "Name must be defined.");
            Preconditions.checkArgument(executor != null, "Executor must be defined.");
            Preconditions.checkArgument(schedule != null, "Schedule must be defined.");
            return new AdvancedTask(name, executor, schedule, async);
        }

    }

}