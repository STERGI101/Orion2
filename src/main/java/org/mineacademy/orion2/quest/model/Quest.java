package org.mineacademy.orion2.quest.model;

import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.orion2.boss.npc.NPC;
import org.mineacademy.orion2.classes.model.ClassBase;
import org.mineacademy.orion2.PlayerCache;

import java.util.*;

/**
 * The core model class for quest and mission system
 */
public abstract class Quest {

	/**
	 * Stores active quests by their name, quests are singletons
	 */
	private static final Map<String, Quest> byName = new HashMap<>();

	// --------------------------------------------------------------------------------------------------------------
	// Place your quests below, make sure to give them protected constructor to prevent accidental initializing
	// --------------------------------------------------------------------------------------------------------------

	public static final Quest ZOMBIE_SLAYER = new QuestZombieSlayer();
	public static final Quest BOSS_KILLER = new QuestBossKill();
	public static final Quest COME_HOME = new QuestComeHome();
	public static final Quest ARMORED_DELIVERY = new QuestArmoredDelivery();

	// --------------------------------------------------------------------------------------------------------------

	/**
	 * The name of this quest
	 */
	@Getter
	private final String name;

	/**
	 * Create a new quest of that given name
	 *
	 * The name is used when saving the file so please do not use special characters or colors
	 *
	 * @param name
	 */
	protected Quest(final String name) {
		this.name = name;

		byName.put(name, this);
	}

	/**
	 * Return which quest should follow if we complete this one?
	 *
	 * This can return different quests based on different classes allowing infinite combinations and non-linear paths
	 *
	 * @param classBase
	 * @return
	 */
	public abstract Quest getNext(ClassBase classBase);

	/**
	 * Get the quest icon show in menus
	 *
	 * @return
	 */
	public abstract CompMaterial getIcon();

	/**
	 * Get quest lore (description) shown in menu and quest start prompt
	 *
	 * @return
	 */
	public abstract String[] getMenuLore();

	/**
	 * Get the completion message in case there is a player
	 *
	 * @param player
	 * @param completionData
	 * @return
	 */
	public abstract String getCompletion(final Player player, final Object completionData);

	// --------------------------------------------------------------------------------------------------------------
	// Events called automatically if the related player is going through this quest
	// --------------------------------------------------------------------------------------------------------------

	/**
	 * Called if the given player has this quest active and kills someone
	 *
	 * @param killer
	 * @param victim
	 * @param event
	 */
	public void onKill(final Player killer, final LivingEntity victim, final EntityDeathEvent event) {
	}

	/**
	 * Called if the given player has this quest active and attacks someone
	 *
	 * @param attacker
	 * @param victim
	 * @param event
	 */
	public void onAttack(final Player attacker, final LivingEntity victim, final EntityDamageByEntityEvent event) {
	}

	/**
	 * Called if something attacks a player that is going through this quest
	 *
	 * @param attacker
	 * @param victim
	 * @param event
	 */
	public void onDamaged(final LivingEntity attacker, final Player victim, final EntityDamageByEntityEvent event) {
	}

	/**
	 * Called automatically when the player doing this quest right clicks an entity such as NPC
	 *
	 * @param cache
	 * @param player
	 * @param npc
	 * @param event
	 */
	public void onRightClickNPC(final PlayerCache cache, final Player player, final NPC npc, final PlayerInteractEntityEvent event) {
	}

	/**
	 * Called automatically from {@link org.mineacademy.orion2.quest.QuestTask}
	 *
	 * @param cache
	 * @param player
	 */
	public void onTick(final PlayerCache cache, final Player player) {
	}

	// --------------------------------------------------------------------------------------------------------------
	// Static access
	// --------------------------------------------------------------------------------------------------------------

	/**
	 * Return the quest class from the given name, or null
	 *
	 * @param name
	 * @return
	 */
	public static final Quest getByName(final String name) {
		return byName.get(name);
	}

	/**
	 * Return all quests
	 *
	 * @return
	 */
	public static final Collection<Quest> getQuests() {
		return Collections.unmodifiableCollection(byName.values());
	}

	/**
	 * Return all quests as their names
	 *
	 * @return
	 */
	public static final Set<String> getQuestsNames() {
		return Collections.unmodifiableSet(byName.keySet());
	}
}
