package org.mineacademy.orion2.boss.skill;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.remain.CompParticle;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.orion2.boss.SpawnedBoss;

public class BossSkillBlockAttack extends BossSkill {

	public BossSkillBlockAttack() {
		super("Block Attack");
	}

	@Override
	public void onBossDamaged(final SpawnedBoss spawnedBoss, final EntityDamageByEntityEvent event) {
		if (RandomUtil.chance(50) && event.getDamager() instanceof Player) {
			event.setCancelled(true);

			final Player attacker = (Player) event.getDamager();

			Common.tell(attacker, "&cThe boss " + spawnedBoss.getBoss().getName() + " has blocked your attack!");

			CompSound.ANVIL_LAND.play(attacker);
			CompParticle.SWEEP_ATTACK.spawn(spawnedBoss.getEntity().getLocation().add(0, 2, 0));
		}
	}
}
