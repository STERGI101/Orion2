package org.mineacademy.orion2.command.orion;

import org.mineacademy.fo.command.SimpleCommandGroup;

public class OrionCommandGroup extends SimpleCommandGroup {

	@Override
	protected void registerSubcommands() {
		registerSubcommand(new StrikeCommand(this));
		registerSubcommand(new HideCommand(this));
		registerSubcommand(new FireCommand(this));
		registerSubcommand(new ReloadCommand()); // Uses the OrionPlugin#getMainCommand as the parent, which is this
	}

	@Override
	protected String getCredits() {
		return "Visit yourname.com for more information.";
	}
}
