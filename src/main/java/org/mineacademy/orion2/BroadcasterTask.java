package org.mineacademy.orion2;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.remain.Remain;

import java.util.Arrays;
import java.util.List;

public class BroadcasterTask extends BukkitRunnable {

	private final List<String> messages = Arrays.asList(
			"Check out our website https.//mineacademy.org.",
			"You can support our server and purchase ranks at https://mineacademy.org/vip.",
			"Use the /orion command to run some fancy features of our custom plugin Orion"
	);

	@Override
	public void run() {

		String prefix = "&8[&4Tip&8]";
		String message = RandomUtil.nextItem(messages);
		for(Player player : Remain.getOnlinePlayers())
			Common.tellNoPrefix(player, prefix + message);
	}
}
