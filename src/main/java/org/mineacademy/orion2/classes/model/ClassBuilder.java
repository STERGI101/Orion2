package org.mineacademy.orion2.classes.model;

import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.orion2.PlayerCache;

public class ClassBuilder extends ClassBase {

	protected ClassBuilder() {
		super("Builder");
	}

	@Override
	public void onBreakBlock(final int tier, final Player player, final Block block, final BlockBreakEvent event) {
		upgradeToNextTier(player);
	}

	@Override
	protected boolean canUpgrade(final Player player, final PlayerCache cache, final int nextTier) {
		final long grassBroken = PlayerUtil.getStatistic(player, Statistic.MINE_BLOCK, CompMaterial.GRASS_BLOCK.getMaterial());

		Common.log("Broke grass: " + grassBroken);
		Common.log("Required: " + (nextTier * 10));

		// TIP: You can add more complex math calculation to make it harder and harder to obtain higher tiers
		return grassBroken >= (nextTier * 10);
	}

	@Override
	protected void onUpgrade(final Player player, final int nextTier) {
		CompSound.LEVEL_UP.play(player);
		player.giveExpLevels(nextTier * 10);
	}
}

