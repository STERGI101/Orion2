package org.mineacademy.orion2.boss.skill;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import org.mineacademy.fo.Common;
import org.mineacademy.orion2.PlayerCache;
import org.mineacademy.orion2.boss.SpawnedBoss;
import org.mineacademy.orion2.classes.model.ClassBase;

public class BossSkillThrow extends BossSkill {

	public BossSkillThrow() {
		super("Throw Player");

		setDelaySeconds(5);
	}

	@Override
	public void onBossDamaged(final SpawnedBoss spawnedBoss, final EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			final Player attacker = (Player) event.getDamager();
			final PlayerCache cache = PlayerCache.getCache(attacker);

			final int warriorTier = cache.getClassTier(ClassBase.WARRIOR);

			if (warriorTier < 3) {
				attacker.setVelocity(new Vector(0, 3, 0));

				Common.tell(attacker, "&eThe boss " + spawnedBoss.getBoss().getName() + " thinks you can fly!");
			}
		}
	}
}
