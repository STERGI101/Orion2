package org.mineacademy.orion2.menu;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.MathUtil;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.MenuQuantitable;
import org.mineacademy.fo.menu.MenuTools;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonConversation;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.model.MenuQuantity;
import org.mineacademy.fo.remain.CompColor;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.orion2.PlayerCache;
import org.mineacademy.orion2.classes.model.ClassBase;
import org.mineacademy.orion2.conversation.ExpPrompt;
import org.mineacademy.orion2.settings.Settings;
import org.mineacademy.orion2.tool.*;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PreferencesPanelMenu extends Menu {

	private final Button timeButton;
	private final Button mobEggButton;
	private final Button chatColorButton;

	private final Button levelButton;
	private final Button toolsButton;
	private final Button classesButton;

	private final Button expButton;

	public PreferencesPanelMenu() {

		setTitle(Settings.Menu.MENU_PREFERENCES_TITLE);
		setSize(9 * 6);

		setSlotNumbersVisible();

		timeButton = new Button() {
			@Override
			public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
				final boolean isDay = isDay();

				player.getWorld().setFullTime(isDay ? 13000 : 1000);
				restartMenu("&2Set the time to " + (isDay ? "night" : "day"));
			}

			@Override
			public ItemStack getItem() {
				final boolean isDay = isDay();

				return ItemCreator.of(isDay ? CompMaterial.SUNFLOWER : CompMaterial.RED_BED,
						"Change the time",
						"",
						"Currently: " + (isDay ? "day" : "night"),
						"",
						"Click to switch between",
						"the day and the night").build().make();
			}

			private boolean isDay() {
				return getViewer().getWorld().getFullTime() < 12500;
			}
		};

		mobEggButton = new ButtonMenu(new EggSelectionMenu(), CompMaterial.ENDERMAN_SPAWN_EGG,
				"Monster egg menu",
				"",
				"Click to open monster",
				"egg selection menu.");

		chatColorButton = new ButtonMenu(new ChatColorSelectionMenu(), CompMaterial.INK_SAC,
				"Chat color menu",
				"",
				"Click to open chat",
				"color selection menu.");

		levelButton = new ButtonMenu(new LevelMenu(), CompMaterial.ELYTRA,
				"Level menu",
				"",
				"Click to open level",
				"menu to change your level.");

		toolsButton = new ButtonMenu(new OrionTools(), CompMaterial.BLAZE_ROD,
				"Tools menu",
				"",
				"Click to open tools",
				"menu to get admin tools.");

		classesButton = new ButtonMenu(new ClassSelectionMenu(), CompMaterial.IRON_CHESTPLATE,
				"Classes menu",
				"",
				"Click to open classes",
				"menu to select your RPG class.");

		expButton = new ButtonConversation(new ExpPrompt(), CompMaterial.EXPERIENCE_BOTTLE,
				"Experience prompt",
				"",
				"Click to open a new prompt",
				"giving you experience levels.");
	}

	@Override
	public ItemStack getItemAt(final int slot) {

		if (slot == 9 * 1 + 1)
			return timeButton.getItem();

		if (slot == 9 * 1 + 3)
			return chatColorButton.getItem();

		if (slot == 9 * 1 + 5)
			return mobEggButton.getItem();

		if (slot == 9 * 1 + 7)
			return levelButton.getItem();

		if (slot == 9 * 3 + 1)
			return classesButton.getItem();

		if (slot == 9 * 3 + 3)
			return toolsButton.getItem();

		if (slot == 9 * 3 + 5)
			return expButton.getItem();

		return null;
	}

	@Override
	protected String[] getInfo() {
		return new String[] {
				"This menu contains simple features",
				"for players or administrators to",
				"enhance their gameplay experience."
		};
	}

	private final class EggSelectionMenu extends MenuPagged<EntityType> {

		private EggSelectionMenu() {
			super(9 * 4, PreferencesPanelMenu.this, Arrays.asList(EntityType.values())
					.stream()
					.filter((entityType) -> entityType.isSpawnable() && entityType.isAlive() && (entityType == EntityType.SHEEP || CompMaterial.makeMonsterEgg(entityType) != CompMaterial.SHEEP_SPAWN_EGG))
					.collect(Collectors.toList()));

			setTitle("Select a mob egg");
		}

		@Override
		protected ItemStack convertToItemStack(final EntityType entityType) {
			return ItemCreator.of(CompMaterial.makeMonsterEgg(entityType), "Spawn " + ItemUtil.bountifyCapitalized(entityType)).build().make();
		}

		@Override
		protected void onPageClick(final Player player, final EntityType entityType, final ClickType clickType) {
			player.getInventory().addItem(ItemCreator.of(CompMaterial.makeMonsterEgg(entityType)).build().make());
			animateTitle("Egg added into your inventory!");
		}

		@Override
		protected String[] getInfo() {
			return new String[] {
					"Click an egg to get it",
					"into your inventory."
			};
		}
	}

	private final class ChatColorSelectionMenu extends MenuPagged<ChatColor> {

		private ChatColorSelectionMenu() {
			super(9 * 3, PreferencesPanelMenu.this, Arrays.asList(ChatColor.values())
					.stream()
					// I replaced that with a method reference to further increase our coding standard
					// - you can only do this for a single condition here so you cannot do this for EntityType above
					.filter(ChatColor::isColor)
					.collect(Collectors.toList()));

			setTitle("Select a chat color");
		}

		@Override
		protected ItemStack convertToItemStack(final ChatColor color) {
			return ItemCreator.ofWool(CompColor.fromChatColor(color)).name(color + "Select " + ItemUtil.bountifyCapitalized(color.name())).build().make();
		}

		@Override
		protected void onPageClick(final Player player, final ChatColor color, final ClickType clickType) {
			final PlayerCache cache = PlayerCache.getCache(player);

			cache.setColor(color);
			animateTitle(color + "Changed chat color to " + ItemUtil.bountifyCapitalized(color.name()));
		}

		@Override
		protected String[] getInfo() {
			return new String[] {
					"Click a color to use it",
					"in your chat messages."
			};
		}
	}

	private final class LevelMenu extends Menu implements MenuQuantitable {

		@Setter
		@Getter
		private MenuQuantity quantity = MenuQuantity.ONE;

		private final Button quantityButton;
		private final Button levelButton;

		private LevelMenu() {
			super(PreferencesPanelMenu.this);

			setTitle("Change your level");

			quantityButton = getEditQuantityButton(this);

			levelButton = new Button() {
				@Override
				public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
					final PlayerCache cache = PlayerCache.getCache(getViewer());
					final int nextLevel = MathUtil.range(cache.getLevel() + getNextQuantity(clickType), 1, 64);

					cache.setLevel(nextLevel);
					restartMenu("Changed level to " + nextLevel);
				}

				@Override
				public ItemStack getItem() {
					final PlayerCache cache = PlayerCache.getCache(getViewer());

					return ItemCreator.of(CompMaterial.ELYTRA,
							"Change your level",
							"",
							"Current: " + cache.getLevel(),
							"",
							"&8(&7Mouse click&8)",
							"< -{q} +{q} >".replace("{q}", quantity.getAmount() + ""))
							.amount(cache.getLevel() == 0 ? 1 : cache.getLevel())
							.build().make();
				}
			};
		}

		@Override
		public ItemStack getItemAt(final int slot) {

			if (slot == getCenterSlot())
				return levelButton.getItem();

			if (slot == getSize() - 5)
				return quantityButton.getItem();

			return null;
		}

		@Override
		protected String[] getInfo() {
			return new String[] {
					"Click the elytra to",
					"level up/down yourself."
			};
		}
	}

	private final class OrionTools extends MenuTools {

		protected OrionTools() {
			super(PreferencesPanelMenu.this); // I added the parent menu behind the scenes for convenience

			setTitle("&4Orion Tools");
		}

		// Return the items you want in this tools menu
		// positioned with the array. Use 0 to place air and increase their position
		@Override
		protected Object[] compileTools() {
			return new Object[] {
					0, HellfireRocket.getInstance(), 0, DiamondChangingTool.getInstance(), 0, KittyArrow.getInstance(), KittyCannon.getInstance(), KittyBow.getInstance()
			};
		}

		@Override
		protected String[] getInfo() {
			return new String[] {
					"Select your tools",
					"for some fun!"
			};
		}
	}

	private final class ClassSelectionMenu extends MenuPagged<ClassBase> {

		protected ClassSelectionMenu() {
			super(9 * 1, PreferencesPanelMenu.this, ClassBase.getClasses());
		}

		@Override
		protected ItemStack convertToItemStack(final ClassBase classBase) {
			final PlayerCache cache = PlayerCache.getCache(getViewer());
			final int tier = cache.getClassTier(classBase);
			final boolean has = tier > 0;
			final String romanTier = has ? MathUtil.toRoman(tier) : "";

			return ItemCreator.of(classBase.getIcon(),
					classBase.getName() + (has ? " " + romanTier : ""),
					"",
					(has ? "You have this class" : "Click to select"),
					(has ? "with Tier " + romanTier : "this class."))
					.glow(has)
					.build().make();
		}

		@Override
		protected void onPageClick(final Player player, final ClassBase classBase, final ClickType clickType) {

			/*if (!PlayerUtil.hasPerm(player, "my.class.perm." + playerClass.getName()) {
				animateTitle("You cannot select this class!");

				return;
			}*/

			final PlayerCache cache = PlayerCache.getCache(player);
			final int existingTier = cache.getClassTier(classBase);

			if (clickType == ClickType.LEFT) {
				final int newTier = existingTier + 1;

				// OBS! If you want all fancy methods to be called, use classBase.upgradeToNextTier
				if (cache.hasClass(classBase))
					cache.setClassTier(classBase, newTier);
				else
					cache.addClass(classBase);

				classBase.applyFor(newTier, player);
				restartMenu("&2Selected " + classBase.getName() + " class!");
			}

			else {
				if (existingTier > 0) {
					if (cache.hasClass(classBase))
						cache.removeClass(classBase);

					restartMenu("&4Removed " + classBase.getName() + " class!");
				}
			}
		}

		@Override
		protected String[] getInfo() {
			return new String[] {
					"Click the classes to",
					"select them."
			};
		}
	}
}

