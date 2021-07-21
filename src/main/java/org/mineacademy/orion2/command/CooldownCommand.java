package org.mineacademy.orion2.command;

import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.Remain;

public class CooldownCommand extends SimpleCommand {

	public CooldownCommand() {
		super("cooldown");

		setMinArguments(2);
		setUsage("<material> <cooldownTicks>");
	}

	@Override
	protected void onCommand() {
		checkConsole();
		final Player player = getPlayer();

		// NB: You can only set the material by command using the last method below from Remain or Bukkit
		final CompMaterial material = findMaterial(args[0], "Material named {item} does not exist!");
		final int cooldownTicks = findNumber(1, "Specify a whole number not {1}");

		// ---------------------------------------------------------------------------------------------------------
		// Example 1: Using packet classes in the NMS package with direct import to create and send the packet

		//final PacketPlayOutSetCooldown packet = new PacketPlayOutSetCooldown(Items.COOKIE, cooldownTicks);
		//((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);

		// ---------------------------------------------------------------------------------------------------------
		// Example 2: Using reflection to access NMS packet classes and achieve the same result as above
		// 			  but without the safeguard in our imports breaking our plugin (your plugin may still break on updates)

		/*try {
			final Class<?> itemClass = ReflectionUtil.getNMSClass("Item");
			final Class<?> itemsClass = ReflectionUtil.getNMSClass("Items");
			final Object cookieItem = ReflectionUtil.getStaticFieldContent(itemsClass, "COOKIE");

			final Class<?> packetClass = ReflectionUtil.getNMSClass("PacketPlayOutSetCooldown");
			final Object packet = packetClass.getConstructor(itemClass, int.class).newInstance(cookieItem, cooldownTicks);

			Remain.sendPacket(player, packet);
		} catch (final Throwable t) {
			t.printStackTrace();
		}*/

		// ---------------------------------------------------------------------------------------------------------
		// Example 3: Using the Remain (Bukkit/own) way to easily and effortlessly achieve the same result

		// MinecraftVersion.atLeast --> check if the MC version is new enough
		Remain.setCooldown(player, material.getMaterial(), cooldownTicks);

	tell("Cooldown wurde &l&2erfolgreich &ffür " + material.getMaterial() + " gesetzt!");
	}
}
