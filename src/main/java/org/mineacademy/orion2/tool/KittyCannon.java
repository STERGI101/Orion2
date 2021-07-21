package org.mineacademy.orion2.tool;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.EntityUtil;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.tool.Tool;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompParticle;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.orion2.util.OrionUtil;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KittyCannon extends Tool {

	@Getter
	private final static Tool instance = new KittyCannon();
	@Override
	public ItemStack getItem() {
		return ItemCreator.of(CompMaterial.STICK,
				"&dKitty Cannon",
				"",
				"Rightclick air to launch",
				"some flying kitties...",
				"PS: They will explode on impact!").glow(true)
				.build().make();
	}

	@Override
	protected void onBlockClick(PlayerInteractEvent event) {
		if(event.getAction() != Action.RIGHT_CLICK_AIR)
			return;

		Player player = event.getPlayer();

		if(!OrionUtil.checkKittyArrow(player, event))
			return;

		Cat cat = player.getWorld().spawn(player.getEyeLocation(), Cat.class);

		cat.setVelocity(player.getEyeLocation().getDirection().multiply(2.0D));

		CompSound.SUCCESSFUL_HIT.play(player);

		EntityUtil.trackFlying(cat,() -> {
			CompParticle.END_ROD.spawn(cat.getLocation());
		});

		EntityUtil.trackFalling(cat, () -> {
			cat.remove();
			cat.getWorld().createExplosion(cat.getLocation(), 4F );

			CompSound.ANVIL_LAND.play(cat.getLocation());
		});
	}

	@Override
	protected boolean ignoreCancelled() {
		return false;
	}
}
