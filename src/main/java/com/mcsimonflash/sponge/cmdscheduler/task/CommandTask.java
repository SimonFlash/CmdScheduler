package com.mcsimonflash.sponge.cmdscheduler.task;

import com.mcsimonflash.sponge.cmdscheduler.schedule.Schedule;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.Tristate;

public class CommandTask {

    private final String name;
    private String command;
    private Schedule schedule;
    private Tristate async;
    private ScheduledTask task;

    public CommandTask(String name, String command, Schedule schedule, Tristate async) {
        this.name = name;
        this.command = command;
        this.schedule = schedule;
        this.async = async;
        task = ScheduledTask.builder()
                .name(name)
                .executor(t -> Sponge.getCommandManager().process(Sponge.getServer().getConsole(), command))
                .schedule(schedule)
                .async(async)
                .build();
    }

    /**
     * @return the name of the task
     */
    public String getName() {
        return name;
    }

    /**
     * @return the command of the task
     */
    public String getCommand() {
        return command;
    }

    /**
     * @return the implementing ScheduledTask
     */
    public ScheduledTask getTask() {
        return task;
    }

}