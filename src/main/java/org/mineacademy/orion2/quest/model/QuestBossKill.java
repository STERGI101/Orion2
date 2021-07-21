package org.mineacademy.orion2.quest.model;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.orion2.PlayerCache;
import org.mineacademy.orion2.boss.model.Boss;
import org.mineacademy.orion2.classes.model.ClassBase;

public class QuestBossKill extends Quest {

	protected QuestBossKill() {
		super("Boss Killer");
	}

	@Override
	public Quest getNext(final ClassBase classBase) {
		return null;
	}

	@Override
	public CompMaterial getIcon() {
		return CompMaterial.SKELETON_SKULL;
	}

	@Override
	public String[] getMenuLore() {
		return new String[] {
				"Kill Warrior boss to",
				"complete this quest."
		};
	}

	@Override
	public String getCompletion(final Player player, final Object completionData) {
		return "Go kill a Warrior Boss";
	}

	@Override
	public void onKill(final Player killer, final LivingEntity victim, final EntityDeathEvent event) {
		final PlayerCache cache = PlayerCache.getCache(killer);
		final Boss boss = Boss.findBoss(victim);

		if (boss == Boss.WARRIOR) {
			cache.completeQuest();

			Common.tell(killer, "&2Congratulations for completing this quest!");
			CompSound.FIREWORK_LARGE_BLAST2.play(killer);
		}
	}
}

