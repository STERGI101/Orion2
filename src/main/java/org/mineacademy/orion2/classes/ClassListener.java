package org.mineacademy.orion2.classes;

import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.mineacademy.orion2.PlayerCache;

public class ClassListener implements Listener {

	@EventHandler
	public void onEntityDamage(final EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity))
			return;

		final LivingEntity damager = (LivingEntity) event.getDamager();
		final LivingEntity victim = (LivingEntity) event.getEntity();

		// First, check if the attacker is a player
		// and if that player has a class then we iterate through them
		// and invoke onAttack method for that class so that you can work with it
		if (damager instanceof Player) {
			final Player damagerPlayer = (Player) damager;
			final PlayerCache cache = PlayerCache.getCache(damagerPlayer);

			for (final PlayerCache.ClassCache classCache : cache.getClasses())
				classCache.getClassBase().onAttack(classCache.getTier(), damagerPlayer, victim, event);
		}

		// Second, check if the victim is a player and do the same loop as above just for the victim
		if (victim instanceof Player) {
			final Player victimPlayer = (Player) victim;
			final PlayerCache cache = PlayerCache.getCache(victimPlayer);

			for (final PlayerCache.ClassCache classCache : cache.getClasses())
				classCache.getClassBase().onDamaged(classCache.getTier(), damager, victimPlayer, event);
		}
	}

	@EventHandler
	public void onBlockBreak(final BlockBreakEvent event) {
		final Player player = event.getPlayer();

		if (player.getGameMode() == GameMode.SURVIVAL) {
			final PlayerCache cache = PlayerCache.getCache(player);

			for (final PlayerCache.ClassCache classCache : cache.getClasses())
				classCache.getClassBase().onBreakBlock(classCache.getTier(), player, event.getBlock(), event);
		}
	}
}
