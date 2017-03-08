# <div style="text-align: center">CmdCalendar - Automatic Command Scheduler</div>
CmdCalendar is managed and developed by Simon_Flash. For inquiries, please e-mail me at mcsimonflash@gmail.com

Dislaimer: CmdCalendar is a WIP BETA plugin and has not received the same extensive tests as released plugins. While relatively stable, _use this plugin at your own risk!_

## <div style="text-align: center">Important Resources</div>
[Sponge Thread](https://forums.spongepowered.org/t/cmdcalendar-calendar-automatic-command-scheduler-wip-beta/17735)

[GitHub](https://github.com/SimonFlash/CmdCalendar)

## <div style="text-align: center">Scheduler Format</div>
Scheduler tasks are the most powerful type of tasks offered and require the time to match the task's schedule to run. A task's schedule is compared to the server's time and uses the format below. However, CmdCalendar does not check to verify that the schedule provided is a valid date.

* * * * *  
║ ║ ║ ║ ╚═ Second: 0-59  
║ ║ ║ ╚═══ Minute: 0-59  
║ ║ ╚═════ Hour: 0-23  
║ ╚═══════ Day: 1-31  
╚═════════ Month: 1-12  

CmdCalendar schedules allow for both base cases and one modifier.

Number - `##`: Specifies an exact number for this value  
Blank - `*`: Ignores conditions for this value  
Increment - `/##`: Includes all whole-number multiples of ##  

## <div style="text-align: center">Command Documentation</div>
CmdCalendar's documentation is maintained in-game and can be accessed using `/CmdCal` to show available commands, their arguments, and useful invormation. Hovering over a command or argument will show a description, any aliases, the permission node, and any notes. Clicking on the base command will suggest the command to your chat bar.

`/CmdCal`: Opens the in-game command documentation
`/CmdCal CreateTask <Type> <Name>`: Creates a `Type` task named `Name`
`/CmdCal DeleteTask <Name>`: Deletes a task named `Name`
`/CmdCal EditTask <Variable> <Name> <Value>`: Sets the `Variable` attribute of the task named `Name` to `Value`
 * `/CmdCal EditTask SetCommand <Name> <Value>`: Sets the command of the task named `Name` to `Value`
 * `/CmdCal EditTask SetDescription <Name> <Value`: Sets the description of the task named `Name` to `Value`
 * `/CmdCal EditTask SetInterval <Name> <Value>`: Sets the interval of an Interval task named `Name` to `Value`
 * `/CmdCal EditTask SetName <Name> <Value>`: Changes the name of the named `Name` to `Value`
 * `/CmdCal EditTask SetSchedule <Name> <Value>`: Sets the schedule of a Scheduler task named `Name` to `Value`
`/CmdCal ShowTask <Name>`: Shows detailed information about the task named `Name`
`/CmdCal StartTask <Name>`: Activates the task named `Name`
`/CmdCal StopTask <Name>`: Halts the task named `Name`
`/CmdCal TaskList`: Shows basic information about all tasks

In addition to the commands above, CmdCalendar also offers various debug commands should the need arise.

`/CmdCal Debug <Subcommand>`: Base debug command
 * `/CmdCal Debug ListConcealed`: Shows all concealed tasks
 * `/CmdCal Debug LoadConfig`: Loads tasks from the config
 * `/CmdCal Debug SaveConfig`: Saves tasks to the config
 * `/CmdCal Debug StopAll`: Halts all active or suspended tasks

## <div style="text-align: center">Upcoming Features</div>
This section contains a list of planned upcoming features for a future version of CmdCalendar. If you would like to suggest a feature, visit the CmdCalendar Sponge thread (above) or send me an e-mail (also above). Asking for features will be added in order of importance and simplicity, and pushing for a new release will not help your cause. These releases are only planned; there is no guarantee what version they will be implemented in.

+ Command flags
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

## <div style="text-align: center">Issue Reports</div>
This section contains a list of all known bugs and issues in the most recent version of CmdCalendar. Bugs are non-intended errors that exist in the code, while issues are features that do not exist. A feature intended to be implemented into future versions is suffixed [Soon™]. Features will be added in order of importance and simplicity, and pushing for a new release will not help your cause.

+ No issues are currently known! That won't last long...
