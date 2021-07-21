package org.mineacademy.orion2.enchant;

import lombok.Getter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mineacademy.fo.model.SimpleEnchantment;
import org.mineacademy.fo.remain.CompSound;

public class HideEnchant extends SimpleEnchantment {

	@Getter
	private static final Enchantment instance = new HideEnchant();

	private HideEnchant() {
		super("Hide", 5);
	}

	@Override
	protected void onShoot(int level, LivingEntity shooter, ProjectileLaunchEvent event) {
		if(!(shooter instanceof Player))
			return;

		Player player = (Player) shooter;
		CompSound.ANVIL_LAND.play(player);
	}

	@Override
	protected void onHit(int level, LivingEntity shooter, ProjectileHitEvent event) {
		if(!(event.getHitEntity() instanceof LivingEntity))
			return;

		LivingEntity hitEntity = (LivingEntity) event.getHitEntity();

		hitEntity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, (level * 3) *20,0));
	}


}
