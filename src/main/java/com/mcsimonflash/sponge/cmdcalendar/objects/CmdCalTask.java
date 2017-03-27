package com.mcsimonflash.sponge.cmdcalendar.objects;

public class CmdCalTask {

    public enum TaskType {
        Scheduler,
        GameTime,
        Interval,
        GameTick,
        ERROR,
    }

    public enum TaskStatus {
        Active,
        Suspended,
        Halted,
        Concealed,
        ERROR,
    }

    private String taskName;
    private TaskType taskType;
    private String taskDescription = "";
    private String taskCommand = "";
    private TaskStatus taskStatus = TaskStatus.ERROR;


    public CmdCalTask(String taskName, TaskType taskType) {
        this.taskName = taskName;
        this.taskType = taskType;
    }

    public static TaskStatus parseStatus(String taskStatus) {
        switch (taskStatus.toLowerCase()) {
            case "active":
                return TaskStatus.Active;
            case "suspended":
                return TaskStatus.Suspended;
            case "halted":
                return TaskStatus.Halted;
            case "concealed":
                return TaskStatus.Concealed;
            default:
                return TaskStatus.ERROR;
        }
    }

    public static String printStatus(TaskStatus taskStatus) {
        switch (taskStatus) {
            case Active:
                return "Active";
            case Concealed:
                return "Concealed";
            case Halted:
                return "Halted";
            case Suspended:
                return "Suspended";
            default:
                return "Error";
        }
    }

    public static TaskType parseType(String taskType) {
        switch (taskType.toLowerCase()) {
            case "scheduler":
            case "sched":
            case "s":
                return TaskType.Scheduler;
            case "interval":
            case "inter":
            case "i":
                return TaskType.Interval;
            default:
                return TaskType.ERROR;
        }
    }

    public static String printType(TaskType taskType) {
        switch (taskType) {
            case GameTick:
                return "GameTick";
            case GameTime:
                return "GameTime";
            case Interval:
                return "Interval";
            case Scheduler:
                return "Scheduler";
            default:
                return "Error";
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
}