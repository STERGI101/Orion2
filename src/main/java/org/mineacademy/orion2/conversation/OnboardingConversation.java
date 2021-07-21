package org.mineacademy.orion2.conversation;

import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.TimeUtil;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.conversation.SimplePrefix;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.orion2.settings.Localization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OnboardingConversation extends SimpleConversation {
	@Override
	protected Prompt getFirstPrompt() {
		return new NamePrompt();
	}

	@Override
	protected ConversationPrefix getPrefix() {
		return new SimplePrefix("&8[&6Application&8]&7 ");
	}

	/*@Override
	protected ConversationCanceller getCanceller() {
		return new SimpleCanceller("one", "two", "three");
	}*/

	@Override
	protected void onConversationEnd(ConversationAbandonedEvent event) {
		// event.gracefulExit(); --> will be on true if the last prompt is null,
		// or false if we cancelled it with getCanceller()

		if(event.gracefulExit())
			tell(event.getContext().getForWhom(), Localization.Boarding.COMPLETED);
		else
			tell(event.getContext().getForWhom(), Localization.Boarding.CANCELLED);
	}

	enum Boarding{
		NAME,
		JOIN_REASON,
		PLAY_PREFERENCE,
		EXPECTATION

	}

	private class NamePrompt extends SimplePrompt{

		@Override
		protected String getPrompt(ConversationContext conversationContext) {
			return "What is your name?";
		}

		@Override
		protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
			context.setSessionData(Boarding.NAME, input);

			return new JoiningReasonPrompt();
		}
	}

	private class JoiningReasonPrompt extends SimplePrompt{

		@Override
		protected String getPrompt(ConversationContext conversationContext) {
			return "What brings you to this server, " + conversationContext.getSessionData(Boarding.NAME) + "?";
		}

		@Override
		protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
			context.setSessionData(Boarding.JOIN_REASON, input);

			return new GamePreferencePrompt();
		}
	}

	private class GamePreferencePrompt extends SimplePrompt{

		@Override
		protected String getPrompt(ConversationContext conversationContext) {
			return "What kind of servers do you like playing on?";
		}

		@Override
		protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
			context.setSessionData(Boarding.PLAY_PREFERENCE, input);

			return new ExpectationPrompt();
		}
	}

	private class ExpectationPrompt extends SimplePrompt{

		@Override
		protected String getPrompt(ConversationContext conversationContext) {
			return "What are you looking forward to see here, " + conversationContext.getSessionData(Boarding.NAME) + "?";
		}

		@Override
		protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
			context.setSessionData(Boarding.EXPECTATION, input);

			final Player player = getPlayer(context);
			final List<String> lines = new ArrayList<>();

			lines.add(Common.chatLine());
			lines.add("Date: " + TimeUtil.getFormattedDateShort());

			for(final Map.Entry<Object, Object> entry: context.getAllSessionData().entrySet())
				lines.add(ItemUtil.bountifyCapitalized(entry.getKey().toString()) + ": " + entry.getValue().toString());

			FileUtil.write("survey/" + player.getName() + ".txt", lines);

			tell(context, "Thank you for finishing the survey, here is your reward!");
			player.getInventory().addItem(ItemCreator.of(CompMaterial.NETHER_STAR,
					"&bCrate key",
					"", "Secret key to unlock",
					"bonuses in the game."
			).build().make());

			return Prompt.END_OF_CONVERSATION;
		}
	}
}
