package com.mcsimonflash.sponge.cmdcalendar.objects;

public class CmdCalTask {

    public enum TaskType {
        Scheduler,
        GameTime,
        Interval,
        GameTick,
        UNKNOWN,
    }

    public enum TaskStatus {
        Active,
        Suspended,
        Halted,
        Concealed,
    }

    private String taskName;
    private TaskType taskType;

    private String taskDescription = "";
    private String taskCommand = "";
    private TaskStatus taskStatus = TaskStatus.Halted;


    public CmdCalTask(String taskName, TaskType taskType) {
        this.taskName = taskName;
        this.taskType = taskType;
    }

    public static TaskStatus parseStatus (String taskStatus) {
        switch (taskStatus.toLowerCase()) {
            case "active":
                return TaskStatus.Active;
            case "suspended":
                return TaskStatus.Suspended;
            case "halted":
                return TaskStatus.Halted;
            default:
                return TaskStatus.Concealed;
        }
    }

    public static TaskType parseType(String taskType) {
        switch (taskType.toLowerCase()) {
            case "scheduler":
            case "sched":
                return TaskType.Scheduler;
            case "interval":
            case "inter":
                return TaskType.Interval;
            default:
                return TaskType.UNKNOWN;
        }
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

    public TaskStatus getStatus() {
        return taskStatus;
    }

    public void setStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskType getType() {
        return taskType;
    }

    public void setType(TaskType taskType) {
        this.taskType = taskType;
    } //SFZ: Implement
}