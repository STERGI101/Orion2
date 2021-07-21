package org.mineacademy.orion2.command;

import org.bukkit.entity.Player;
import org.mineacademy.fo.MathUtil;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.orion2.PlayerCache;
import org.mineacademy.orion2.classes.model.ClassBase;

import java.util.ArrayList;
import java.util.List;

public class ClassCommand extends SimpleCommand {

	public ClassCommand() {
		super("class|cl");

		addTellPrefix(false); // Edit: Uncomment this line if you do not want the plugin prefix in front of each line
		setUsage("[upgrade]");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		final PlayerCache cache = PlayerCache.getCache(player);

		final String param = args.length > 0 ? args[0].toLowerCase() : "";

		if ("upgrade".equals(param)) {
			checkArgs(2, "Usage: /{label} {0} <className>");

			final String className = args[1];
			final ClassBase classBase = ClassBase.getByName(className);

			checkNotNull(classBase, "Class named " + className + " does not exist. Available: " + String.join(", ", ClassBase.getClassNames()));

			if (!classBase.upgradeToNextTier(player))
				tell("&cYou do not qualify to upgrade to " + classBase.getName() + " lvl " + cache.getClassTier(classBase) + ".");
		}

		else {
			tell("&7============ &6Your Classes &7============");

			for (final PlayerCache.ClassCache classCache : cache.getClasses())
				tell(" &8- &f" + classCache.getClassBase().getName() + " &7lvl. &f" + MathUtil.toRoman(classCache.getTier()));
		}
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord("upgrade");

		if (args.length == 2)
			return completeLastWord(ClassBase.getClassNames());

		return new ArrayList<>();
	}
}




