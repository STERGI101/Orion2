package org.mineacademy.orion2.rank.model;

import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.orion2.PlayerCache;

public class RankCraftsman extends Rank {

	protected RankCraftsman() {
		super("Craftsman", ChatColor.WHITE);
	}

	@Override
	protected boolean canUpgrade(final Player player, final PlayerCache cache, final Rank next) {
		final long zombiesKilled = PlayerUtil.getStatistic(player, Statistic.KILL_ENTITY, EntityType.ZOMBIE);

		return zombiesKilled > 10;
	}

	@Override
	public Rank getNext() {
		return HERO;
	}
}

