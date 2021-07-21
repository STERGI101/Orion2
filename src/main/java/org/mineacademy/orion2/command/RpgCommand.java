package org.mineacademy.orion2.command;

import org.mineacademy.fo.command.SimpleCommand;

public class RpgCommand extends SimpleCommand {
	public RpgCommand() {
		super("rpg");

		setMinArguments(1);
		setUsage("<new>");
	}

	@Override
	protected void onCommand() {
		String param = args[0].toLowerCase();



	}

}

