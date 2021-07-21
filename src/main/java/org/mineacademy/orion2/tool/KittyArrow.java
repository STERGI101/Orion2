package org.mineacademy.orion2.tool;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.tool.Tool;
import org.mineacademy.fo.remain.CompMaterial;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KittyArrow extends Tool {


    @Getter
	private final static Tool instance = new KittyArrow();

	@Override
	public ItemStack getItem() {
		return ItemCreator.of(CompMaterial.SPECTRAL_ARROW,
				"&6Kitty Arrow",
				"",
				"Use this as ammunition",
				"in KittyBow and KittyCannon",
				"tools!").glow(true)
				.build().make();
	}
	@Override
	protected void onBlockClick(PlayerInteractEvent event){
		event.setCancelled(true);

	}
}
