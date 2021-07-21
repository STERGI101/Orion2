package org.mineacademy.orion2.command;

import com.google.common.collect.Sets;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Goal;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.ai.goals.TargetNearbyEntityGoal;
import net.citizensnpcs.api.npc.MetadataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.remain.CompAttribute;

public class NpcTestCommand extends SimpleCommand {

	public NpcTestCommand() {
		super("npctest");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		final NPCRegistry registry = CitizensAPI.getNPCRegistry();

		// NB! This only registers the NPC in Citizens but does not spawn it yet
		// Once you register it you can only spawn it once
		final NPC npc = registry.createNPC(EntityType.WOLF, Common.colorize("&cPet"));

		npc.spawn(player.getTargetBlock(null, 5).getLocation().add(0, 1, 0));

		// Make the NPC vulnerable to attacks
		npc.setProtected(false);

		{
			// Create a new pathfinder goal to target nearby entities
			final Goal goal = TargetNearbyEntityGoal.builder(npc)
					// Attack the target
					.aggressive(true)
					// Search for targets in 12 blocks distance
					.radius(12)
					// Target villagers and chickens
					.targets(Sets.newHashSet(EntityType.VILLAGER, EntityType.CHICKEN))
					.build();

			// Add this goal with priority #1
			npc.getDefaultGoalController().addGoal(goal, 1);
		}

		{
			final Navigator navigator = npc.getNavigator();

			navigator.getLocalParameters()
					// What is the delay between each hit? We set it to one second
					.attackDelayTicks(20)

					// What is the delay between recalculating the best route to the target?
					// We set it to 0.25 second
					.updatePathRate(5);
		}

		{
			final MetadataStore metadata = npc.data();

			// For names, see the SoundEffects class in the NMS package
			metadata.set(NPC.HURT_SOUND_METADATA, "entity.guardian.hurt");
			metadata.set(NPC.AMBIENT_SOUND_METADATA, "entity.guardian.ambient");
			metadata.set(NPC.DEATH_SOUND_METADATA, "entity.guardian.death");
		}


		{
			// Now that the entity is spawned, we can simply
			// get the Bukkit class instance from it and modify it directly just
			// as you would normally
			final Wolf wolf = (Wolf) npc.getEntity();

			CompAttribute.GENERIC_MOVEMENT_SPEED.set(wolf, 0.5);
			CompAttribute.GENERIC_ATTACK_SPEED.set(wolf, 8);
		}
	}
}
