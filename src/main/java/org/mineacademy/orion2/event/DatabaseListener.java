package org.mineacademy.orion2.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.orion2.PlayerCache;
import org.mineacademy.orion2.mysql.OrionDatabase;

import java.util.UUID;

public class DatabaseListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onLogin(final AsyncPlayerPreLoginEvent event) {
		final UUID uuid = event.getUniqueId();

		if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
			final PlayerCache cache = PlayerCache.getCache(uuid);

			OrionDatabase.getInstance().load(uuid, cache);
		}
	}

	@EventHandler
	public void onQuit(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		final PlayerCache cache = PlayerCache.getCache(player);

		Common.runLaterAsync(() -> OrionDatabase.getInstance().save(player.getName(), player.getUniqueId(), cache));
	}
}

