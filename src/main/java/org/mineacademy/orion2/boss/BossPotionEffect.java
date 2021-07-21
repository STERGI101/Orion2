package org.mineacademy.orion2.boss;

import lombok.Data;
import org.bukkit.potion.PotionEffectType;

@Data
public class BossPotionEffect {

	private final PotionEffectType potion;
	private final int amplifier;
}
