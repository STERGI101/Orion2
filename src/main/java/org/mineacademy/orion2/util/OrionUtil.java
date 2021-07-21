package org.mineacademy.orion2.util;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.orion2.tool.KittyCannon;

public class OrionUtil {

	public static boolean checkKittyArrow(Player player, Cancellable event){
		ItemStack arrow = PlayerUtil.getFirstItem(player, KittyCannon.getInstance().getItem());

		if(arrow == null){
			Common.tell(player, "&cYou lack the Kitty Arrow required to shoot from this tool!");

			event.setCancelled(true);
			return false;
		}

		if(player.getGameMode() == GameMode.SURVIVAL)
			PlayerUtil.takeOnePiece(player, arrow);

		return true;
	}
}
