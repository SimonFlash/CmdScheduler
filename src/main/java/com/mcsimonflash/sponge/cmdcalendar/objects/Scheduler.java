package com.mcsimonflash.sponge.cmdcalendar.objects;

public class Scheduler extends CmdCalTask {

    private String taskSchedule = "";

    public Scheduler(String taskName, TaskType taskType) {
        super(taskName, taskType);
    }

    public String getSchedule() {
        return taskSchedule;
    }

    public void setSchedule(String taskSchedule) {
        this.taskSchedule = taskSchedule;
    }
}