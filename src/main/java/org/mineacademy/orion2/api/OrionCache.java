package org.mineacademy.orion2.api;

import org.bukkit.ChatColor;

/**
 * Represents the basic player cache with information
 * the Orion plugin stores about each online player
 * when they join. This information can be found
 * in the data.db file.
 *
 * Setting new information will automatically
 * save them into the data.db file.
 */
public interface OrionCache {

	/**
	 * Get the chat color of the player
	 *
	 * @return the color, or null if none
	 */
	ChatColor getColor();

	/**
	 * Set a new color for the player
	 *
	 * @param color the new color
	 */
	void setColor(ChatColor color);

	/**
	 * Get the player's level in the Orion levelling system
	 *
	 * @return the player's level, or 1 if they don't have any
	 */
	int getLevel();

	/**
	 * Set a new player level in the Orion levelling system
	 *
	 * @param level the new level, at least 1
	 */
	void setLevel(int level);

	/**
	 * Return how many players the player had killed
	 *
	 * @return the player kills
	 */
	int getPlayerKills();

	/**
	 * Increase the player kills by 1
	 */
	void increasePlayerKills();

	/**
	 * Return how many times the player had died
	 *
	 * @return the player deaths
	 */
	int getDeaths();

	/**
	 * Increase the player deaths by 1
	 */
	void increaseDeaths();
}

