package org.mineacademy.orion2.boss;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.BlockUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.debug.LagCatcher;
import org.mineacademy.orion2.boss.model.Boss;

public class BossTimedTask extends BukkitRunnable {

	@Override
	public void run() {

		// Handle active boss ticking
		LagCatcher.start("boss-ticking");

		try {
			for (final World world : Bukkit.getWorlds())
				for (final LivingEntity entity : world.getLivingEntities()) {
					final Boss boss = Boss.findBoss(entity);

					if (boss != null)
						boss.onTick(entity);
				}

		} finally {
			LagCatcher.end("boss-ticking");
		}

		// Handle boss random spawning around players
		LagCatcher.start("boss-spawning");

		try {
			final int radius = 30;

			for (final World world : Bukkit.getWorlds()) {

				// If you want to execute every alive Boss' skill:
				// for entity
				// if it is a boss
				// for all skills
				// onSkillTick (create that in skills)

				//if (world.hasStorm())
				for (final Player player : world.getPlayers()) {

					// Visualize a spherical location around player - this is the arena in which we spawn the Boss
					// Disabled because it drains performance a lot

					// Use this for stress testing, runs the code in the block 50 times
					/*LagCatcher.testPerformance(50, "particle-rendering", () -> {
						for (final Location outerSphereLocation : BlockUtil.getSphere(player.getLocation(), radius, true))
							CompParticle.VILLAGER_HAPPY.spawn(outerSphereLocation);
					});*/

					if (getBossesInWorld(world) > 1) // Limit to max 2 bosses per world, bit unprecise
						break;

					if (getBossesInRadius(player, radius) > 0) // Limit to max 1 boss per player radius
						continue;

					if (!RandomUtil.chanceD(0.01)) // Set the chance to 1% - increase this to 1.00 for 100% to see the boss instantly
						continue;

					final Location randomLocation = RandomUtil.nextLocation(player.getLocation(), radius, true);
					final int highestY = BlockUtil.findHighestBlockNoSnow(randomLocation);

					if (highestY != -1) {
						randomLocation.setY(highestY);

						spawnOneRandomBoss(randomLocation);
					}
				}
			}

		} finally {
			LagCatcher.end("boss-spawning");
		}
	}

	private int getBossesInWorld(final World world) {
		int foundEntities = 0;

		for (final Entity worldEntity : world.getLivingEntities())
			if (Boss.findBoss(worldEntity) != null)
				foundEntities++;

		return foundEntities;
	}

	private int getBossesInRadius(final Player player, final int radius) {
		int foundEntities = 0;

		for (final Entity nearbyEntity : player.getNearbyEntities(radius, player.getWorld().getMaxHeight(), radius))
			if (Boss.findBoss(nearbyEntity) != null)
				foundEntities++;

		return foundEntities;
	}

	private void spawnOneRandomBoss(final Location spawnLocation) {
		final Boss randomBoss = RandomUtil.nextItem(Boss.getBosses(), Boss::canSpawnRandomly);

		if (randomBoss != null) {
			Common.log("Spawning " + randomBoss.getName() + " at " + Common.shortLocation(spawnLocation));

			randomBoss.spawn(spawnLocation);
		}
	}
}
