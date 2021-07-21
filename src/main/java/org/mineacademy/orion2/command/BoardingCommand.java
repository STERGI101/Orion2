package org.mineacademy.orion2.command;

import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.orion2.conversation.OnboardingConversation;

public class BoardingCommand extends SimpleCommand {
	public BoardingCommand() {
		super("survey|boarding");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		new OnboardingConversation().start(getPlayer());

	}
}
