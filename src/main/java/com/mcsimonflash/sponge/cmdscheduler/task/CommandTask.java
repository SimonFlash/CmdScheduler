package com.mcsimonflash.sponge.cmdscheduler.task;

import com.mcsimonflash.sponge.cmdscheduler.schedule.Schedule;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.Tristate;

import java.util.List;

public class CommandTask {

    private final String name;
    private List<String> commands;
    private Schedule schedule;
    private Tristate async;
    private ScheduledTask task;

    public CommandTask(String name, List<String> commands, Schedule schedule, Tristate async) {
        this.name = name;
        this.commands = commands;
        this.schedule = schedule;
        this.async = async;
        task = ScheduledTask.builder()
                .name(name)
                .executor(t -> commands.forEach(c -> Sponge.getCommandManager().process(Sponge.getServer().getConsole(), c)))
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
     * @return the commands of the task
     */
    public List<String> getCommands() {
        return commands;
    }

    /**
     * @return the implementing ScheduledTask
     */
    public ScheduledTask getTask() {
        return task;
    }

}