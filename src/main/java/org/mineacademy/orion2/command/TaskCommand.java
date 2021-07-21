package org.mineacademy.orion2.command;

import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.orion2.OrionPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TaskCommand extends SimpleCommand {

	private final Map<UUID, BukkitTask> runningTasks = new HashMap<>();

	public TaskCommand() {
		super("task");

		setMinArguments(1);
		setUsage("<sync|async|start|stop>");
	}

	@Override
	protected void onCommand() {

		String param = args[0].toLowerCase();
		if("sync".equals(param)){
			// --> Play a sound and spawn a cat

            //traditional way of running tasks later
			new BukkitRunnable(){
				@Override
				public void run() {
					tell("Spawning a cat (1/2)");

					CompSound.LEVEL_UP.play(getPlayer());

					getPlayer().getWorld().spawnEntity(getPlayer().getLocation(), EntityType.CAT);

				}
			}.runTaskLater(OrionPlugin.getInstance(), 3* 20);

            //Recommended way of running tasks later
			Common.runLater(6* 20, () -> {
				tell("Spawning a chicken (2/2)");

				CompSound.CHICKEN_HURT.play(getPlayer());

				getPlayer().getWorld().spawnEntity(getPlayer().getLocation(), EntityType.CHICKEN);
			});

		}else if ("async".equals(param)){

            //This will fail since we cannot add entities asynchronously
			//Recommended use for async - connecting to the internet, using libraries other than Spigot etc. or when you know what you are doing : )
			Common.runLaterAsync(6* 20, () -> {
				tell("Spawning a zombie async");

				CompSound.ZOMBIE_HURT.play(getPlayer());

				getPlayer().getWorld().spawnEntity(getPlayer().getLocation(), EntityType.ZOMBIE);
			});


		}else if ("start".equals(param)){
			checkBoolean(!runningTasks.containsKey(getPlayer().getUniqueId()), "A task is already running!");
			runningTasks.put(getPlayer().getUniqueId(), Common.runTimer(20, () -> {
				tell("&6Sending you a timer message:");
			}));

		}else if ("stop".equals(param)){
			checkBoolean(runningTasks.containsKey(getPlayer().getUniqueId()), "A task is running for you!");
			BukkitTask task = runningTasks.remove(getPlayer().getUniqueId());

			task.cancel();
			tell("&6The task is now cancelled.");

		}else
			returnInvalidArgs();


	}
}
