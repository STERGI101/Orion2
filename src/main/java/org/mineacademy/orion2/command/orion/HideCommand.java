package org.mineacademy.orion2.command.orion;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.orion2.OrionPlugin;

import java.util.ArrayList;
import java.util.List;

public class HideCommand extends TargettedCommand {

	protected HideCommand(SimpleCommandGroup parent) {
		super(parent, "hide|h");


		setDescription("Hide the target player from you.");
	}

	@Override
	protected void onCommandFor(Player target) {
		checkConsole();


		checkBoolean(target.getName().equals(getPlayer().getName()),"You cannot hide from yourself :-)");


		if(target.canSee(getPlayer())){

			target.hidePlayer(OrionPlugin.getInstance(), getPlayer());
			tell("&aPlayer " + target.getName() + " can no longer see you.");

		}else{
			target.showPlayer(OrionPlugin.getInstance(), getPlayer());
			tell("&aPlayer " + target.getName() + " can no longer see you.");
		}


		}
	@Override
	protected List<String> tabComplete(){

	if(isPlayer()) {
		switch (args.length) {
			case 1:
				return completeLastWord(EntityType.values());
			case 2:
				return completeLastWord(getPlayer().getLocation().getBlockX());
			case 3:
				return completeLastWord(getPlayer().getLocation().getBlockY());
			case 4:
				return completeLastWord(getPlayer().getLocation().getBlockZ());
		}
	}

		return new ArrayList<>();

	}
}
