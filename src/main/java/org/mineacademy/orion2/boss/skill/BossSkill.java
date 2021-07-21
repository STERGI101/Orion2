package org.mineacademy.orion2.boss.skill;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.mineacademy.orion2.boss.SpawnedBoss;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BossSkill {

	private final String name;

	@Setter(AccessLevel.PROTECTED)
	private int delaySeconds = 0;
	private long lastExecutedTime = 0;

	public void onBossAttack(final SpawnedBoss spawnedBoss, final EntityDamageByEntityEvent event) {
	}

	public void onBossDamaged(final SpawnedBoss spawnedBoss, final EntityDamageByEntityEvent event) {
	}

	// Return true if we can run this skill and also update last executed time
	public final boolean checkLastRun() {
		if (delaySeconds == 0)
			return true;

		if (lastExecutedTime == 0 || System.currentTimeMillis() - lastExecutedTime > delaySeconds * 1000) {
			updateLastExecutedTime();

			return true;
		}

		return false;
	}

	private void updateLastExecutedTime() {
		lastExecutedTime = System.currentTimeMillis();
	}

	@Override
	public final boolean equals(final Object obj) {
		return obj instanceof BossSkill && ((BossSkill)obj).getName().equals(this.name);
	}
}
