package org.mineacademy.orion2.command.orion;

import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;

public class StrikeCommand extends TargettedCommand {

	protected StrikeCommand(SimpleCommandGroup parent) {
		super(parent, "strike|s");

		setDescription("Strike ligting at the target.");
	}

	@Override
	protected void onCommandFor(Player target) {
		target.getWorld().strikeLightning(target.getLocation());
		tell("&bStroke lightning at " + target.getName());

	}
}
