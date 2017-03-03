package com.mcsimonflash.sponge.cmdcalendar.objects;

public class CmdCalTask {

    public enum TaskType {
        Scheduler,
        GameTime,
        Interval,
        GameTick,
        UNKNOWN,
    }

    private String taskName;
    private TaskType taskType;

    private String taskDescription = "";
    private String taskCommand = "";
    private boolean taskStatus = false;


    public CmdCalTask(String taskName, TaskType taskType) {
        this.taskName = taskName;
        this.taskType = taskType;
    }

    public String getName() {
        return taskName;
    }

    public void setName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return taskDescription;
    }

    public void setDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getCommand() {
        return taskCommand;
    }

    public void setCommand(String taskCommand) {
        this.taskCommand = taskCommand;
    }

    public boolean getStatus() {
        return taskStatus;
    }

    public void setStatus(boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskType getType() {
        return taskType;
    }

    public void setType(TaskType taskType) {
        this.taskType = taskType;
    } //SFZ: Implement
}