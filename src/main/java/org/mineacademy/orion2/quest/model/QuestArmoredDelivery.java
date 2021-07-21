package org.mineacademy.orion2.quest.model;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.orion2.boss.npc.NPC;
import org.mineacademy.orion2.PlayerCache;
import org.mineacademy.orion2.classes.model.ClassBase;

public final class QuestArmoredDelivery extends Quest {

	private static final CompMaterial DELIVERY_ITEM = CompMaterial.DIAMOND_HORSE_ARMOR;

	protected QuestArmoredDelivery() {
		super("Armored Delivery");
	}

	@Override
	public Quest getNext(final ClassBase classBase) {
		return null;
	}

	@Override
	public CompMaterial getIcon() {
		return DELIVERY_ITEM;
	}

	@Override
	public String[] getMenuLore() {
		return new String[] {
				"Deliver a horse armor",
				"to Sir Richard at the",
				"gate to Stavanger!"
		};
	}

	@Override
	public void onRightClickNPC(final PlayerCache cache, final Player player, final NPC npc, final PlayerInteractEntityEvent event) {

		// Prevent opening trader menu
		event.setCancelled(true);

		if (player.getInventory().contains(DELIVERY_ITEM.getMaterial())) {
			cache.completeQuest();

			PlayerUtil.takeFirstOnePiece(player, DELIVERY_ITEM);
			npc.tell(player, "&2Thank you very much, you will now be rewarded!");

			Common.runLater(20 * 4, () -> {
				npc.tell(player, "&CMuhahaha, that's what you get!");

				player.getWorld().strikeLightningEffect(player.getLocation());
			});

		} else
			npc.tell(player, "&cI am waiting for your diamond horse armor!");
	}

	@Override
	public String getCompletion(final Player player, final Object completionData) {
		final boolean hasHorseArmor = player.getInventory().contains(DELIVERY_ITEM.getMaterial());

		return hasHorseArmor ? "Sir Richard is waiting for you!" : "Collect Diamond Horse Armor";
	}
}