package org.mineacademy.orion2;

import org.bukkit.Material;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.fo.settings.YamlStaticConfig;
import org.mineacademy.orion2.boss.BossListener;
import org.mineacademy.orion2.boss.BossTimedTask;
import org.mineacademy.orion2.classes.ClassListener;
import org.mineacademy.orion2.classes.model.ClassBase;
import org.mineacademy.orion2.command.*;
import org.mineacademy.orion2.command.orion.OrionCommandGroup;
import org.mineacademy.orion2.event.DatabaseListener;
import org.mineacademy.orion2.event.LocaleListener;
import org.mineacademy.orion2.event.PlayerListener;
import org.mineacademy.orion2.event.ProjectileListener;
import org.mineacademy.orion2.hook.EffectLibHook;
import org.mineacademy.orion2.hook.ProtocolLibHook;
import org.mineacademy.orion2.mysql.OrionDatabase;
import org.mineacademy.orion2.quest.QuestListener;
import org.mineacademy.orion2.quest.QuestTask;
import org.mineacademy.orion2.rank.RankListener;
import org.mineacademy.orion2.rank.RankupTask;
import org.mineacademy.orion2.rank.model.Rank;
import org.mineacademy.orion2.settings.Localization;
import org.mineacademy.orion2.settings.Settings;
import org.mineacademy.orion2.stats.StatsListener;

import java.util.Arrays;
import java.util.List;

public class OrionPlugin extends SimplePlugin {

	private final SimpleCommandGroup commandGroup = new OrionCommandGroup();
	private EffectLibHook effectLibHook;

	@Override
	protected void onPluginStart() {
		getLogger().info("All works, captain!"); // Use Common#log instead
		System.out.println("All works from system out, boss!"); // Will not display your plugin prefix
		Common.log("Hello from foundation!"); // The recommended way of sending messages

		//LagCatcher.start("mysql");
		//OrionDatabase.getInstance().connect("casa21.fakaheda.eu", 3306, "254108_mysql_db", "254108_mysql_db", "demopassword", "Orion");
		//LagCatcher.end("mysql", 0, "Connection to MySQL established. Took {time} ms.");

		registerCommand(new FireworkCommand());
		registerCommand(new SpawnEntityCommand());
		registerCommand(new PermCommand());
		registerCommand(new TaskCommand());
		registerCommand(new PreferencesCommand());
		registerCommand(new RpgCommand());
		registerCommand(new BoardingCommand());
		registerCommand(new BossCommand());
		registerCommand(new CustomEnchantsCommand());
		registerCommand(new StatsCommand());
		registerCommand(new RankCommand());
		registerCommand(new ClassCommand());
		registerCommand(new QuestCommand());
		registerCommand(new CooldownCommand());
		registerCommand(new PacketTestCommand());

		registerEvents(new PlayerListener());
		registerEvents(new ProjectileListener());
		registerEvents(new BossListener());
		registerEvents(new RankListener());
		registerEvents(new ClassListener());
		registerEvents(new QuestListener());
		registerEvents(new StatsListener());
		registerEvents(new DatabaseListener());

		if (Common.doesPluginExist("EffectLib")) {
			effectLibHook = new EffectLibHook();

			registerEvents(effectLibHook);
		}

		Common.ADD_TELL_PREFIX = true;
		//Common.setLogPrefix(Common.getTellPrefix());

		// {orion_rank} (ChatControl) --> the current rank (in colors)
		// %orion_rank% (other plugins) --> ^
		HookManager.addPlaceholder("rank", (player) -> {
			final PlayerCache cache = PlayerCache.getCache(player);
			final Rank rank = cache.getRank();

			return rank.getColor() + rank.getName();
		});

		for (final ClassBase classBase : ClassBase.getClasses()) {

			// Magic Warrior --> magic_warrior
			HookManager.addPlaceholder("class_" + classBase.getName().toLowerCase().replace(" ", "_"), (player) -> {
				final PlayerCache cache = PlayerCache.getCache(player);
				final int tier = cache.getClassTier(classBase);

				return tier + "";
			});
		}

		Variables.addVariable("player_deaths", sender -> sender instanceof Player ? PlayerCache.getCache((Player) sender).getDeaths() + "" : "0");
	}

