package org.mineacademy.orion2.hook;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProtocolLibHook {

	public static void addPacketListeners() {
		// Log all incoming and outgoing packets
		/*HookManager.addPacketListener(new PacketAdapter(SimplePlugin.getInstance(), PacketType.values()) {
			@Override
			public void onPacketReceiving(final PacketEvent event) {
				Common.log("Receiving " + event.getPacketType());
			}

			@Override
			public void onPacketSending(final PacketEvent event) {
				Common.log("Sending " + event.getPacketType());
			}
		});*/

		// Listen to the Server Info packet to show fake player count
		/*HookManager.addPacketListener(new PacketAdapter(SimplePlugin.getInstance(), PacketType.Status.Server.SERVER_INFO) {

			@Override
			public void onPacketSending(final PacketEvent event) {
					final PacketContainer packet = event.getPacket();

					final WrappedServerPing ping = packet.getServerPings().read(0);

					ping.setPlayersOnline(55_894);
					ping.setPlayersMaximum(100_000); // +1 value of how many players are actually online

					packet.getServerPings().write(0, ping);
			}
		});*/

		// Listen to outgoing World Particles packet and create a real explosion when the player hits the ground
		// - we find out that he has hit the ground by listening to the impact particles created by him
		/*HookManager.addPacketListener(new PacketAdapter(SimplePlugin.getInstance(), PacketType.Play.Server.WORLD_PARTICLES) {

			@Override
			public void onPacketSending(final PacketEvent event) {
				final PacketContainer packet = event.getPacket();

				final int distance = packet.getIntegers().read(0);
				final float particleId = packet.getFloat().read(6);

				if (particleId == 0.15F && distance > 30) {
					final Player player = event.getPlayer(); // YOU CAN ONLY GET THE PLAYER IN THE PLAY STATE!
					final float explosionPower = (float) distance / 30F;

					Common.runLater(() -> player.getWorld().createExplosion(player.getLocation().subtract(0, 1, 0), explosionPower));
				}
			}
		});*/
	}
}

