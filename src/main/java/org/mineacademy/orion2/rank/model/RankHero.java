package org.mineacademy.orion2.rank.model;

import org.bukkit.ChatColor;

public class RankHero extends Rank {

	protected RankHero() {
		super("Hero", ChatColor.DARK_AQUA);
	}

	@Override
	public Rank getNext() {
		return MASTER;
	}
}

