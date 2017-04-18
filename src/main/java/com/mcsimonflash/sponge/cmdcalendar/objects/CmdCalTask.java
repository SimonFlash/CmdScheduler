package com.mcsimonflash.sponge.cmdcalendar.objects;

public class CmdCalTask {

    public CmdCalTask(String name, TaskType type) {
        Name = name;
        Type = type;
    }

    public String Name;
    public TaskType Type;
    public String Description = "";
    public String Command = "";
    public TaskStatus Status = TaskStatus.ERROR;

    public enum TaskType {
        GameTick,
        GameTime,
        Interval,
        Scheduler,
        ERROR,
    }

    public enum TaskStatus {
        Active,
        Concealed_Active,
        Concealed_Halted,
        Halted,
        Suspended,
        ERROR,
    }
}