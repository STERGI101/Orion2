package org.mineacademy.orion2.boss.npc;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.CompProperty;
import org.mineacademy.orion2.PlayerCache;
import org.mineacademy.orion2.boss.model.Boss;

/**
 * Represents the core model for an NPC, building upon the Boss system we had already
 * created
 *
 * NPCs are auto-registered in the Boss class
 */
public class NPC extends Boss {

	/**
	 * The NPC prefix when using tell methods below
	 */
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private String tellPrefix;

	/**
	 * Has AI? Moves around, gravity etc.
	 */
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private boolean ai = false;

	/**
	 * Create a new NPC with the given name of the given type
	 *
	 * NB: DO NOT TYPE IN PLAYER ENTITY TYPE THERE, IT WONT WORK!
	 * PLAYER NPC REQUIRE CITIZENS LIBRARY THAT WE COVER IN WEEK 4
	 *
	 * @param name
	 * @param type
	 */
	protected NPC(final String name, final EntityType type) {
		super(name, type);

		setCanSpawnRandomly(false);
	}

	// --------------------------------------------------------------------------------------------------------------
	// Handling events
	// --------------------------------------------------------------------------------------------------------------

	@Override
	protected final void onSpawn(final Location location, final LivingEntity entity) {

		// Prevent NPC from moving
		if (!ai)
			CompProperty.AI.apply(entity, false);

		// God mode for NPC - it can still be killed by other means though
		CompProperty.INVULNERABLE.apply(entity, true);

		// Disable NPC making sounds
		CompProperty.SILENT.apply(entity, true);

		// Should prevent despawning
		entity.setRemoveWhenFarAway(false);

		onNPCSpawn(location, entity);
	}

	/**
	 * Called after the NPC is spawned and its AI disabled
	 *
	 * @param location
	 * @param entity
	 */
	protected void onNPCSpawn(final Location location, final LivingEntity entity) {
	}

	// Automatically handle right click NPCs and send data to players active quest
	@Override
	public final void onRightClick(final Player player, final LivingEntity npc, final PlayerInteractEntityEvent event) {
		final PlayerCache cache = PlayerCache.getCache(player);

		// Already doing some quest or conversing with us,
		// let the quest handle the right click
		if (cache.getActiveQuest() != null || player.isConversing())
			return;

		// Or, prevent quest from catching this right click and handle it here
		event.setCancelled(true);

		onNPCRightClick(player, npc, event);
	}

	/**
	 * Called automatically when a player right clicks this NPC
	 *
	 * @param player
	 * @param entity
	 * @param event
	 */
	protected void onNPCRightClick(final Player player, final LivingEntity entity, final PlayerInteractEntityEvent event) {
	}

	/**
	 * Prevent NPCs from taking damage
	 *
	 * @param attacker
	 * @param bossVictim
	 * @param event
	 */
	@Override
	public final void onDamaged(final LivingEntity attacker, final LivingEntity bossVictim, final EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}

	// --------------------------------------------------------------------------------------------------------------
	// Convenience methods
	// --------------------------------------------------------------------------------------------------------------

	/**
	 * Convenience method for sending a message to the player
	 * using {@link #tellPrefix}
	 *
	 * @param player
	 * @param message
	 */
	public final void tell(final Player player, final String message) {
		Common.tellNoPrefix(player, Common.getOrEmpty(tellPrefix) + message);
	}

	/**
	 * Convenience method for sending a non-repetitive message to the player,
	 * see {@link Common#tellTimed(int, org.bukkit.command.CommandSender, String)}
	 * using the {@link #tellPrefix}
	 *
	 * @param delaySeconds
	 * @param player
	 * @param message
	 */
	public final void tellTimed(final int delaySeconds, final Player player, final String message) {
		Common.tellTimedNoPrefix(delaySeconds, player, Common.getOrEmpty(tellPrefix) + message);
	}
}