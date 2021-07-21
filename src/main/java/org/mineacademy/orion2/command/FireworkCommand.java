package org.mineacademy.orion2.command;

import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

public class FireworkCommand extends SimpleCommand {
	public FireworkCommand() {
		super("firework"); // /firework --> run this command
	}

	@Override
	protected void onCommand() {
		//Available fields:
		// sender
		// args
		checkConsole();

		Player player = getPlayer();

		player.getWorld().spawn(player.getLocation(), Firework.class);

		String oldPrefix = Common.getTellPrefix();
		Common.setTellPrefix("&8[&dFirework&8] &7");

		tell("&cLol this rocket seems like is not going !");

		Common.setTellPrefix(oldPrefix);

		Common.log("Hello from firework");

	}

	public void test(){

	}
}
