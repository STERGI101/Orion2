package org.mineacademy.orion2.boss;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMetadata;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.orion2.boss.model.Boss;
import org.mineacademy.orion2.boss.skill.BossSkill;

import java.util.List;

public class BossListener implements Listener {

	@EventHandler
	public void onCombust(final EntityCombustEvent event) {
		final Boss boss = findBoss(event.getEntity());

		if (boss != null)
			event.setCancelled(true);
	}

	@EventHandler
	public void onDeath(final EntityDeathEvent event) {
		final Boss boss = findBoss(event.getEntity());

		if (boss != null) {
			final List<ItemStack> drops = event.getDrops();
			final LivingEntity entity = event.getEntity();

			// Handle drops
			final BossEquipment equipment = boss.getEquipment();

			drops.clear();
			drops.add(buildUndamagedItem(equipment.getHelmet()));
			drops.add(buildUndamagedItem(equipment.getChestplate()));
			drops.add(buildUndamagedItem(equipment.getLeggings()));
			drops.add(buildUndamagedItem(equipment.getBoots()));

			// Handle experience
			if (boss.getDroppedExp() != null)
				event.setDroppedExp(event.getDroppedExp());

			// Handle riding entity
			final Entity vehicle = entity.getVehicle();

			if (vehicle != null)
				vehicle.remove();

			boss.onDeath(entity.getKiller(), entity, event);
		}
	}

	@EventHandler
	public void onEntityDamage(final EntityDamageByEntityEvent event) {
		final Entity damager = event.getDamager();
		final Entity victim = event.getEntity();

		if (!(damager instanceof LivingEntity) || !(victim instanceof LivingEntity))
			return;

		Boss boss = findBoss(damager);

		if (boss != null) {
			final SpawnedBoss spawnedBoss = new SpawnedBoss(boss, (LivingEntity) damager);

			for (final BossSkill skill : boss.getSkills())
				if (skill.checkLastRun())
					// Edit: If you want only one skill to execute at the time, you can change the onBossAttack to boolean,
					// return true if the skill does something and then put this into if (skill.onBossAttack) block
					// and put a "break" within it
					skill.onBossAttack(spawnedBoss, event);

			boss.onAttack((LivingEntity) damager, (LivingEntity) victim, event);
		}

		boss = findBoss(victim);

		if (boss != null) {
			final SpawnedBoss spawnedBoss = new SpawnedBoss(boss, (LivingEntity) victim);

			for (final BossSkill skill : boss.getSkills())
				if (skill.checkLastRun())
					// Edit: If you want only one skill to execute at the time, see comments above for onBossAttack
					skill.onBossDamaged(spawnedBoss, event);

			boss.onDamaged((LivingEntity) damager, (LivingEntity) victim, event);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityInteract(final PlayerInteractEntityEvent event) {
		if (!Remain.isInteractEventPrimaryHand(event))
			return;

		final Entity clickedEntity = event.getRightClicked();
		final Player player = event.getPlayer();

		if (!(clickedEntity instanceof LivingEntity))
			return;

		final Boss boss = findBoss(clickedEntity);

		if (boss != null)
			boss.onRightClick(player, (LivingEntity) clickedEntity, event);
	}

	@EventHandler
	public void onSpawnerSpawn(final SpawnerSpawnEvent event) {
		final CreatureSpawner spawner = event.getSpawner();

		if (!CompMetadata.hasMetadata(spawner, Boss.BOSS_SPAWNER_TAG))
			return;

		final String bossName = CompMetadata.getMetadata(spawner, Boss.BOSS_SPAWNER_TAG);
		final Boss boss = Boss.findBoss(bossName);

		if (boss != null) {
			event.setCancelled(true);

			boss.spawn(event.getLocation());
		}
	}

	private ItemStack buildUndamagedItem(final ItemCreator.ItemCreatorBuilder item) {
		return item.damage(0).build().makeSurvival();
	}

	private Boss findBoss(final Entity entity) {
		return Boss.findBoss(entity);
	}
}
