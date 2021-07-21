package org.mineacademy.orion2.quest.model;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.orion2.PlayerCache;
import org.mineacademy.orion2.classes.model.ClassBase;

public class QuestZombieSlayer extends Quest {

	private static final int THRESHOLD = 3;

	protected QuestZombieSlayer() {
		super("Zombie Slayer");
	}

	@Override
	public Quest getNext(final ClassBase classBase) {
		return classBase == ClassBase.WARRIOR || classBase == ClassBase.ARCHER ? BOSS_KILLER : null;
	}

	@Override
	public CompMaterial getIcon() {
		return CompMaterial.IRON_SWORD;
	}

	@Override
	public String[] getMenuLore() {
		return new String[] {
				"Kill 3 zombies to",
				"complete this quest."
		};
	}

	@Override
	public String getCompletion(final Player player, final Object completionData) {
		return "Killed " + (completionData == null ? "0" : completionData) + "/" + THRESHOLD + " Zombies";
	}

	@Override
	public void onKill(final Player killer, final LivingEntity victim, final EntityDeathEvent event) {
		final PlayerCache cache = PlayerCache.getCache(killer);

		if (victim.getType() == EntityType.ZOMBIE) {
			final Object data = cache.getActiveQuestData();
			final int zombiesKilled = (data != null ? (int) data : 0) + 1;

			if (zombiesKilled >= THRESHOLD) {
				cache.completeQuest();

				Common.tell(killer, "&2Congratulations for completing this quest!");
				CompSound.FIREWORK_LAUNCH.play(killer);

			} else {
				cache.updateActiveQuestData(zombiesKilled);

				Common.tell(killer, "&6Only " + (THRESHOLD - zombiesKilled) + " zombie kills left to complete your quest!");
			}
		}
	}
}

