package org.mineacademy.orion2.mysql;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.database.SimpleFlatDatabase;
import org.mineacademy.orion2.PlayerCache;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrionDatabase extends SimpleFlatDatabase<PlayerCache> {

	@Getter
	private final static OrionDatabase instance = new OrionDatabase();

	@Override
	protected void onLoad(final SerializedMap map, final PlayerCache data) {
		final ChatColor color = map.get("Color", ChatColor.class);

		if (color != null)
			data.setColor(color);

		// We must make the Bukkit API methods run on the main thread by synchronizing them
		/*Common.runLater(() -> {
			Bukkit.getWorld("world").getBlockAt(0,0,0).setType(Material.FLOWER_POT);
		});*/
	}

	@Override
	protected SerializedMap onSave(final PlayerCache data) {
		final SerializedMap map = new SerializedMap();

		if (data.getColor() != null)
			map.put("Color", data.getColor());

		return map;
	}

	/*@Override
	protected int getExpirationDays() {
		return 90; // 90 is the default value
	}*/
}

