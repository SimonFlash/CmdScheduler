package com.mcsimonflash.sponge.cmdcalendar.objects;

public class CmdCalTask {
    private String taskName;
    private String taskDescription;
    private String taskCommand;
    private int taskInterval;
    private boolean taskStatus;

    public CmdCalTask(String taskName, int taskInterval, String taskCommand) {
        this.taskName = taskName;
        this.taskInterval = taskInterval;
        this.taskCommand = taskCommand;
        this.taskDescription = "";
        this.taskStatus = false;
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

    public int getInterval() {
        return taskInterval;
    }

    public void setInterval(int taskInterval) {
        this.taskInterval = taskInterval;
    }

    public boolean getStatus() {
        return taskStatus;
    }

    public void setStatus(boolean taskStatus) {
        this.taskStatus = taskStatus;
    }
}
