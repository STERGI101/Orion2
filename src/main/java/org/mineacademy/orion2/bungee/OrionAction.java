package org.mineacademy.orion2.bungee;

import lombok.Getter;
import org.mineacademy.fo.bungee.BungeeAction;

public enum OrionAction implements BungeeAction {

	/**
	 * Represents a chat message.
	 *
	 * Data: the senders name, the message
	 */
	CHAT_MESSAGE(String.class, String.class);

	@Getter
	private final Class<?>[] content;

	private OrionAction(final Class<?>... content) {
		this.content = content;
	}
}
