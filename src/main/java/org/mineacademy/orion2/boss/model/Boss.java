package org.mineacademy.orion2.boss.model;

import lombok.AccessLevel;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompAttribute;
import org.mineacademy.fo.remain.CompMetadata;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.orion2.api.event.BossSpawnEvent;
import org.mineacademy.orion2.boss.BossAttribute;
import org.mineacademy.orion2.boss.BossEquipment;
import org.mineacademy.orion2.boss.BossPotionEffect;
import org.mineacademy.orion2.boss.npc.NPCSirRichard;
import org.mineacademy.orion2.boss.skill.BossSkill;

import javax.annotation.Nullable;
import java.util.*;

/**
 * The core model class for our Boss system, currently completely
 * customizable only within your plugin. If you would like to configure this
 * from your config, see ClassBase.
 */
@Setter(AccessLevel.PROTECTED)
public abstract class Boss {

	/**
	 * The persistent NBT tag we use to mark an entity as our Boss
	 */
	public static final String BOSS_TAG = "OrionBoss";

	/**
	 * The persistent NBT tag we use to mark a spawner as our Boss
	 */
	public static final String BOSS_SPAWNER_TAG = "OrionBossSpawner";

	/**
	 * Stores active bosses by their name, so they are singletons
	 */
	private static final Map<String, Boss> byName = new HashMap<>();

	// --------------------------------------------------------------------------------------------------------------
	// Place your quests below, make sure to give them protected constructor to prevent accidental initializing
	// --------------------------------------------------------------------------------------------------------------

	public static final Boss WARRIOR = new BossWarrior();

	public static final Boss SIR_RICHARD = new NPCSirRichard();

	// --------------------------------------------------------------------------------------------------------------

	// Below are the options you must specify when making a new boss class

	/**
	 * The name of this boss
	 */
	private final String name;

	/**
	 * The entity type that this boss represents in Minecraft (client can only render existing entities)
	 */
	private final EntityType type;

	// Below are the options you _can_ specify when making a new boss class in the constructor

	/**
	 * The custom visible boss name, colors & are supported
	 */
	private String customName;

	/**
	 * The custom boss health. We use Double instead of double since it can be null so
	 * that when no health is specified we do not modify it
	 */
	private Double health;

	/**
	 * Boss equipment
	 */
	private BossEquipment equipment;

	/**
	 * How much experience boss drops on death?
	 */
	private Integer droppedExp;

	/**
	 * The entity that this boss rides, if any
	 */
	private EntityType passenger;

	/**
	 * Special {@link org.bukkit.attribute.Attribute} for this boss
	 */
	private List<BossAttribute> attributes = new ArrayList<>();

	/**
	 * Special {@link PotionEffectType} for this boss
	 */
	private List<BossPotionEffect> potionEffects = new ArrayList<>();

	/**
	 * Special skills for this boss
	 */
	private StrictList<BossSkill> skills = new StrictList<>();

	/**
	 * Indicates if the Boss can appear as a result of {@link org.mineacademy.orion2.boss.BossTimedTask}
	 */
	private boolean canSpawnRandomly = true;

	/**
	 * Create a new boss and register it in {@link #byName}
	 *
	 * @param name
	 * @param type
	 */
	protected Boss(final String name, final EntityType type) {
		Valid.checkBoolean(type.isAlive() && type.isSpawnable(), "The boss type must be alive and spawnable!"); // Edit: I placed it here so that you see errors faster

		this.name = name;
		this.type = type;

		byName.put(name, this);
	}

	/**
	 * Spawn this boss at the given location and apply all properties
	 * in this class for him
	 *
	 * @param location
	 */
	public final void spawn(Location location) {

		final BossSpawnEvent event = new BossSpawnEvent(this, location);

		if (!Common.callEvent(event))
			return;

		location = event.getLocation();

		final LivingEntity entity = makeEntity(location);

		Remain.setCustomName(entity, name);

		if (health != null)
			entity.setHealth(health);

		if (this.equipment != null) {
			final EntityEquipment equipment = entity.getEquipment();

			equipment.setHelmet(this.equipment.getHelmet().build().makeSurvival());
			equipment.setChestplate(this.equipment.getChestplate().build().makeSurvival());
			equipment.setLeggings(this.equipment.getLeggings().build().makeSurvival());
			equipment.setBoots(this.equipment.getBoots().build().makeSurvival());
		}

		if (attributes != null)
			for (final BossAttribute attribute : attributes)
				attribute.getAttribute().set(entity, attribute.getValue());

		if (potionEffects != null)
			for (final BossPotionEffect potionEffect : potionEffects)
				entity.addPotionEffect(new PotionEffect(potionEffect.getPotion(), Integer.MAX_VALUE, potionEffect.getAmplifier()));

		if (passenger != null) {
			final LivingEntity spawnedPassenger = (LivingEntity) location.getWorld().spawnEntity(location, passenger);

			spawnedPassenger.setPassenger(entity);
		}

		// Mark that boss with our persistent NBT tag
		CompMetadata.setMetadata(entity, BOSS_TAG, name);

		if (entity.isValid() && !entity.isDead())
			onSpawn(location, entity);
	}

	/**
	 * Create a new living entity on the certain location
	 *
	 * @param location
	 * @return
	 */
	protected LivingEntity makeEntity(final Location location) {
		return (LivingEntity) location.getWorld().spawnEntity(location, type);
	}

	// --------------------------------------------------------------------------------------------------------------
	// Events called automatically for this Boss
	// --------------------------------------------------------------------------------------------------------------

