package org.mineacademy.orion2.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.mineacademy.orion2.PlayerCache;

/**
 * This class is the main API class for the Orion plugin.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrionAPI {

	/**
	 * Return the {@link OrionCache} for the given player
	 * containing various Orion information stored in the
	 * data.db file.
	 *
	 * @param player the player
	 * @return the player cache
	 */
	public static OrionCache getCache(final Player player) {
		return PlayerCache.getCache(player);
	}
}

