package org.mineacademy.orion2.command.orion;

import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.SimpleLocalization;

public class ReloadCommand extends SimpleSubCommand {

	protected ReloadCommand() {
		super("reload|rl");
	}

	@Override
	protected void onCommand() {
		try {
			SimplePlugin.getInstance().reload();
			tell(SimpleLocalization.Commands.RELOAD_SUCCESS);

		} catch (final Throwable t) {
			t.printStackTrace();

			tell(SimpleLocalization.Commands.RELOAD_FAIL.replace("{error}", t.toString()));
		}
	}
}

