package org.mineacademy.orion2.settings;

import org.mineacademy.fo.model.Replacer;
import org.mineacademy.fo.settings.SimpleLocalization;

public class Localization extends SimpleLocalization {

	@Override
	protected int getConfigVersion() {
		return 1;
	}

	public static class Command {

		public static class Boss {
			public static Replacer NOT_FOUND;

			private static void init() {
				pathPrefix("Command.Boss");

				NOT_FOUND = getReplacer("Not_Found");
			}
		}
	}

	public static class Boarding {
		public static String COMPLETED;
		public static String CANCELLED;

		private static void init() {
			pathPrefix("Boarding");

			COMPLETED = getString("Completed");
			CANCELLED = getString("Cancelled");
		}
	}

	private static void init() {
		pathPrefix(null);
	}
}

