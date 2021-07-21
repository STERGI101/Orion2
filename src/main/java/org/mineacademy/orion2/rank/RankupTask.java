package org.mineacademy.orion2.rank;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.orion2.PlayerCache;

public class RankupTask extends BukkitRunnable {

	@Override
	public void run() {
		for (final Player player : Remain.getOnlinePlayers()) {
			final PlayerCache cache = PlayerCache.getCache(player);

			// Every time this task run, check if the player can rankup
			// and upgrade their rank automatically
			cache.getRank().upgradeToNextRank(player);
		}
	}
}
