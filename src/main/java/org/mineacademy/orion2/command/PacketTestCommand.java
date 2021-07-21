package org.mineacademy.orion2.command;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.remain.CompMaterial;

public class PacketTestCommand extends SimpleCommand {

	public PacketTestCommand() {
		super("packettest");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();

		// ---------------------------------------------------------------------------------------------------------
		// Example 1

		// Using ProtocolLib to send a packet to set the sky to darker
		/*final PacketContainer packet = new PacketContainer(PacketType.Play.Server.GAME_STATE_CHANGE);

		packet.getIntegers().write(0, 7);
		packet.getFloat().write(0, 3F);

		HookManager.sendPacket(player, packet);*/

		// Official but long way, use HookManager to send your packet
		/*try {
			ProtocolLibrary.getProtocolManager().sendServerPacket(getPlayer(), packet);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}*/

		// ---------------------------------------------------------------------------------------------------------
		// Example 2

		// Using ProtocolLib to send a fake block change packet
		final PacketContainer blockChangePacket = new PacketContainer(PacketType.Play.Server.BLOCK_CHANGE);
		final Block lookedAtBlock = player.getTargetBlock(null, 6);

		blockChangePacket.getBlockPositionModifier().write(0, new BlockPosition(lookedAtBlock.getLocation().toVector()));
		blockChangePacket.getBlockData().write(0, WrappedBlockData.createData(CompMaterial.BEACON.getMaterial()));

		HookManager.sendPacket(player, blockChangePacket);

		// NB: Using packets for this operation is completely useless, you can simply call Bukkits method here
		//player.sendBlockChange(player.getLocation(), Bukkit.createBlockData(CompMaterial.BEACON.getMaterial()));
		// Or, you can also use Remain:
		// Remain.sendBlockChange
	}
}
