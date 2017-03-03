package com.mcsimonflash.sponge.cmdcalendar.objects;

public class Interval extends CmdCalTask {

    private int taskInterval = -1;

    public Interval(String taskName, TaskType taskType) {
        super(taskName, taskType);
    }

    public int getInterval() {
        return taskInterval;
    }

    public void setInterval(int taskInterval) {
        this.taskInterval = taskInterval;
    }
}
