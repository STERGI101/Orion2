package org.mineacademy.orion2.boss;

import lombok.Data;
import org.bukkit.entity.LivingEntity;
import org.mineacademy.orion2.boss.model.Boss;

@Data
public final class SpawnedBoss {

	private final Boss boss;
	private final LivingEntity entity;
}

