package org.mineacademy.orion2.boss.npc;

import org.bukkit.Location;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.*;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.orion2.PlayerCache;
import org.mineacademy.orion2.boss.model.Boss;
import org.mineacademy.orion2.classes.model.ClassBase;
import org.mineacademy.orion2.quest.model.Quest;

import java.util.Arrays;

public class NPCSirRichard extends NPCCitizen {

	public NPCSirRichard() {
		super("Sir Richard", EntityType.VILLAGER);

		setCustomName("&f" + getName());
		setTellPrefix("&8[&6" + getName() + "&8] &7");
	}

	@Override
	protected void onNPCSpawn(final Location location, final LivingEntity entity) {
		Valid.checkBoolean(entity instanceof Villager, "NPC must be Villager but got " + entity.getClass().getSimpleName());

		((Villager) entity).setProfession(Profession.ARMORER); // Requires MC 1.14+
	}

	@Override
	protected void onNPCRightClick(final Player player, final LivingEntity entity, final PlayerInteractEntityEvent event) {
		final PlayerCache cache = PlayerCache.getCache(player);

		// Case 1 - already completed this quest
		if (cache.hasCompletedQuest(Quest.ARMORED_DELIVERY, ClassBase.WARRIOR)) {
			tell(player, "&cYou already completed this hidden quest!");

			return;
		}

		// Case 2 - not having any quest, check for enter criteria
		if (cache.getClassTier(ClassBase.WARRIOR) < 3) {
			tell(player, "&cOnly Warriors lvl 3+ may start this quest!");

			return;
		}

		// Case 3 - show prompt to start this quest
		new DeliverItemQuestPrompt().show(player);
	}

	@Override
	public void onTick(final LivingEntity bossEntity) {
		final int radius = 6;

		for (final Player player : Remain.getOnlinePlayers()) {
			final PlayerCache cache = PlayerCache.getCache(player);

			if (cache.hasCompletedQuest(Quest.ARMORED_DELIVERY, ClassBase.WARRIOR) || cache.hasActiveQuest(Quest.ARMORED_DELIVERY) || player.isConversing())
				continue;

			for (final Entity nearbyEntity : player.getNearbyEntities(radius, radius, radius)) {
				final Boss boss = findBoss(nearbyEntity);

				if (boss instanceof NPCSirRichard) {
					tellTimed(10, player, "&7I am here, " + player.getName() + "!");

					break;
				}
			}
		}
	}

	// --------------------------------------------------------------------------------------------------------------
	// Prompt
	// --------------------------------------------------------------------------------------------------------------

	private final class DeliverItemQuestPrompt extends SimplePrompt {

		@Override
		protected String getCustomPrefix() {
			return NPCSirRichard.this.getTellPrefix(); // Use the tell prefix from the super class instance
		}

		@Override
		protected String getPrompt(final ConversationContext ctx) {
			return "Will you deliver a Diamond Horse armor to me?";
		}

		@Override
		protected boolean isInputValid(final ConversationContext context, final String input) {
			return Arrays.asList("accept", "deny").contains(input);
		}

		@Override
		protected String getFailedValidationText(final ConversationContext context, final String invalidInput) {
			return "&cWhat is that? Type accept or deny.";
		}

		@Override
		protected Prompt acceptValidatedInput(final ConversationContext context, final String input) {
			if (input.equals("accept")) {
				final PlayerCache cache = PlayerCache.getCache(getPlayer(context));

				cache.startQuest(Quest.ARMORED_DELIVERY, ClassBase.WARRIOR);
				tell(context, "&6Let's begin Warrior, deliver me the goods!");
			} else
				tell(context, "&cSee you next time Warrior!");

			return Prompt.END_OF_CONVERSATION;
		}
	}
}