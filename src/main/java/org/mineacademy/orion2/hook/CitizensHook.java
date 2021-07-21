package org.mineacademy.orion2.hook;

import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Goal;
import net.citizensnpcs.api.ai.goals.TargetNearbyEntityGoal;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.orion2.boss.model.Boss;
import org.mineacademy.orion2.boss.npc.NPCSirRichard;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CitizensHook {

	public static LivingEntity makeEntity(final Boss boss, final Location location) {
		final String bossName = boss.getName();
		final NPC citizen = CitizensAPI.getNPCRegistry().createNPC(boss.getType(), bossName);

		final boolean spawned = citizen.spawn(location);
		final Entity entity = citizen.getEntity();

		Valid.checkBoolean(spawned, "Unable to spawn " + bossName + " at " + Common.shortLocation(location));
		Valid.checkBoolean(entity instanceof LivingEntity, "Boss " + bossName + " must be LivingEntity, got " + entity.getClass());

		return (LivingEntity) entity;
	}

	public static void applyNavigation(final NPCSirRichard boss, final LivingEntity entity) {
		final NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);
		Valid.checkNotNull(npc, "NPC was not found in " + boss.getName() + " from " + entity);

		final Goal goal = TargetNearbyEntityGoal.builder(npc)
				.aggressive(true)
				.radius(12)
				.targets(Sets.newHashSet(EntityType.VILLAGER, EntityType.CHICKEN))
				.build();

		npc.getDefaultGoalController().addGoal(goal, 1);
	}
}
