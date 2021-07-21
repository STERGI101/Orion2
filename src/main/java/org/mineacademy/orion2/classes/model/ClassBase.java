package org.mineacademy.orion2.classes.model;

import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.MathUtil;
import org.mineacademy.fo.remain.CompAttribute;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlConfig;
import org.mineacademy.orion2.PlayerCache;
import org.mineacademy.orion2.quest.model.Quest;

import java.util.*;

/**
 * The core model class for our class system. The class system has
 * non-linear progression so each class has levels (such as Builder I, Builder II etc.)
 * named tiers.
 *
 * Players can have multiple classes.
 */
@Getter
public abstract class ClassBase extends YamlConfig {

	/**
	 * Stores active classes by their name, so they are singletons
	 */
	private static final Map<String, ClassBase> byName = new HashMap<>();

	// --------------------------------------------------------------------------------------------------------------
	// Place your classes below, make sure to give them protected constructor to prevent accidental initializing
	// --------------------------------------------------------------------------------------------------------------

	public static final ClassBase ARCHER = new ClassArcher();
	public static final ClassBase WARRIOR = new ClassWarrior();
	public static final ClassBase BUILDER = new ClassBuilder();

	// --------------------------------------------------------------------------------------------------------------

	/**
	 * The highest tier of this class
	 */
	private int maxTier;

	/**
	 * How much extra damage should this class deal?
	 *
	 * See {@link #applyFor(int, Player)}
	 */
	private double damageModifier;

	/**
	 * What should be the hit cooldown introduced in MC 1.9 for this class?
	 *
	 * See {@link #applyFor(int, Player)}
	 */
	private double hitCooldown;

	/**
	 * The menu icon of this class
	 */
	private CompMaterial icon;

	/*
	/**
	 * If the Boss has the {@link org.mineacademy.orion.boss.skill.BossSkillThrow}
	 * how much we should reduce the throw?
	 */
	private int bossThrowReduction;

	/**
	 * The first quest of this class
	 */
	private Quest firstQuest;

	/**
	 * Create a new player class and load it automatically
	 *
	 * @param className
	 */
	protected ClassBase(final String className) {

		setHeader("Welcome to the main class settings file!");

		// This will copy your rpg-class.yml file from src/main/resources in your source code
		// into classes/ folder and rename it to the specific class name as well as load values at the end
		loadConfiguration("rpg-class.yml", "classes/" + className + (!className.endsWith(".yml") ? ".yml" : ""));

		byName.put(className, this);
	}

	/**
	 * Called automatically when you call {@link #loadConfiguration(String, String)} above
	 */
	@Override
	protected void onLoadFinish() {
		maxTier = getInteger("Max_Tier");
		damageModifier = getDoubleSafe("Damage_Modifier"); // Supports for integers such as "1" as well as doubles "1.0"
		hitCooldown = getDoubleSafe("Hit_Cooldown");
		icon = getMaterial("Icon");
		bossThrowReduction = getInteger("Boss_Throw_Reducation");
		firstQuest = Quest.getByName(getString("First_Quest"));
	}

	/**
	 *  Give some of this class properties to the player
	 *  See https://minecraft.gamepedia.com/Attribute
	 *
	 * @param tier
	 * @param player
	 */
	public void applyFor(final int tier, final Player player) {
		Common.tell(player, "Applying changes from your class " + getName());

		// Edit: Use these two lines for maximum MC compatibility
		CompAttribute.GENERIC_ATTACK_DAMAGE.set(player, damageModifier);
		CompAttribute.GENERIC_ATTACK_SPEED.set(player, hitCooldown);
		//player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damageModifier);
		//player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(hitCooldown);
	}

	/**
	 * Return the name of this class
	 *
	 * @return
	 */
	public final String getName() {
		return getFileName().replace(".yml", "");
	}

	// --------------------------------------------------------------------------------------------------------------
	// Events called automatically if the related player is having this class
	// See ClassListener
	// --------------------------------------------------------------------------------------------------------------


	/**
	 * Called if the given player has this class and attacks someone
	 *
	 * @param tier
	 * @param attacker
	 * @param victim
	 * @param event
	 */

	public void onAttack(final int tier, final Player attacker, final LivingEntity victim, final EntityDamageByEntityEvent event) {
	}

	/**
	 * Called if something attacks a player that has this class
	 *
	 * @param tier
	 * @param attacker
	 * @param victim
	 * @param event
	 */

	public void onDamaged(final int tier, final LivingEntity attacker, final Player victim, final EntityDamageByEntityEvent event) {
	}


	/*
	/**
	 * Called if the player having this class breaks a block in survival
	 *
	 * @param tier
	 * @param player
	 * @param block
	 * @param event
	 */

	public void onBreakBlock(final int tier, final Player player, final Block block, final BlockBreakEvent event) {
	}

	// --------------------------------------------------------------------------------------------------------------
	// Methods related to upgrading class tiers
	// --------------------------------------------------------------------------------------------------------------

	/**
	 * @see #upgradeToNextTier(Player)
	 *
	 * @param player
	 * @return
	 */
	public boolean upgradeToNextTier(final Player player) {
		return upgradeToNextTier(player, false);
	}

	/*
	/**
	 * If the player have not reached the {@link #maxTier} and he {@link #canUpgrade(Player, PlayerCache, int)}
	 * to the next tier, we save the next tier to the data file and call {@link #onUpgrade(Player, int)} method
	 * as well as send him a congratulation message
	 *
	 * @param player
	 * @param force should we force the player to the next tier even if the {@link #canUpgrade(Player, PlayerCache, int)} method fails?
	 * @return
	 */
	public boolean upgradeToNextTier(final Player player, final boolean force) {
		final PlayerCache cache = PlayerCache.getCache(player);
		final int currentTier = cache.getClassTier(this);

		if (currentTier >= maxTier)
			return false;

		final int nextTier = currentTier + 1;

		if (canUpgrade(player, cache, nextTier) || force) {
			if (cache.hasClass(this))
				cache.setClassTier(this, nextTier);
			else
				cache.addClass(this);

			onUpgrade(player, nextTier);

			Common.tellNoPrefix(player, "&8[&c!&8] &6You have now become " + getName() + " " + MathUtil.toRoman(nextTier) + "!");
			return true;
		}

		return false;
	}

	/**
	 * Override this to return if the player can upgrade this class to the next tier
	 *
	 * @param player
	 * @param cache
	 * @param nextTier
	 * @return
	 */
	protected boolean canUpgrade(final Player player, final PlayerCache cache, final int nextTier) {
		return false;
	}

	/**
	 * Override this to execute additional actions after the player has successfuly leveled up
	 *
	 * @param player
	 * @param nextTier
	 */
	protected void onUpgrade(final Player player, final int nextTier) {
	}

	// --------------------------------------------------------------------------------------------------------------
	// Static access
	// --------------------------------------------------------------------------------------------------------------

	/**
	 * Get this class by its name
	 *
	 * @param name
	 * @return the class, or null if not found
	 */
	public static ClassBase getByName(final String name) {
		for (final ClassBase loadedClass : byName.values())
			if (loadedClass.getName().equals(name))
				return loadedClass;

		return null;
	}

	/**
	 * Return an unmodifiable collection of all loaded classes
	 *
	 * @return
	 */
	public static Collection<ClassBase> getClasses() {
		return Collections.unmodifiableCollection(byName.values());
	}

	/**
	 * Return an unmodifiable set of all class names
	 * @return
	 */
	public static Set<String> getClassNames() {
		return Collections.unmodifiableSet(byName.keySet());
	}
}
