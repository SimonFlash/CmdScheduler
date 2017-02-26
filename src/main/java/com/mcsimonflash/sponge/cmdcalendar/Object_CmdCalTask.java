package com.mcsimonflash.sponge.cmdcalendar;

public class Object_CmdCalTask {
    private String taskName;
    private String taskDescription;
    private String taskCommand;
    private int taskInterval;
    private boolean taskStatus;

    public Object_CmdCalTask(String taskName, int taskInterval, String taskCommand) {
        this.taskName = taskName;
        this.taskInterval = taskInterval;
        this.taskCommand = taskCommand;
        this.taskDescription = "";
        this.taskStatus = false;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskCommand() {
        return taskCommand;
    }

    public void setTaskCommand(String taskCommand) {
        this.taskCommand = taskCommand;
    }

    public int getTaskInterval() {
        return taskInterval;
    }

    public void setTaskInterval(int taskInterval) {
        this.taskInterval = taskInterval;
    }

    public boolean getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void toggleTask() {
        this.taskStatus = !this.taskStatus;
    }
}
