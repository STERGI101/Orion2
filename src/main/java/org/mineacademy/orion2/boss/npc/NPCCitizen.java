package org.mineacademy.orion2.boss.npc;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.orion2.hook.CitizensHook;

public abstract class NPCCitizen extends NPC {

	/**
	 * Create a new NPC using the Citizens plugin
	 *
	 * @param name
	 * @param type
	 */
	protected NPCCitizen(final String name, final EntityType type) {
		super(name, type);
	}

	/**
	 * Create a new NPC using the Citizens plugin
	 */
	@Override
	protected final LivingEntity makeEntity(final Location location) {
		return HookManager.isCitizensLoaded() ? CitizensHook.makeEntity(this, location) : super.makeEntity(location);
	}

}