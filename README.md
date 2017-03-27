# CmdCalendar - Automatic Command Scheduler
CmdCalendar is managed and developed by Simon_Flash. For inquiries, please e-mail me at mcsimonflash@gmail.com

Dislaimer: CmdCalendar is still in the early stages of testing and has not received the rigorous testing as some of my other plugins. *Use at your own discretion!*

## Important Resources
[Sponge Thread](https://forums.spongepowered.org/t/cmdcalendar-calendar-automatic-command-scheduler-wip-beta/17735)  
[GitHub](https://github.com/SimonFlash/CmdCalendar)  
[Support Discord](https://discord.gg/4wayq37)

## Scheduler Format
Scheduler tasks are the most powerful type of tasks offered and require the time to match the task's schedule to run. A task's schedule is compared to the server's time and uses the format below. However, CmdCalendar does not check to verify that the schedule provided is a valid date.

\* \* \* \* \*  
║ ║ ║ ║ ╚═ Month: 1-12  
║ ║ ║ ╚═══ Day: 1-31  
║ ║ ╚═════ Hour: 0-23  
║ ╚═══════ Minute: 0-59  
╚═════════ Second: 0-59  

CmdCalendar schedules currently allow the use of both base cases and one modifier.

Number - `##`: Specifies an exact number for this value  
Blank - `*`: Ignores conditions for this value  
Increment - `/##`: Includes all whole-number multiples of ##  

## Command Documentation
CmdCalendar's documentation is maintained in-game and can be accessed using `/CmdCal` to show available commands, their arguments, and useful invormation. Hovering over a command or argument will show a description, any aliases, the permission node, and any notes. Clicking on the base command will suggest the command to your chat bar.

`/CmdCal`: Opens the in-game command documentation  
`/CmdCal CreateTask <Name> <Type>`: Creates a task named `Name` of type `Type`  
`/CmdCal DeleteTask <Name>`: Deletes a task named `Name`  
`/CmdCal EditTask <Name> <Variable> <Value>`: Changes the `Name` task's `Variable` attribute to `Value`  
`/CmdCal ShowTask <Name>`: Shows detailed information about the task named `Name`  
`/CmdCal StartTask <Name>`: Activates the task named `Name`  
`/CmdCal StopTask <Name>`: Halts the task named `Name`  
`/CmdCal TaskList`: Shows basic information about all tasks

In addition to the commands above, CmdCalendar also offers various debug commands should the need arise.

`/CmdCal Debug ListConcealed confirm`: Shows all concealed tasks  
`/CmdCal Debug LoadConfig confirm`: Loads tasks from the config  
`/CmdCal Debug SaveConfig confirm`: Saves tasks to the config  
`/CmdCal Debug StopAll confirm`: Halts all active or suspended tasks

## Configuration
The Configuration file for CmdCalendar contains various settings to customize your experience with the plugin. Furthermore, tasks may also be created, changed, or deleted by directly adding to the tasks section of the config. However, errors in the config could cause serious issues - make sure that all your edits follow the correct format! A copy of the default config file can be found below for reference.

```
config {
    # Active or Suspended tasks are automatically activated when server starts #
    activate_on_startup=true;
    # Interval tasks wait the delay length before first running #
    interval_delay_start=true
    # Time unit for interval task's interval #
    interval_time_unit="Seconds"
    # Command to be blacklisted (Only supports the base command) #
    task_blacklist=[
        "start",
        "restart",
        "stop",
        "cc",
        "cmdcal",
        "cmdcalendar",
        "commandcalendar"
    ]
    # Use the command blacklist #
    task_blacklist_check=true
}
tasks {
    Name1 {
        command="broadcast &3Go CmdCalendar!'"
        description="Shows how cool CmdCalendar is!"
        interval="180"
        status="Active"
        type="Interval"
    }
    Name2 {
        command="/plainbroadcast &dKittens!"
        description="Summons beings from the dark web"
        schedule="* /5 * * *"
        status="Halted"
        type="Scheduler"
    }
}
```
The configuration can be loaded into the game using on of three methods - using the debug LoadConfig command, reloading Sponge, or restarting the server. To save the current in-game configuration to the config file, use the debug SaveConfig command.

## Upcoming Features
This section contains a list of planned upcoming features for a future version of CmdCalendar. If you would like to suggest a feature, visit the CmdCalendar Sponge thread (above) or send me an e-mail (also above). Asking for features will be added in order of importance and simplicity, and pushing for a new release will not help your cause. These releases are only planned; there is no guarantee what version they will be implemented in.

+ Advanced status
+ Scheduler presets
+ Additional modifiers
+ Conditions on running tasks
+ Multiple commands in tasks
+ Link tasks to run together
+ Task registration
+ Smart registration
+ Smart scheduling
+ Time remaining command
+ Reminder command

## Issue Reports
This section contains a list of all known bugs and issues in the most recent version of CmdCalendar. Bugs are non-intended errors that exist in the code, while issues are features that do not exist. A feature intended to be implemented into future versions is suffixed [Soon™]. Features will be added in order of importance and simplicity, and pushing for a new release will not help your cause.

+ No issues are currently known! That won't last long...
