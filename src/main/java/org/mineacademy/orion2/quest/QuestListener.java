package org.mineacademy.orion2.quest;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.mineacademy.orion2.PlayerCache;
import org.mineacademy.orion2.boss.model.Boss;
import org.mineacademy.orion2.boss.npc.NPC;

public class QuestListener implements Listener  {

	@EventHandler
	public void onEntityDeath(final EntityDeathEvent event) {
		if (event.getEntity().getKiller() == null)
			return;

		final LivingEntity victim = event.getEntity();
		final Player killer = victim.getKiller();
		final PlayerCache.ActiveQuestCache quest = PlayerCache.getCache(killer).getActiveQuest();

		if (quest != null)
			quest.getQuest().onKill(killer, victim, event);
	}

	@EventHandler
	public void onEntityDamage(final EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity))
			return;

		final LivingEntity damager = (LivingEntity) event.getDamager();
		final LivingEntity victim = (LivingEntity) event.getEntity();

		// For help on handling those, see ClassListener and the video about classes
		if (damager instanceof Player) {
			final Player damagerPlayer = (Player) damager;
			final PlayerCache.ActiveQuestCache quest = PlayerCache.getCache(damagerPlayer).getActiveQuest();

			if (quest != null)
				quest.getQuest().onAttack(damagerPlayer, victim, event);
		}

		if (victim instanceof Player) {
			final Player victimPlayer = (Player) victim;
			final PlayerCache.ActiveQuestCache quest = PlayerCache.getCache(victimPlayer).getActiveQuest();

			if (quest != null)
				quest.getQuest().onDamaged(damager, victimPlayer, event);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST) // adjusted priority due to NPC right clicking handling
	public void onRightClick(final PlayerInteractEntityEvent event) {
		if (!(event.getRightClicked() instanceof LivingEntity))
			return;

		final LivingEntity rightClicked = (LivingEntity) event.getRightClicked();
		final Boss boss = Boss.findBoss(rightClicked);

		if (!(boss instanceof NPC))
			return;

		final Player player = event.getPlayer();
		final PlayerCache cache = PlayerCache.getCache(player);
		final PlayerCache.ActiveQuestCache activeQuest = cache.getActiveQuest();

		if (activeQuest != null)
			activeQuest.getQuest().onRightClickNPC(cache, player, (NPC) boss, event);
	}
}


