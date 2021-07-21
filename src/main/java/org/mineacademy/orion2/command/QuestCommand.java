package org.mineacademy.orion2.command;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.TabUtil;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.orion2.DataFile;
import org.mineacademy.orion2.PlayerCache;
import org.mineacademy.orion2.classes.model.ClassBase;
import org.mineacademy.orion2.menu.QuestsMenu;
import org.mineacademy.orion2.quest.model.Quest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestCommand extends SimpleCommand {

	public QuestCommand() {
		super("quest");

		setUsage("[stop|remove/top <quest> <class>]");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final PlayerCache cache = PlayerCache.getCache(getPlayer());

		// /quest -> menu will show
		if (args.length == 0) {

			final PlayerCache.ActiveQuestCache activeQuest = cache.getActiveQuest();

			/*if (activeQuest != null) {
				tellNoPrefix( "&8[&6Pending quest " + activeQuest.getQuest().getName() + "&8] &f" + String.join(" ", activeQuest.getQuest().getMenuLore()) );

			} else*/
			new QuestsMenu(getPlayer()).displayTo(getPlayer());


			return;
		}

		final String param = args[0].toLowerCase();

		if ("stop".equals(param)) {
			checkBoolean(cache.getActiveQuest() != null, "You are not going through any quest right now.");

			tell("You have stopped your quest: " + cache.getActiveQuest().getQuest().getName());
			cache.stopActiveQuest();
		}

		else if ("top".equals(param) || "remove".equals(param)) {
			checkArgs(3, "Usage: /{label} {0} <class> <quest>");

			final ClassBase playerClass = ClassBase.getByName(args[1]);
			final Quest quest = Quest.getByName(Common.joinRange(2, args));

			checkNotNull(playerClass, "Invalid class named '" + args[1] + "'. Available: " + String.join(", ", ClassBase.getClassNames()));
			checkNotNull(quest, "Invalid quest named '" + Common.joinRange(2, args) + "'. Available: " + String.join(", ", Quest.getQuestsNames()));

			if ("remove".equals(param)) {
				checkBoolean(cache.hasCompletedQuest(quest, playerClass), "You haven't completed " + quest.getName() + " for " + playerClass.getName() + " class!");

				cache.removeQuest(quest, playerClass);
				tell("You have removed your quest " + quest.getName() + " from " + playerClass.getName() + " class");
			}

			else {
				final List<DataFile.PlayerQuestData> completionTimes = DataFile.getInstance().getCompletedQuests(playerClass, quest);
				tellNoPrefix("&7======== &6" + playerClass.getName() + ": " + quest.getName() + " Top Times &7========");

				for (int i = 0; i < completionTimes.size(); i++) {
					final DataFile.PlayerQuestData completion = completionTimes.get(i);

					tellNoPrefix("&f#" + (i + 1) + " &7- &f" + completion.getPlayer().getName() + " &7(" + completion.getQuest().getDurationFormatted() + ")");
				}
			}
		}
	}

	@Override
	protected List<String> tabComplete() {
		if (!isPlayer())
			return new ArrayList<>();

		if (args.length == 1)
			return completeLastWord("stop", "remove");

		else if (args.length > 1 && ("remove".equals(args[0]) || "top".equals(args[0]))) {
			final PlayerCache cache = PlayerCache.getCache(getPlayer());

			// Ninja: Only complete classes the player actually has
			if (args.length == 2)
				if ("top".equals(args[0]))
					return completeLastWord(ClassBase.getClassNames());
				else
					return completeLastWord(Common.convert(cache.getClasses(), (playerClass) -> playerClass.getClassBase().getName()));

				// SuperNinja: Only complete quests the player has completed
			else if (args.length > 2) {
				final String questName = Common.joinRange(2, args);

				if ("top".equals(args[0]))
					return TabUtil.complete(questName, Quest.getQuestsNames());

				else {
					final ClassBase playerClass = ClassBase.getByName(args[1]);
					final Set<PlayerCache.CompletedQuestCache> completedQuests = cache.hasClass(playerClass) ? cache.getClass(playerClass).getCompletedQuests() : new HashSet<>();

					return TabUtil.complete(questName, completedQuests != null ? Common.convert(completedQuests, (completedQuest) -> completedQuest.getQuest().getName()) : new ArrayList<>());
				}
			}
		}

		return new ArrayList<>();
	}
}