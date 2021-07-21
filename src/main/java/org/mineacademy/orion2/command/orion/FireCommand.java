package org.mineacademy.orion2.command.orion;

import org.bukkit.entity.Player;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.command.SimpleCommandGroup;

public class FireCommand extends TargettedCommand {

	protected FireCommand(SimpleCommandGroup parent) {
		super(parent, "fire|f");

		setPermission("orion.fire");

		setDescription("Set target on fire.");
	}

	@Override
	protected void onCommandFor(Player target) {

		checkBoolean(!PlayerUtil.hasPerm(target,"dont.put.me.on.fire"), "You cannot put " + target.getName() + " on fire!");

		target.setFireTicks(20*4);
		tell("&cSet " + target.getName() + " on fire for four seconds.");


	}
}
