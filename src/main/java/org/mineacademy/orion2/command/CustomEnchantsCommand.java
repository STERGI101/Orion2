package org.mineacademy.orion2.command;

import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.SimpleEnchant;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.orion2.enchant.BlackNovaEnchant;
import org.mineacademy.orion2.enchant.HideEnchant;

public class CustomEnchantsCommand extends SimpleCommand {
	public CustomEnchantsCommand() {
		super("customenchants|ce");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		Player player = getPlayer();

		player.getInventory().addItem(
				ItemCreator.of(CompMaterial.IRON_SWORD,
						"Black Sword",
						"",
						"Mystical Black Sword",
						"having special abilities.")
						.enchant(new SimpleEnchant(BlackNovaEnchant.getInstance(),1))
						.build().makeSurvival(),

				ItemCreator.of(CompMaterial.BOW)
						.enchant(new SimpleEnchant(HideEnchant.getInstance(),2))
						.build().makeSurvival()
		);

		tell("&6You were given custom items with custom enchantments.");
	}
}
