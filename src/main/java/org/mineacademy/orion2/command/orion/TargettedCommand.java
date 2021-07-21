package org.mineacademy.orion2.command.orion;

import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public abstract class TargettedCommand extends SimpleSubCommand {
	protected TargettedCommand(SimpleCommandGroup parent, String sublabel) {
		super(parent, sublabel);
		setMinArguments(1);
		setUsage("<target>");
	}

	@Override
	protected void onCommand() {
		Player target = findPlayer(args[0]);

		onCommandFor(target);


	}

	protected abstract void onCommandFor(Player target);




}
