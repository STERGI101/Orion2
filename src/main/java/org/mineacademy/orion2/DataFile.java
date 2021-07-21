package org.mineacademy.orion2;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.fo.settings.YamlConfig;
import org.mineacademy.orion2.classes.model.ClassBase;
import org.mineacademy.orion2.quest.model.Quest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public final class DataFile extends YamlConfig {

	@Getter
	private static final DataFile instance = new DataFile();

	private DataFile() {
		loadConfiguration(NO_DEFAULT, "data.db");
	}

	// --------------------------------------------------------------------------------------------------------------
	// Statistics and quests
	// --------------------------------------------------------------------------------------------------------------

	public PlayerStatisticData getTopStatistic(final Function<PlayerCache, Integer> statisticGetter) {
		final List<PlayerStatisticData> stats = getStatistics(statisticGetter);

		return stats.isEmpty() ? null : stats.get(0);
	}

	public List<PlayerStatisticData> getStatistics(final Function<PlayerCache, Integer> statisticGetter) {
		final List<PlayerStatisticData> stats = new ArrayList<>();

		for (final PlayerCache cache : getCaches()) {
			final PlayerStatisticData stat = new PlayerStatisticData(cache.getUuid(), statisticGetter.apply(cache));

			stats.add(stat);
		}

		Collections.sort(stats, (first, second) -> Long.compare(second.getValue(), first.getValue()));

		return stats;
	}

	public List<PlayerQuestData> getCompletedQuests(final ClassBase base, final Quest quest) {
		final List<PlayerQuestData> questsData = new ArrayList<>();

		for (final PlayerCache cache : getCaches())
			if (cache.hasClass(base))
				for (final PlayerCache.CompletedQuestCache completedQuest : cache.getClass(base).getCompletedQuests())
					if (completedQuest.getQuest() == quest) {
						final PlayerQuestData questData = new PlayerQuestData(cache.getUuid(), completedQuest);

						questsData.add(questData);
					}

		Collections.sort(questsData, (first, second) -> Long.compare(second.getQuest().getDurationMillis(), first.getQuest().getDurationMillis()));

		return questsData;
	}

	// --------------------------------------------------------------------------------------------------------------
	// Helpers
	// --------------------------------------------------------------------------------------------------------------

	private List<PlayerCache> getCaches() {
		final List<PlayerCache> caches = new ArrayList<>();

		for (final String uuid : getMap("").keySet()) {
			final PlayerCache cache = PlayerCache.getCache(UUID.fromString(uuid));

			caches.add(cache);
		}

		return caches;
	}

	// --------------------------------------------------------------------------------------------------------------
	// Own classes
	// --------------------------------------------------------------------------------------------------------------

	@Getter
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class PlayerStatisticData {

		private final UUID uuid;
		private final int value; // for example: death count

		public OfflinePlayer getPlayer() {
			return Remain.getOfflinePlayerByUUID(uuid);
		}
	}

	@Getter
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class PlayerQuestData {

		private final UUID uuid;
		private final PlayerCache.CompletedQuestCache quest;

		public OfflinePlayer getPlayer() {
			return Remain.getOfflinePlayerByUUID(uuid);
		}
	}

}