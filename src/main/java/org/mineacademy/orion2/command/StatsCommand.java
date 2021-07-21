package org.mineacademy.orion2.command;

import org.bukkit.entity.Player;
import org.mineacademy.fo.*;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.orion2.DataFile;
import org.mineacademy.orion2.PlayerCache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatsCommand extends SimpleCommand {

	public StatsCommand() {
		super("stats");

		setMinArguments(1);
		setUsage("<play|kills|deaths|ktd>");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final String param = args[0].toLowerCase();

		if ("play".equals(param)) {
			final Player player = getPlayer();
			final long ticksPlayed = PlayerUtil.getStatistic(player, Remain.getPlayTimeStatisticName());

			tell("You played on the server for " + TimeUtil.formatTimeDays(ticksPlayed / 20) + ".");
		}

		else if ("kills".equals(param) || "deaths".equals(param) || "ktd".equals(param)) {
			final DataFile data = DataFile.getInstance();

			final List<DataFile.PlayerStatisticData> kills = data.getStatistics(PlayerCache::getPlayerKills);
			final List<DataFile.PlayerStatisticData> deaths = data.getStatistics(PlayerCache::getDeaths);

			if ("kills".equals(param)) {
				tellNoPrefix("&7============ &6Top Player Kills &7============");

				int position = 1;

				for (final DataFile.PlayerStatisticData statistic : kills)
					if (statistic.getValue() > 0) {
						tellNoPrefix("&f#" + position + " &7- &f" + statistic.getPlayer().getName() + " &7(" + statistic.getValue() + ")");

						position++;
					}
			}

			else if ("deaths".equals(param)) {
				tellNoPrefix("&7============ &6Top Deaths &7============");

				int position = 1;

				for (final DataFile.PlayerStatisticData statistic : deaths)
					if (statistic.getValue() > 0) {
						tellNoPrefix("&f#" + position + " &7- &f" + statistic.getPlayer().getName() + " &7(" + statistic.getValue() + ")");

						position++;
					}
			}

			else if ("ktd".equals(param)) {
				final List<String> messages = new ArrayList<>();

				messages.add("&7" + Common.chatLineSmooth());
				messages.add(ChatUtil.center("&6Top Kills to Death Ratio"));
				messages.add("&7" + Common.chatLineSmooth());
				messages.add(" ");

				Collections.sort(deaths, (first, second) -> {
					final int firstKills = PlayerCache.getCache(first.getUuid()).getPlayerKills();
					final int secondKills = PlayerCache.getCache(first.getUuid()).getPlayerKills();

					return Double.compare(MathUtil.atLeast(secondKills, 1) / MathUtil.atLeast(second.getValue(), 1),
							MathUtil.atLeast(firstKills, 1) / MathUtil.atLeast(first.getValue(), 1));
				});

				int position = 1;

				// We have to decide if we want to iterate deaths or kills and then find counterpart
				for (final DataFile.PlayerStatisticData deathStat : deaths) {
					final int otherKills = PlayerCache.getCache(deathStat.getUuid()).getPlayerKills();

					if (deathStat.getValue() > 0 && otherKills > 0) {
						final String killToDeath = MathUtil.formatOneDigit(MathUtil.atLeast(otherKills, 1) / MathUtil.atLeast(deathStat.getValue(), 1));

						messages.add("&f#" + position + " &7- &f" + deathStat.getPlayer().getName() + " &7(" + killToDeath + ")");

						position++;
					}
				}

				tellNoPrefix(ChatUtil.verticalCenter(messages));
			}
		}

		else
			returnInvalidArgs();
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord("play", "kills", "deaths", "ktd");

		return new ArrayList<>();
	}
}
