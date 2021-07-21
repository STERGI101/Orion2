package org.mineacademy.orion2.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonConversation;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.orion2.PlayerCache;
import org.mineacademy.orion2.classes.model.ClassBase;
import org.mineacademy.orion2.quest.QuestStartPrompt;
import org.mineacademy.orion2.quest.model.Quest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class QuestsMenu extends MenuPagged<PlayerCache.ClassCache> {

	private final Player player;

	public QuestsMenu(final Player player) {
		super(9, getClasses(player));

		setTitle("Select your class");

		this.player = player;
	}

	private static Iterable<PlayerCache.ClassCache> getClasses(final Player player) {
		final PlayerCache cache = PlayerCache.getCache(player);
		final PlayerCache.ActiveQuestCache activeQuest = cache.getActiveQuest();

		return activeQuest != null ? Arrays.asList(cache.getClass(activeQuest.getClassBase())) : cache.getClasses();
	}

	@Override
	protected ItemStack convertToItemStack(final PlayerCache.ClassCache item) {
		final ClassBase classBase = item.getClassBase();

		return ItemCreator.of(classBase.getIcon(),
				classBase.getName() + " " + item.getTierRoman(),
				"",
				"Click to open quests",
				"for this class.")
				.build().make();
	}

	@Override
	protected void onPageClick(final Player player, final PlayerCache.ClassCache item, final ClickType click) {
		new QuestClassMenu(item.getClassBase()).displayTo(player);
	}

	@Override
	protected String[] getInfo() {
		return new String[] {
				"Select your class for which you",
				"want to start or manage your",
				"quests and missions."
		};
	}

	private final class QuestClassMenu extends Menu {

		private final Button completedQuests;
		private final Button currentQuest;

		public QuestClassMenu(final ClassBase questClass) {
			super(QuestsMenu.this);

			setTitle(questClass.getName() + " Quests");
			setSize(9 * 3);

			final PlayerCache cache = PlayerCache.getCache(player);

			completedQuests = new ButtonMenu(new CompletedQuestsMenu(this, questClass), CompMaterial.DIAMOND_SWORD,
					"Completed quests",
					"",
					"Click to view your past",
					"quests you had completed.");

			final Quest next = cache.getNextQuest(questClass);

			currentQuest = cache.getActiveQuest() != null ? getActiveQuestButton(cache.getActiveQuest().getQuest()) : next != null ? getStartQuestButton(next, questClass) : getMaxQuestButton();
		}

		private Button getActiveQuestButton(final Quest quest) {
			return ItemCreator.of(CompMaterial.ENDER_EYE,
					quest.getName(),
					"",
					"Click to display progress",
					"and information about",
					"your active quest.").build().makeButton();
		}

		private Button getStartQuestButton(final Quest quest, final ClassBase classBase) {
			return new ButtonConversation(new QuestStartPrompt(quest, classBase), ItemCreator.of(CompMaterial.ENDER_EYE,
					"Start Next Quest",
					"",
					"Click to start your next",
					"quest of your class!")
					.glow(true));
		}

		private Button getMaxQuestButton() {
			return ItemCreator.of(CompMaterial.ENDER_EYE,
					"All Quests Completed",
					"",
					"You have completed all quests",
					"of your class. Click to show",
					"leaderboards and statistics.").build().makeButton();
		}

		@Override
		public ItemStack getItemAt(final int slot) {

			if (slot == 9 + 2)
				return completedQuests.getItem();

			if (slot == 9 + 4)
				return currentQuest.getItem();

			return null;
		}

		@Override
		protected int getReturnButtonPosition() {
			return 9 + 6;
		}

		@Override
		protected String[] getInfo() {
			return null;
		}
	}

	private final class CompletedQuestsMenu extends MenuPagged<PlayerCache.CompletedQuestCache> {

		protected CompletedQuestsMenu(final Menu parent, final ClassBase classBase) {
			super(9 * 1, parent, getCompletedQuests(player, classBase));

			setTitle("Completed Quests");
		}

		@Override
		protected ItemStack convertToItemStack(final PlayerCache.CompletedQuestCache item) {
			final Quest quest = item.getQuest();

			final List<String> lore = new ArrayList<>();
			lore.add(" ");
			if (item.getCompletionDate() != 0)
				lore.add("Completed in " + item.getDurationFormatted());
			lore.add(" ");
			lore.addAll(Arrays.asList(quest.getMenuLore()));

			return ItemCreator
					.of(quest.getIcon(), quest.getName())
					.lores(lore)
					.build().make();
		}

		@Override
		protected void onPageClick(final Player player, final PlayerCache.CompletedQuestCache quest, final ClickType click) {
		}

		@Override
		protected String[] getInfo() {
			return null;
		}
	}

	private static Set<PlayerCache.CompletedQuestCache> getCompletedQuests(final Player player, final ClassBase classBase) {
		final PlayerCache cache = PlayerCache.getCache(player);

		return cache.getClass(classBase).getCompletedQuests();
	}
}

