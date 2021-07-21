package org.mineacademy.orion2.quest;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.orion2.PlayerCache;

/*
/**
 * A runnable task that shows action bar to players
 * as well as ticks their quest progression (override {@link org.mineacademy.orion.quest.model.Quest#onTick)
 */
public class QuestTask extends BukkitRunnable {

	@Override
	public void run() {
		for (final Player player : Remain.getOnlinePlayers()) {
			final PlayerCache cache = PlayerCache.getCache(player);
			final PlayerCache.ActiveQuestCache quest = cache.getActiveQuest();

			if (quest != null) {
				// Call the tick method automatically,
				// see QuestComeHome for example use
				quest.getQuest().onTick(cache, player);

				// If the tick method did not cancel or complete the quest
				// Show progression bar
				if (cache.getActiveQuest() != null)
					Remain.sendActionBar(player, "&8[&e%&8] &f" + quest.getCompletion(player) + " &8[&e%&8]");
			}
		}
	}
}
