package org.mineacademy.orion2.event;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.TimeUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.orion2.PlayerCache;
import org.mineacademy.orion2.settings.Settings;

public class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChatFirst(final AsyncPlayerChatEvent event) {
		//Common.log("Lowest priority - " + event.getPlayer().getName() + ": " + event.getMessage());
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		//Common.log("Normal priority (cancelling) - " + event.getPlayer().getName() + ": " + event.getMessage());

		//event.setCancelled(true);

		// TODO Must be wrapped into a sync runnable to make this command run from the main thread
		//if (event.getMessage().equalsIgnoreCase("hello"))
		//	Bukkit.dispatchCommand(event.getPlayer(), "firework");
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerChatLate(final AsyncPlayerChatEvent event) {
		//Common.log("High priority (cancelled=" + event.isCancelled() + ") - " + event.getPlayer().getName() + ": " + event.getMessage());

		//event.setCancelled(false);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChatLast(final AsyncPlayerChatEvent event) {
		//Common.log("Monitor priority (cancelled=" + event.isCancelled() + ") - " + event.getPlayer().getName() + ": " + event.getMessage());
	}

	// ----------------------------------------------------------

	@EventHandler
	public void onPlayerCommand(final PlayerCommandPreprocessEvent event) {
		//Common.log("Message: " + event.getMessage());

		// TODO Not a good way to stop /plugins, give players negative plugin permission instead
		/*if (event.getMessage().equalsIgnoreCase("/plugins")) {
			Common.tell(event.getPlayer(), "You cannot view plugins, well you can but differently...");

			event.setCancelled(true);
		}*/

		final Player player = event.getPlayer();
		final String message = event.getMessage();
		final String commandLabel = message.substring(1).split(" ")[0];

		if (!Valid.isInListStartsWith(commandLabel /* UPDATE: Changed to commandLabel, but both will work just fine */, Settings.IGNORED_LOG_COMMANDS))
			FileUtil.write("commands.log", "[" + TimeUtil.getFormattedDate() + "] " + player.getName() + ": " + Common.stripColors(message));
	}

	// ----------------------------------------------------------

	@EventHandler
	public void onPlayerChat2(final AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();
		final PlayerCache cache = PlayerCache.getCache(player);
		final ChatColor color = cache.getColor();

		if (color != null)
			event.setMessage(color + event.getMessage());

		FileUtil.write("logs/" + player.getName() + ".log", "[" + TimeUtil.getFormattedDate() + "] " + player.getName() + ": " + Common.stripColors(event.getMessage()));
		//FileUtil.write("common-chat.log", "[" + TimeUtil.getFormattedDate() + "] " + player.getName() + ": " + Common.stripColors(event.getMessage()));

	}

	// ----------------------------------------------------------

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		final PlayerCache cache = PlayerCache.getCache(player);

		for (final PlayerCache.ClassCache classCache : cache.getClasses())
			classCache.getClassBase().applyFor(classCache.getTier(), player);
	}
}

