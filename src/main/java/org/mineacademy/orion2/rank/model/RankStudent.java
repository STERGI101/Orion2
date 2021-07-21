package org.mineacademy.orion2.rank.model;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.TimeUtil;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.orion2.PlayerCache;

public class RankStudent extends Rank {

	protected RankStudent() {
		super("Student", ChatColor.GRAY);
	}

	@Override
	protected boolean canUpgrade(final Player player, final PlayerCache cache, final Rank next) {
		final long playTimeTicks = PlayerUtil.getStatistic(player, Remain.getPlayTimeStatisticName());
		final long threshold = TimeUtil.toTicks("4 hours 1 minutes"); // Edit: You can also use the old calculating method: 11 /* HOURS */ * 60 * 60 * 20 + 41 /* MINUTES */ * 60 * 20;

		return playTimeTicks > threshold;
	}

	@Override
	public Rank getNext() {
		return CRAFTSMAN;
	}
}
