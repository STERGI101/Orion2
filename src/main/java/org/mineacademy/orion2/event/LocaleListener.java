package org.mineacademy.orion2.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;

public class LocaleListener implements Listener {

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();

		//Common.log("The locale of " + player.getName() + " is " + Remain.getLocale(player));
	}

	@EventHandler
	public void onLocaleChange(final PlayerLocaleChangeEvent event) {
		/*final Player player = event.getPlayer();

		final String newLocale = Remain.getLocale(player);

		if (newLocale.equals("en_us"))
			Common.tell(player, "You are now speaking English!");

		Common.log("The locale of " + player.getName() + " has been changed to " + Remain.getLocale(player));*/
	}
}

