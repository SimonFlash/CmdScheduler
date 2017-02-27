CmdCalendar - Automatic Command Scheduler [WIP Alpha]
Managed by Simon_Flash (mcsimonflash@gmail.com)

Dislaimer:
	-	CmdCalendar is a WIP Alpha plugin from a new Sponge developer and has not received the same
		extensive tests as released plugins. Use this plugin at your own risk!

CmdCalendar Documentation:
	-	CmdCalendar contains an extensive documentation in-game that can be accessed using "/CmdCal".
		Hovering over commands will provide a description of what the command does, the data type
		required, and any aliases of the command. Clicking the command will suggest the base command
		to your chat window. An online documentation is plannned.

Shorthand Notation:
	- Most CmdCalendar commands can be run using shorthand notation, a two character alias of a
		command. For example, "/CmdCal EditTask SetCommand" is equivalent to "/cc et sc". For a full
		list of shorthands, hover over the command in the "/CmdCal" documentation. Note that all
		task names are considered variables and thus do not have a shorthand.

Running Commands:
	-	Commands are run with Sponge.getCommandManager.process() and will always run through Console.
		The command source will be changeable in future versions.
	-	The command may be entered with or without the initial "/"
	-	CmdCalendar commands and server commands (start, stop, reload, op) may not be used. A config
		option for blacklisted commands will be implemented in future versions.
	-	The server verifies that the command exists when creating the Scheduler Task, not the
		CmdCalendar task. This means that a task could be created that does not have a valid command.
	-	When verifying commands, the server checks that Sponge.getCommandManager.process() returns
		CommandResult.success(). Commands that do not return this (all Vanilla commands among others)
		may not be used with CmdCalendar. Other changes will fix this issue, but if someone can suggest
		a better way of verifying a command exists (mainly without running it) I would be grateful.

Upcoming Features:
	-	This section contains a list of planned upcoming features for a future version of CmdCalendar.
		If you would like to suggest a feature, visit the CmdCalendar Sponge thread (above) or send me
		and e-mail (also above). Asking for Features will be added in order of importance and simplicity,
		and pushing for a new release will not help your cause.
	-	These releases are planned; there is no guarantee they will be in specific version labled.
		
		1.0.1-BETA:
			-	Tasks are created without checking commands. *
					* This allows for the use of Vanilla commands and prevents commands from running early.
			- Tasks save to config automatically.
		
		1.0.1-RELEASE:
			-	Tasks can use IRL date/time and in-game time.
			- Command blacklist can be configured.
			
		?.?.?-UNKNOWN:
			-	The time remaining before a task runs can be obtained.
			- Tasks can be force run (with and without altering intervals).
			- Tasks can hold multiple commands.
			- Tasks will only be run if specified conditions are met.
			- Task interval time can be configured.
					* Currently accepts minutes only.
			- All tasks can be suspended (paused) and resumed.
			- Tasks have multiple status cases.

Issue Reports:
	-	This section contains a list of all known bugs and issues in the most recent version of
		CmdCalendar. Bugs are non-intended errors that exist in the code, while issues are features
		that do not exist. A feature intended to be implemented into future versions is suffixed
		[Soon™]. Features will be added in order of importance and simplicity, and pushing for a new
		release will not help your cause.
		
		Bugs:
			-	No bugs known. That won't last long.
		
		Issues:
			-	1.	Vanilla commands may not be used. [Soon™]*
							* See Running Commands
			-	2.	Commands are run when the task is started. [Soon™]*
							* See Running Commands
			-	3.	Commands run when the server is started [Soon™]*
							* See Running Commands
			-	4.	Certain commands may not be run. [Soon™]*
							* See Issue 5; Running Commands
			-	5.	Command may only be run through Console. [Soon™]
			-	6.	CmdCalendar / Server commands (start, stop, reload, op) may not be used. [Soon™]*
							* These commands are default blacklisted. A blacklist option will be available
								in the config in a future version.
			-	7.	Commands can only be run on intervals [Soon™]*
							* CmdCalendar will allow for the use of IRL date/time, in-game time, and intervals
								in the full release.
			-	8.	Tasks do not save to config automatically [Soon™]*
							* Available in the BETA release.
