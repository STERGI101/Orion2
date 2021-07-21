package org.mineacademy.orion2.hook;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.TextEffect;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.awt.*;

public final class EffectLibHook implements Listener {

	private final EffectManager effectManager;

	public EffectLibHook() {
		this.effectManager = new EffectManager(SimplePlugin.getInstance());
	}

	public void shutdown() {
		effectManager.dispose();
	}

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final de.slikey.effectlib.effect.TextEffect effect = new TextEffect(effectManager);

		effect.text = "Hey " + event.getPlayer().getName();
		effect.particle = Particle.VILLAGER_HAPPY;
		effect.period = 20;
		effect.iterations = -1;
		effect.font = new Font("Arial", Font.BOLD, 30);

		effect.setLocation(event.getPlayer().getLocation().add(0, 2, 0));
		effect.start();

		Common.runTimer(5, () -> {
			if (!event.getPlayer().isOnline())
				effect.cancel();
		});

		// Add a callback to the effect
		effect.callback = () -> {
			// Run this code when the effect is cancelled
		};
	}
}
