package org.mineacademy.orion2;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.MathUtil;
import org.mineacademy.fo.TimeUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.collection.expiringmap.ExpiringMap;
import org.mineacademy.fo.model.ConfigSerializable;
import org.mineacademy.fo.settings.YamlSectionConfig;
import org.mineacademy.orion2.api.OrionCache;
import org.mineacademy.orion2.classes.model.ClassBase;
import org.mineacademy.orion2.quest.model.Quest;
import org.mineacademy.orion2.rank.model.Rank;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
public final class PlayerCache extends YamlSectionConfig implements OrionCache  {

	private static final ExpiringMap<UUID, PlayerCache> cacheMap = ExpiringMap.builder().expiration(30, TimeUnit.MINUTES).build();

	private final UUID uuid;

	private Set<ClassCache> classes;
	private ActiveQuestCache activeQuest;
	private ChatColor color;
	private int level;
	private Rank rank;
	private int playerKills;
	private int deaths;

	protected PlayerCache(final UUID uuid) {

		// This will prepend this cache with the players unique id just like you use pathPrefix in SimpleSettings
		super(uuid.toString());

		this.uuid = uuid;

		// Load our player cache from the disk however do not use any default file
		// from our source code
		loadConfiguration(NO_DEFAULT, "data.db");
	}

	@Override
	protected void onLoadFinish() {
		checkForUpgrades();

		classes = getSetSafe("Classes", ClassCache.class);
		activeQuest = get("Active_Quest", ActiveQuestCache.class);
		color = get("Color", ChatColor.class);
		level = getInteger("Level", 1);
		rank = get("Rank", Rank.class);
		playerKills = getInteger("Player_Kills", 0);
		deaths = getInteger("Deaths", 0);
	}

	private void checkForUpgrades() {
		convertMapList("Classes", "Completed_Quests", String.class, CompletedQuestCache.class,
				questName -> new CompletedQuestCache(Quest.getByName(questName), 0, 0));
	}

	@Override
	public void setColor(final ChatColor color) {
		this.color = color;

		save("Color", color.name());
	}

	@Override
	public void setLevel(final int level) {
		this.level = level;

		save("Level", level);
	}

	public Rank getRank() {
		return Common.getOrDefault(rank, Rank.getFirstRank());
	}

	public void setRank(final Rank rank) {
		this.rank = rank;

		save("Rank", rank.getName());
	}

	@Override
	public void increasePlayerKills() {
		this.playerKills++;

		save("Player_Kills", this.playerKills);
	}

	@Override
	public void increaseDeaths() {
		this.deaths++;

		save("Deaths", this.deaths);
	}

	// --------------------------------------------------------------------------------------------------------------
	// Classes manipulation
	// --------------------------------------------------------------------------------------------------------------

	// Return the player tier of the given class, or 0 if the player does not have this class
	public int getClassTier(final ClassBase classBase) {
		final ClassCache classCache = getClass(classBase);

		return classCache != null ? classCache.getTier() : 0;
	}

	// Return if the player has the given class
	public boolean hasClass(final ClassBase classBase) {
		return getClass(classBase) != null;
	}

	// Return the player class data holding a tier and other information about his class
	// The ClassBase is just a model for classes in general
	// whereas PlayerClass is bound to a certain player storing his data
	public ClassCache getClass(final ClassBase classBase) {
		for (final ClassCache classCache : classes)
			if (classCache.getClassBase().getName().equals(classBase.getName()))
				return classCache;

		return null;
	}

	// Add a new class to the player, failing if he already has one
	public void addClass(final ClassBase classBase) {
		Valid.checkNotNull(classBase, "Class must not be null");
		Valid.checkBoolean(!hasClass(classBase), "Player already have " + classBase.getName() + " class!");

		classes.add(new ClassCache(classBase, 1));
		save("Classes", classes);
	}

	// Remove a class from the player, failing if he already has one
	public void removeClass(final ClassBase classBase) {
		Valid.checkNotNull(classBase, "Class must not be null");

		final ClassCache classCache = getClass(classBase);
		Valid.checkNotNull(classCache, "Player does not have " + classBase.getName() + " class!");

		classes.remove(classCache);
		save("Classes", classes);
	}

	// Clear all classes
	public void removeClasses() {
		classes.clear();

		save("Classes", null);
	}

	// Updates the tier for the players class, or removes it if you give a 0 tier
	// NB: The player must have the given class
	public void setClassTier(final ClassBase classBase, final int tier) {
		Valid.checkNotNull(classBase, "Class must not be null");
		Valid.checkBoolean(tier >= 0, "Tier for " + classBase.getName() + " must be 0 or greater");

		if (tier == 0)
			removeClass(classBase);

		else {
			final ClassCache classCache = getClass(classBase);
			Valid.checkNotNull(classCache, "Player does not have " + classBase.getName() + " class!");

			classCache.setTier(tier);
			save("Classes", classes);
		}
	}

