package org.mineacademy.orion2.stats;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.mineacademy.fo.model.BoxedMessage;
import org.mineacademy.orion2.DataFile;
import org.mineacademy.orion2.DataFile.PlayerStatisticData;
import org.mineacademy.orion2.PlayerCache;

public class StatsListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onDeath(final EntityDeathEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;

		final Player victim = (Player) event.getEntity();
		final Player killer = victim.getKiller();

		if (victim.getGameMode() == GameMode.SURVIVAL) {
			if (killer != null) {
				final PlayerCache killerCache = PlayerCache.getCache(killer);

				final PlayerStatisticData topKiller = DataFile.getInstance().getTopStatistic(PlayerCache::getPlayerKills);
				final String topKillerName = topKiller.getPlayer().getName();

				if (topKiller.getValue() == killerCache.getPlayerKills()) {
					if (!topKillerName.equals(killer.getName()))
						BoxedMessage.tell(killer, "&6You are now the top killer in the list!");

				} else if (topKillerName.equals(victim.getName()))
					BoxedMessage.tell(killer, "&6Congratulations for taking down the &ctop killer&6!");

				PlayerCache.getCache(killer).increasePlayerKills();
			}

			PlayerCache.getCache(victim).increaseDeaths();
		}
	}
}