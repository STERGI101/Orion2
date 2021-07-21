package org.mineacademy.orion2.conversation;

import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.remain.CompSound;

public class ExpPrompt extends SimplePrompt {

	public ExpPrompt(){
		super(false);
	}

	@Override
	protected String getPrompt(ConversationContext conversationContext) {
		return "&6Write the amount of exp levels you wanna receive.";
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input) {
		if(!Valid.isInteger(input))
			return false;

		int level = Integer.parseInt(input);

		return level > 0 && level < 9009;
	}

	@Override
	protected String getFailedValidationText(ConversationContext context, String invalidInput) {
		return "&cOnly specify a non-zero number";
	}

	@Override
	protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
		int level = Integer.parseInt(input);
		Player player = getPlayer(context);

		player.setLevel(level);
		CompSound.LEVEL_UP.play(player);

		tell(context, "&6You now have " + level + " experience levels.");

		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	public void onConversationEnd(SimpleConversation conversation, ConversationAbandonedEvent event) {
		//Freie Wahl, um was zu Ende der Konversation zu machen wie eine Nachricht oder so.
	}
}
