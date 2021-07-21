package org.mineacademy.orion2.command;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.TabUtil;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompMetadata;
import org.mineacademy.orion2.boss.model.Boss;
import org.mineacademy.orion2.settings.Localization;

import java.util.ArrayList;
import java.util.List;

public class BossCommand extends SimpleCommand {

	public BossCommand() {
		super("boss");

		setMinArguments(2);
		setUsage("<spawn|setspawner> <bossName>");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final String param = args[0].toLowerCase();

		final String bossName = Common.joinRange(1, args);
		final Boss boss = Boss.findBoss(bossName);

		checkNotNull(boss,
				Localization.Command.Boss.NOT_FOUND
						.find("boss_name", "boss_available")
						.replace(bossName, String.join(", ", Boss.getBossesNames()))
						.getReplacedMessageJoined());

		if ("spawn".equals(param))
			boss.spawn(getPlayer().getLocation());

		else if ("setspawner".equals(param)) {
			final Player player = getPlayer();
			final Block lookedAtBlock = player.getTargetBlock(null, 6);

			checkBoolean(lookedAtBlock != null && lookedAtBlock.getType() == CompMaterial.SPAWNER.getMaterial(), "You must be looking at a mob spawner.");

			final CreatureSpawner spawner = (CreatureSpawner) lookedAtBlock.getState();

			spawner.setSpawnedType(boss.getType());
			CompMetadata.setMetadata(spawner, Boss.BOSS_SPAWNER_TAG, boss.getName());

			tell("&6Set the spawner to spawn " + boss.getName());
		}
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord("spawn", "setspawner");

		if (args.length > 1) {
			final String bossName = Common.joinRange(1, args);

			return TabUtil.complete(bossName, Boss.getBossesNames());
		}

		return new ArrayList<>();
	}
}