	@Override
	protected void onPluginStop() {
		if (Common.doesPluginExist("EffectLib") && effectLibHook != null)
			effectLibHook.shutdown();
	}

	// This method is called when you start the plugin AND when you reload it
	@Override
	protected void onReloadablesStart() {
		// Those tasks are cancelled on reload so they are only run once each time
		new BroadcasterTask().runTaskTimer(this, 0, Settings.BROADCASTER_DELAY.getTimeTicks());
		new BossTimedTask().runTaskTimer(this, 0, 20 /* this will run every second (20 ticks = 1 second) */);
		new RankupTask().runTaskTimer(this, 0, /*60 **/ 20 /* ONE SECOND --> you want to increase that number for better performance */);
		new QuestTask().runTaskTimer(this, 0, 20);

		// Events here are disabled on reload
		registerEventsIf(new LocaleListener(), Settings.LOG_PLAYERS_LOCALE);

		// Commands here are overriden on reload so they are registered only once
		if (HookManager.isCitizensLoaded())
			registerCommand(new NpcTestCommand());
		else
			Common.log("Citizens not found, limited functionality available!");

		// Packet listeners are automatically unregistered on reload as well
		if (HookManager.isProtocolLibLoaded())
			ProtocolLibHook.addPacketListeners();

		// Save player data to MySQL
		for (final Player player : Remain.getOnlinePlayers()) {
			final PlayerCache cache = PlayerCache.getCache(player);

			OrionDatabase.getInstance().save(player.getName(), player.getUniqueId(), cache);
		}

		// Save the data.db file
		DataFile.getInstance().save();

		// Clear the cache in the plugin so that we load it fresh for only players that are online right now,
		// saving memory
		PlayerCache.clearAllData();
	}

	// Automatically load classes extending YamlStaticConfig
	@Override
	public List<Class<? extends YamlStaticConfig>> getSettings() {
		return Arrays.asList(Settings.class, Localization.class);
	}

	@Override
	public SimpleCommandGroup getMainCommand() {
		return commandGroup;
	}

	@Override
	public boolean areScriptVariablesEnabled() {
		return true;
	}

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();

		// Send the player a simple colored message,
		// for color codes see https://minecraft.gamepedia.com/Formatting_codes
		Common.tellLater(2, player, "&eHello &9from &cFoundation");

		// Add five emeralds to the players inventory
		if (Settings.Join.GIVE_EMERALD)
			player.getInventory().addItem(new ItemStack(Material.EMERALD, 5));

		// Important! The Variables.replace is NOT called automatically when you use "tell" or Replacer.
		// You must call this manually like you see here. This is a feature that saves performance
		// because replacing thousands of variables can put a strain on your server so that you only replace
		// them when you need them.
		final String replacedMessage = Variables.replace(true, "Hello {player} you died {player_deaths} times and have {player_ping}ms ping.", player);

		Common.tell(player, replacedMessage);
	}

	@EventHandler
	public void onEntityDamage(final EntityDamageByEntityEvent event) {
		final Entity victim = event.getEntity();

		// If the damager is a player and the hit entity (victim) is a cow,
		// create explosion with the size of 2 at the cows location
		if (event.getDamager() instanceof Player && victim instanceof Cow && Settings.Entity_Hit.EXPLODE_COW)
			victim.getWorld().createExplosion(victim.getLocation(), Settings.Entity_Hit.POWER.floatValue());
	}

	/*@EventHandler
	public void onBossSpawn(final BossSpawnEvent event) {
		Common.log("Spawning " + event.getBoss().getName() + " at " + Common.shortLocation(event.getLocation()));

		//event.setCancelled(true);
	}*/
}
