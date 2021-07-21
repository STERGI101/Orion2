package org.mineacademy.orion2.quest;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.orion2.PlayerCache;
import org.mineacademy.orion2.classes.model.ClassBase;
import org.mineacademy.orion2.quest.model.Quest;

import java.util.Arrays;

public final class QuestStartPrompt extends SimplePrompt {

	private final Quest quest;
	private final ClassBase classBase;

	public QuestStartPrompt(final Quest quest, final ClassBase classBase) {
		super(false);

		this.quest = quest;
		this.classBase = classBase;
	}

	@Override
	protected String getPrompt(final ConversationContext ctx) {
		return String.join("\n", Arrays.asList(
				"&8" + Common.chatLineSmooth(),
				"&cWelcome to '" + quest.getName() + "' quest",
				"&8 ",
				"&cObjective&7: " + String.join(" ", quest.getMenuLore()),
				"&7Type &caccept &7to take on this quest, or &cdeny &7to reject it.",
				"&8" + Common.chatLineSmooth()));
	}

	@Override
	protected boolean isInputValid(final ConversationContext context, final String input) {
		return Arrays.asList("accept", "deny").contains(input);
	}

	@Override
	protected Prompt acceptValidatedInput(final ConversationContext context, final String input) {
		final Player player = getPlayer(context);

		if ("accept".equals(input)) {
			final PlayerCache cache = PlayerCache.getCache(player);

			cache.startQuest(quest, classBase);
			tell(context, "&6You have started this quest.");

		} else
			tell(context, "&cYou have decided not to start this quest.");

		return Prompt.END_OF_CONVERSATION;
	}

}
