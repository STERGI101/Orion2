package org.mineacademy.orion2.settings;

import org.mineacademy.fo.settings.SimpleSettings;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.List;

// Update: added this suppress warnings annotation for Eclipse users
public class Settings extends SimpleSettings {

	@Override
	protected int getConfigVersion() {
		return 1;
	}

	public static class Menu {
		public static String MENU_PREFERENCES_TITLE;
		public static String MENU_EGG_SELECTION_TITLE;

		private static void init() {
			pathPrefix("Menu");

			MENU_PREFERENCES_TITLE = getString("Preferences_Title");
			MENU_EGG_SELECTION_TITLE = getString("Egg_Selection_Title");
		}
	}

	public static class Join {
		public static Boolean GIVE_EMERALD;

		private static void init() {
			pathPrefix("Join");

			GIVE_EMERALD = getBoolean("Give_Emerald");
		}
	}

	public static class Entity_Hit {
		public static Boolean EXPLODE_COW;
		public static Double POWER;

		private static void init() {
			pathPrefix("Entity_Hit");

			EXPLODE_COW = getBoolean("Explode_Cows");
			POWER = getDoubleSafe("Power");
		}
	}

	public static List<String> IGNORED_LOG_COMMANDS;
	public static Boolean LOG_PLAYERS_LOCALE;
	public static YamlConfig.TimeHelper BROADCASTER_DELAY;


	private static void init() {
		pathPrefix(null);

		IGNORED_LOG_COMMANDS = getStringList("Ignored_Log_Commands");
		LOG_PLAYERS_LOCALE = getBoolean("Log_Players_Locale");
		BROADCASTER_DELAY = getTime("Broadcaster_Delay");
	}
}