	// --------------------------------------------------------------------------------------------------------------
	// Quest manipulation
	// --------------------------------------------------------------------------------------------------------------

	// Find the last not completed quest starting from the first quest of that given player
	// Returns null if all quests have been completed
	public Quest getNextQuest(final ClassBase classBase) {
		Quest next = classBase.getFirstQuest();

		while (next != null && hasCompletedQuest(next, classBase))
			next = next.getNext(classBase);

		return next;
	}

	// Return true if the player has completed that quest for the given class (Classes may have different quests)
	public boolean hasCompletedQuest(final Quest quest, final ClassBase classBase) {
		return getCompletedQuest(quest, classBase) != null;
	}

	// Return completed data about the given quest in the given class
	public CompletedQuestCache getCompletedQuest(final Quest quest, final ClassBase classBase) {
		Valid.checkNotNull(quest, "Quest cannot be null");
		Valid.checkNotNull(classBase, "Class for quest " + quest.getName() + " cannot be null");

		final ClassCache classCache = getClass(classBase);

		if (classCache != null)
			for (final CompletedQuestCache completedQuest : classCache.getCompletedQuests())
				if (completedQuest.getQuest().equals(quest))
					return completedQuest;
		return null;
	}

	// Return true if the player has the given active quest
	public boolean hasActiveQuest(final Quest quest) {
		Valid.checkNotNull(quest, "Quest to check cannot be null");

		return activeQuest != null && activeQuest.getQuest().equals(quest);
	}

	// Stops the active quest and saves it to the list of completed quests
	public void completeQuest() {
		Valid.checkNotNull(activeQuest, "Player is not doing any quest right now!");

		final Quest quest = activeQuest.getQuest();
		final ClassCache classCache = getClass(activeQuest.getClassBase());

		Valid.checkNotNull(classCache, "Cannot find player class associated with quest " + quest.getName());

		classCache.getCompletedQuests().add(new CompletedQuestCache(quest, activeQuest.getStartDate(), System.currentTimeMillis()));
		save("Classes", classes);

		stopActiveQuest();
	}

	// Remove a quest from player class, failing if player did not complete that quest for the given class
	public void removeQuest(final Quest quest, final ClassBase classBase) {
		Valid.checkNotNull(quest, "Quest must not be null");
		Valid.checkNotNull(classBase, "Class must not be null");

		final ClassCache classCache = getClass(classBase);
		Valid.checkNotNull(classCache, "Player does not have " + classBase.getName() + " class!");

		final CompletedQuestCache completedQuest = getCompletedQuest(quest, classBase);
		Valid.checkBoolean(classCache.getCompletedQuests().contains(completedQuest), "Player did not complete " + quest.getName() + " for his " + classBase.getName() + " class!");

		classCache.getCompletedQuests().remove(completedQuest);
		save("Classes", classes);
	}

	// Updates the active quest data
	// Edit: I changed the name to updateActiveQuestData instead of updateCurrentQuestData for better integrity
	public void updateActiveQuestData(final Object data) {
		Valid.checkNotNull(activeQuest, "Player is not doing any quest right now!");

		activeQuest.setData(data);
		save("Active_Quest", activeQuest);
	}

	// If the player does not have any active quest, puts a new active quest into the file
	// This does NOT handle any rewards, you should handle those in your quest class
	public void startQuest(final Quest quest, final ClassBase classBase) {
		Valid.checkNotNull(quest, "Active quest cannot be null");
		Valid.checkNotNull(classBase, "Class running quest " + quest.getName() + " cannot be null");
		Valid.checkBoolean(activeQuest == null, "Player is already having an active quest " + activeQuest); // Edit: I added one more check

		activeQuest = new ActiveQuestCache(quest, classBase, System.currentTimeMillis(), null);

		save("Active_Quest", activeQuest);
	}

	// Stops the players active quest
	public void stopActiveQuest() {
		Valid.checkNotNull(activeQuest, "Player is not doing any quest right now!"); // Edit: I added one more check
		activeQuest = null;

		save("Active_Quest", null);
	}

	// A shortcut for getting the active quest data
	public Object getActiveQuestData() {
		return activeQuest != null ? activeQuest.getData() : null;
	}

	// --------------------------------------------------------------------------------------------------------------
	// Static methods below
	// --------------------------------------------------------------------------------------------------------------

	public static PlayerCache getCache(final Player player) {
		return getCache(player.getUniqueId());
	}

