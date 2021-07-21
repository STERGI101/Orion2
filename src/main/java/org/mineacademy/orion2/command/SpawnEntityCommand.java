package org.mineacademy.orion2.command;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

public class SpawnEntityCommand extends SimpleCommand {
	public SpawnEntityCommand() {

		super("spawnentity|se"); // /spawnentity or /se

		setMinArguments(1);
		setUsage("<type> [x] [y] [z]");
		setDescription("Spawn an entity at your or the given location.");

	}

	@Override
	protected void onCommand() {
		// /spawnentity <type> [x] [y] [z]
		// /spawnentity pig --> spawn pig at your location
		// /spawnentity pig 100 5 78 --> spawn pig at 100 5 78 coordinates

		checkConsole();

		EntityType entitytype = findEnum(EntityType.class, args[0], "&cEntity named {enum} is invalid.");

		checkBoolean(entitytype.isAlive() && entitytype.isSpawnable(),"&cEntity " + entitytype + " is not spawnable.");
		Location location;
		if(args.length == 4){
			int x = findNumber(1, "Please specify the x coordinate as a number");
			int y = findNumber(2, "Please specify the y coordinate as a number");
			int z = findNumber(3, "Please specify the z coordinate as a number");

			location = new Location(getPlayer().getWorld(), x, y, z);
		}
		else
			location = getPlayer().getLocation();
		location.getWorld().spawnEntity(location,entitytype);
		tell("&aSpawned " + entitytype + " at " + Common.shortLocation(location));



	}
}
