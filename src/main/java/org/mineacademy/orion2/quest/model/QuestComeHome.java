package org.mineacademy.orion2.quest.model;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.orion2.PlayerCache;
import org.mineacademy.orion2.classes.model.ClassBase;

public class QuestComeHome extends Quest {

	private static Location HOME_LOCATION;

	protected QuestComeHome() {
		super("Come Home");

		HOME_LOCATION = new Location(Bukkit.getWorld("world"), 77, 9, -52);
	}

	@Override
	public Quest getNext(final ClassBase classBase) {
		return null;
	}

	@Override
	public CompMaterial getIcon() {
		return CompMaterial.BIRCH_SAPLING;
	}

	@Override
	public String[] getMenuLore() {
		return new String[] {
				"Reach your home",
				"location."
		};
	}

	@Override
	public String getCompletion(final Player player, final Object completionData) {
		return "Distance from home: " + Math.round(player.getLocation().distance(HOME_LOCATION)) + "m";
	}

	@Override
	public void onTick(final PlayerCache cache, final Player player) {
		if (player.getLocation().distance(HOME_LOCATION) < 2) {
			cache.completeQuest();

			Common.tell(player, "&2Congratulations for completing this quest!");
			CompSound.FIREWORK_LARGE_BLAST2.play(player);
		}
	}
}