	public static PlayerCache getCache(final UUID uuid) {
		PlayerCache cache = cacheMap.get(uuid);

		if (cache == null) {
			cache = new PlayerCache(uuid);

			cacheMap.put(uuid, cache);
		}

		return cache;
	}

	public static void clearAllData() {
		cacheMap.clear();
	}

	// --------------------------------------------------------------------------------------------------------------
	// Own classes
	// --------------------------------------------------------------------------------------------------------------

	// This file holds information about the players class
	@Getter
	@AllArgsConstructor
	public static final class ClassCache implements ConfigSerializable {

		// The class the player has
		private final ClassBase classBase;

		// The tier of the players class
		@Setter(AccessLevel.PRIVATE)
		private int tier;

		// List of all completed quests
		private final Set<CompletedQuestCache> completedQuests;

		private ClassCache(final ClassBase classBase, final int tier) {
			this(classBase, tier, new HashSet<>());
		}

		public String getTierRoman() {
			return MathUtil.toRoman(tier);
		}

		// This method does the magic when you use save() method above
		// and converts this class into a hash map you can save in your yml files
		@Override
		public SerializedMap serialize() {
			final SerializedMap map = new SerializedMap();

			map.put("Name", classBase.getName());
			map.put("Tier", tier);
			map.put("Completed_Quests", completedQuests);

			return map;
		}

		public static ClassCache deserialize(final SerializedMap map) {
			final String name = map.getString("Name");
			Valid.checkNotNull(name, "Malformed class settings, lacking the Name key: " + map);

			final ClassBase base = ClassBase.getByName(name);
			Valid.checkNotNull(base, "Could not find class named " + name + ". Available: " + ClassBase.getClassNames());

			final Integer tier = map.getInteger("Tier");
			Valid.checkNotNull(base, "Class " + name + " lacks the Tier key!");

			final Set<CompletedQuestCache> completedQuests = map.getSetSafe("Completed_Quests", CompletedQuestCache.class);

			return new ClassCache(base, tier, completedQuests);
		}
	}

	// Holds information about a completed quest
	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public final static class CompletedQuestCache implements ConfigSerializable {

		// The quest
		private final Quest quest;

		// The timestamp in milliseconds (since 1970) when this quest was started
		private final long startDate;

		// The timestamp in milliseconds (since 1970) when this quest was completed
		private final long completionDate;

		public long getDurationMillis() {
			return completionDate - startDate;
		}

		// 1m 20s
		public String getDurationFormatted() {
			return TimeUtil.formatTimeShort(getDurationMillis() / 1000);
		}

		// This method does the magic when you use save() method above
		// and converts this java class into a hash map you can save in your yml files
		@Override
		public SerializedMap serialize() {
			final SerializedMap map = new SerializedMap();

			map.put("Name", quest.getName());
			map.put("Start_Date", startDate);
			map.put("Completion_Date", completionDate);

			return map;
		}

		public static CompletedQuestCache deserialize(final SerializedMap map) {
			final Quest quest = Quest.getByName(map.getString("Name"));
			Valid.checkNotNull("Cannot add a non existing quest to completed quests: " + map.getString("Name"));

			final long startDate = map.getLong("Start_Date", 0L);
			final long completionDate = map.getLong("Completion_Date", 0L);

			return new CompletedQuestCache(quest, startDate, completionDate);
		}

		@Override
		public boolean equals(final Object obj) {
			return obj instanceof CompletedQuestCache && ((CompletedQuestCache) obj).getQuest().equals(this.quest);
		}
	}

	// Holds information about the players ongoing quest
	@Getter
	@AllArgsConstructor
	public final static class ActiveQuestCache implements ConfigSerializable {

		// The quest
		private final Quest quest;

		// Class associated with this quest
		private final ClassBase classBase;

		// Time time in milliseconds when this quest was started
		private final long startDate;

		// Quest data (any kind)
		@Setter(AccessLevel.PRIVATE)
		private Object data;

		public String getCompletion(final Player player) {
			return quest.getCompletion(player, data);
		}

		// This method does the magic when you use save() method above
		// and converts this class into a hash map you can save in your yml files
		@Override
		public SerializedMap serialize() {
			final SerializedMap map = new SerializedMap();

			map.put("Name", quest.getName());
			map.put("Class", classBase.getName());
			map.put("Start_Date", startDate);

			if (data != null)
				map.put("Data", data);

			return map;
		}

		public static ActiveQuestCache deserialize(final SerializedMap map) {
			final Quest quest = Quest.getByName(map.getString("Name"));
			final ClassBase classBase = ClassBase.getByName(map.getString("Class"));
			final long startDate = map.getLong("Start_Date", 0L);
			final Object data = map.getObject("Data");

			return new ActiveQuestCache(quest, classBase, startDate, data);
		}
	}
}
