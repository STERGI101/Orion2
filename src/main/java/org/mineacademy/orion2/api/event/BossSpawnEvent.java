package org.mineacademy.orion2.api.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.mineacademy.orion2.boss.model.Boss;

@Getter
@Setter
public final class BossSpawnEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Boss boss;
	private Location location;
	private boolean cancelled;

	public BossSpawnEvent(final Boss boss, final Location location) {
		this.boss = boss;
		this.location = location;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}