	/**
	 * Called when the Boss dies
	 *
	 * @param killer the player killer or null
	 * @param bossEntity the boss entity
	 * @param event
	 */
	public void onDeath(@Nullable final Player killer, final LivingEntity bossEntity, final EntityDeathEvent event) {
	}

	/**
	 * Called when the Boss attacks something
	 *
	 * @param bossAttacker
	 * @param victim
	 * @param event
	 */
	public void onAttack(final LivingEntity bossAttacker, final LivingEntity victim, final EntityDamageByEntityEvent event) {
	}

	/**
	 * Called when something attacks the Boss
	 *
	 * @param attacker
	 * @param bossVictim
	 * @param event
	 */
	public void onDamaged(final LivingEntity attacker, final LivingEntity bossVictim, final EntityDamageByEntityEvent event) {
	}

	/**
	 * Called automatically from Boss timed task
	 *
	 * @param bossEntity
	 */
	public void onTick(final LivingEntity bossEntity) {
	}

	/**
	 * Called automatically when a player right clicks this Boss
	 *
	 * @param player
	 * @param boss
	 * @param event
	 */
	public void onRightClick(final Player player, final LivingEntity boss, final PlayerInteractEntityEvent event) {
	}

	/**
	 * Called automatically after the Boss has been spawned
	 *
	 * @param location
	 * @param entity
	 */
	protected void onSpawn(final Location location, final LivingEntity entity) {
	}

	// --------------------------------------------------------------------------------------------------------------
	// Final methods below
	// --------------------------------------------------------------------------------------------------------------

	/**
	 * Get the boss name
	 *
	 * @return
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Get the boss entity type
	 *
	 * @return
	 */
	public final EntityType getType() {
		return type;
	}

	// --------------------------------------------------------------------------------------------------------------
	// Configurable boss attributes
	// --------------------------------------------------------------------------------------------------------------

	/**
	 * Get the boss custom name, see {@link LivingEntity#getCustomName()}
	 *
	 * @return
	 */
	public final String getCustomName() {
		return Common.colorize(customName);
	}

	/**
	 * Get the boss health, see {@link LivingEntity#getName()}
	 * @return
	 */
	public final Double getHealth() {
		return health;
	}

	/**
	 * Get equipment for this boss
	 *
	 * @return
	 */
	public final BossEquipment getEquipment() {
		return equipment;
	}

	/**
	 * Sets equipment for this boss
	 *
	 * @param helmet
	 * @param chestplate
	 * @param leggings
	 * @param boots
	 */
	protected final void setEquipment(final ItemCreator.ItemCreatorBuilder helmet,
									  final ItemCreator.ItemCreatorBuilder chestplate,
									  final ItemCreator.ItemCreatorBuilder leggings,
									  final ItemCreator.ItemCreatorBuilder boots) {
		this.equipment = new BossEquipment(helmet, chestplate, leggings, boots);
	}

	/**
	 * Get dropped experience for this boss
	 *
	 * @return
	 */
	public final Integer getDroppedExp() {
		return droppedExp;
	}

	/**
	 * Add new attribute for this boss
	 *
	 * @param attribute
	 * @param value
	 */
	protected final void addAttribute(final CompAttribute attribute, final double value) {
		attributes.add(new BossAttribute(attribute, value));
	}

	/**
	 * Add new potion effect for this boss
	 *
	 * @param potion
	 */
	protected final void addPotionEffect(final PotionEffectType potion) {
		addPotionEffect(potion, 0);
	}

	/**
	 * Add new potion effect for this boss
	 *
	 * @param potion
	 * @param amplifier
	 */
	protected final void addPotionEffect(final PotionEffectType potion, final int amplifier) {
		potionEffects.add(new BossPotionEffect(potion, amplifier));
	}

	/**
	 * Get the entity that rides this boss
	 *
	 * @return
	 */
	public final EntityType getPassenger() {
		return passenger;
	}

	/**
	 * Add a new skill for this boss
	 *
	 * @param skill
	 */
	public final void addSkill(final BossSkill skill) {
		skills.add(skill);
	}

	/**
	 * Return unmodifiable skill list for this boss
	 *
	 * @return
	 */
	public final List<BossSkill> getSkills() {
		return Collections.unmodifiableList(skills.getSource());
	}

	public final boolean canSpawnRandomly() {
		return canSpawnRandomly;
	}

	/**
	 * Return true if the given object is a boss having the same name
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(final Object obj) {
		return obj instanceof Boss && ((Boss) obj).getName().equals(getName());
	}

	// --------------------------------------------------------------------------------------------------------------
	// Static access
	// --------------------------------------------------------------------------------------------------------------

	/**
	 * If the given entity has our NBT tag, see {@link #BOSS_TAG},
	 * then return the boss if we have it registered.
	 *
	 * @param entity
	 * @return
	 */
	public static Boss findBoss(final Entity entity) {
		if (!(entity instanceof LivingEntity))
			return null;

		final String bossName = CompMetadata.getMetadata(entity, Boss.BOSS_TAG);

		return bossName != null ? findBoss(bossName) : null;
	}

	/**
	 * Get the boss by its name
	 *
	 * @param name
	 * @return
	 */
	public static Boss findBoss(final String name) {
		Valid.checkNotNull(name);

		for (final Boss boss : byName.values())
			if (boss.getName().equals(name))
				return boss;

		return null;
	}

	/**
	 * Get all bosses
	 *
	 * @return
	 */
	public static Collection<Boss> getBosses() {
		return Collections.unmodifiableCollection(byName.values());
	}

	/**
	 * Get all bosses names
	 *
	 * @return
	 */
	public static Set<String> getBossesNames() {
		return Collections.unmodifiableSet(byName.keySet());
	}
}
