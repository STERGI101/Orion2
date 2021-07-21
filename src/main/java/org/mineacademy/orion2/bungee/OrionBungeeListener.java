package org.mineacademy.orion2.bungee;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.mineacademy.fo.BungeeUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.bungee.BungeeListener;
import org.mineacademy.fo.bungee.message.IncomingMessage;

public class OrionBungeeListener extends BungeeListener {

	@Override
	public void onMessageReceived(final Player player, final IncomingMessage message) {
		if (message.getAction() == OrionAction.CHAT_MESSAGE) {
			final String senderName = message.readString();
			final String chatMessage = message.readString();

			for (final Player online : Bukkit.getOnlinePlayers()) {
				//Common.tellNoPrefix(online, "&8[&5" + message.getServerName() + "&8] &7" + senderName + ": &f" + chatMessage);
				Common.tellNoPrefix(online, String.format("&8[&5%s&8] &7%s: &f%s", message.getServerName(), senderName, chatMessage));
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public final void onChat(final AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();
		final String message = event.getMessage();

		BungeeUtil.tellBungee(OrionAction.CHAT_MESSAGE, player.getName(), message);
	}
}
