CmdCalendar - Automatic Command Scheduler [WIP Alpha]
Managed by Simon_Flash (mcsimonflash@gmail.com)  

Dislaimer: CmdCalendar is a WIP Alpha plugin from a new Sponge developer and has not received the same extensive tests as released plugins. Use this plugin at your own risk!

CmdCalendar Documentation: CmdCalendar contains an extensive documentation in-game that can be accessed using "/CmdCal". Hovering over commands will provide a description of what the command does, the data type required, and any aliases of the command. Clicking the command will suggest the base command to your chat window. An online documentation is plannned.

Shorthand Notation: Most CmdCalendar commands can be run using shorthand notation, a two character alias of a command. For example, "/CmdCal EditTask SetCommand" is equivalent to "/cc et sc". For a full list of shorthands, hover over the command in the "/CmdCal" documentation. Note that all task names are considered variables and thus do not have a shorthand.

Running Commands: Commands are run with Sponge.getCommandManager.process() and will always run through Console, though this will be changeable in future versions.The command may be entered with or without the initial "/". CmdCalendar blacklists all CmdCalendar commands in addition to start, stop, reload, and op from being used in commands. Until a blacklist config is added, this may be bypassed by setting conf Settings: ignoreBlacklistCheck to "true". CmdCalendar also checks that the user has the permission for the command by default. To disable, set conf Settings: ignorePermissionCheck to "true". Changing this setting is not recommended.

Upcoming Features: This section contains a list of planned upcoming features for a future version of CmdCalendar. If you would like to suggest a feature, visit the CmdCalendar Sponge thread (above) or send me an e-mail (also above). Asking for features will be added in order of importance and simplicity, and pushing for a new release will not help your cause. These releases are only planned; there is no guarantee they will be in specific version labled.
		
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

Issue Reports: This section contains a list of all known bugs and issues in the most recent version of CmdCalendar. Bugs are non-intended errors that exist in the code, while issues are features that do not exist. A feature intended to be implemented into future versions is suffixed [Soon™]. Features will be added in order of importance and simplicity, and pushing for a new release will not help your cause.
		
		Bugs:
			-	No bugs known. That won't last long.
		
		Issues:
			- NL	Commands can only be run on intervals [Soon™]*
							* CmdCalendar will implement Interval, DateTime, and GameTick tasks.
			-	NL.	Command may only be run through Console. [Soon™]
							* Source changes will be supported before the full release.
			-	NL.	Tasks do not save to config automatically [Soon™]*
							* Available in the BETA release.
