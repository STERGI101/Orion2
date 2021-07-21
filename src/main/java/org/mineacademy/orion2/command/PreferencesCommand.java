package org.mineacademy.orion2.command;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.menu.model.InventoryDrawer;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompItemFlag;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.orion2.menu.PreferencesPanelMenu;

public class PreferencesCommand extends SimpleCommand {
	public PreferencesCommand() {
		super("preferences|pref");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		new PreferencesPanelMenu().displayTo(getPlayer());

		InventoryDrawer drawer = InventoryDrawer.of(9*3, "&1User Preferences");

		drawer.setItem(0, ItemCreator.of(CompMaterial.DIAMOND,
				"&bShiny diamond",
				"&aFirst lore line",
				"&eSecond Lore Line")
				.glow(true)
				.build().make());
		drawer.setItem(9*1 + 4, new ItemStack(Material.GOLD_INGOT));

		drawer.pushItem(ItemCreator.of(CompMaterial.DIAMOND_AXE).flag(CompItemFlag.HIDE_ATTRIBUTES).build().make());

		drawer.display(getPlayer());

	}
}
