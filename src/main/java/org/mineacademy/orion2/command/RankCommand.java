package org.mineacademy.orion2.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.TimeUtil;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.orion2.PlayerCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RankCommand extends SimpleCommand {
	public RankCommand() {
		super("rank");

		setUsage("[top|upgrade]");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		final PlayerCache cache = PlayerCache.getCache(player);

		final String param = args.length == 1 ? args[0].toLowerCase() : "";

		if ("top".equals(param)) {

			// If you have large player base, run this async so it won't block your server heartbeat
			// while we read your stats files
			Common.runLaterAsync(0, () -> {
				tell("&7============ &6Top Zombie Kills &7============");

				for (final Map.Entry<Long, OfflinePlayer> entry : PlayerUtil.getStatistics(Statistic.KILL_ENTITY, EntityType.ZOMBIE).entrySet()) {
					final long zombiesKilled = entry.getKey();
					final String playerName = entry.getValue().getName();

					tell("&6" + Common.plural(zombiesKilled, "zombie") + " - " + playerName);
				}

				tell("&7============ &6Top Play Time &7============");

				for (final Map.Entry<Long, OfflinePlayer> entry : PlayerUtil.getStatistics(Statistic.PLAY_ONE_MINUTE).entrySet()) {
					final long playedTicks = entry.getKey();
					final String playerName = entry.getValue().getName();

					tell("&6" + playerName + " - " + TimeUtil.formatTimeDays(playedTicks / 20));
				}
			});
		}

		else if ("upgrade".equals(param)) {
			checkNotNull(cache.getRank().getNext(), "&6You have reached the maximum rank.");

			if (!cache.getRank().upgradeToNextRank(player))
				tell("&cYou do not qualify to upgrade to " + cache.getRank().getNext().getName());

		}

		else
			tell("Your rank: " + cache.getRank().getName());
	}

	@Override
	protected List<String> tabComplete() {

		if(args.length == 1)
			return completeLastWord("top", "upgrade");

		return new ArrayList<>();
	}
}

