package org.mineacademy.orion2.rank;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.mineacademy.orion2.PlayerCache;
import org.mineacademy.orion2.boss.model.Boss;
import org.mineacademy.orion2.boss.model.BossWarrior;

public class RankListener implements Listener {

	@EventHandler
	public void onDeath(final EntityDeathEvent event) {
		final LivingEntity diedEntity = event.getEntity();
		final Player killer = diedEntity.getKiller();

		if (killer != null) {
			final Boss boss = Boss.findBoss(diedEntity);

			if (boss != null) {
				final PlayerCache cache = PlayerCache.getCache(killer);

				// Force rankup when the player kills the Warrior boss
				if (boss instanceof BossWarrior/* && cache.getRank() == Rank.HERO*/) // Edit: Uncomment if you only want to rankup when the current rank is Hero
					cache.getRank().upgradeToNextRank(killer, true);
			}
		}
	}

	// Uncomment below if you don't have any chat plugin and want to format
	// your chat here
	/*@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onChat(final AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();
		final PlayerCache cache = PlayerCache.getCache(player);

		final Rank rank = cache.getRank();

		event.setFormat(Common.colorize("&8[" + rank.getColor() + rank.getName() + "&8] &f" + player.getName() + "&7: ") + event.getMessage());
	}*/
}
