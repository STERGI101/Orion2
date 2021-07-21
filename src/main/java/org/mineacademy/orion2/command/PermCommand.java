package org.mineacademy.orion2.command;

import org.bukkit.permissions.PermissionAttachment;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.orion2.OrionPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PermCommand extends SimpleCommand {

	private final static String DEMO_PERMISSION = "my.simple.permission";

    // TODO Highest coding standards say to only have command execution code in the command class, so you are recommended to move this into a separate PermissionsManager class of your Plugin
	private final Map<UUID, PermissionAttachment> permissions = new HashMap<>();

	public PermCommand() {
		super("perm");

		setMinArguments(1);
		setUsage("<add|remove>");
	}

	@Override
	protected void onCommand() {
		// <add/remove>
		String param = args[0].toLowerCase();
		if("add".equals(param)){
			checkBoolean(!permissions.containsKey(getPlayer().getUniqueId()),"&fRun &6/{label} remove &fto remove the demo permission first");
			tell("Do you have the demo permission before? " + hasPerm(DEMO_PERMISSION));
			PermissionAttachment perm = getPlayer().addAttachment(OrionPlugin.getInstance(), "bukkit.command.plugins",false);
			tell("Do you have the demo permission after? " + hasPerm(DEMO_PERMISSION));

			permissions.put(getPlayer().getUniqueId(), perm);

		}else if ("remove".equals(param)){

			PermissionAttachment perm = permissions.remove(getPlayer().getUniqueId());
			checkNotNull(perm, "Run /{label} add first to give yourself the demo permission.");
			tell("Do you have the demo permission before? " + hasPerm(DEMO_PERMISSION));
			getPlayer().removeAttachment(perm);
			tell("Do you have the demo permission after? " + hasPerm(DEMO_PERMISSION));



		}else
			returnInvalidArgs();


	}
}
