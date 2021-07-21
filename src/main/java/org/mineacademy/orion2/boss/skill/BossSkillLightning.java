package org.mineacademy.orion2.boss.skill;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.MathUtil;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.orion2.boss.SpawnedBoss;

public class BossSkillLightning extends BossSkill {

	public BossSkillLightning() {
		super("Strike Player");
	}

	@Override
	public void onBossDamaged(final SpawnedBoss spawnedBoss, final EntityDamageByEntityEvent event) {
		if (RandomUtil.chance(50) && event.getDamager() instanceof Player) {
			final Player attacker = (Player) event.getDamager();

			attacker.getWorld().strikeLightningEffect(attacker.getLocation());

			if (attacker.getGameMode() == GameMode.SURVIVAL)
				attacker.setHealth(MathUtil.range(attacker.getHealth() - 4, 0, Integer.MAX_VALUE) );

			Common.tell(attacker, "&bThe boss " + spawnedBoss.getBoss().getName() + " wants you to shine!");
		}
	}
}
