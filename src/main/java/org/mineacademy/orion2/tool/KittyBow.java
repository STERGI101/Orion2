package org.mineacademy.orion2.tool;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.mineacademy.fo.BlockUtil;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.tool.Tool;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompMetadata;
import org.mineacademy.orion2.util.OrionUtil;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KittyBow extends Tool implements Listener {

	@Getter
	private final static Tool instance = new KittyBow();

	@Override
	public ItemStack getItem() {
		return ItemCreator.of(CompMaterial.STICK,
				"&bKitty Bow",
				"",
				"Rightclick air to launch",
				"explosive arrows...",
				"PS: They will damage blocks!").glow(true)
				.build().make();
	}

	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		if(!(event.getEntity().getShooter() instanceof Player))
			return;

		Projectile projectile = event.getEntity();

		Player player = (Player) projectile.getShooter();

		if(ItemUtil.isSimilar(player.getItemInHand(), getItem()))
			return;
		if(!OrionUtil.checkKittyArrow(player, event)) {

			event.setCancelled(true);
			return;
		}

		CompMetadata.setMetadata(projectile, "KittyArrow");
	}

	private final Map<Location, Vector> explodedLocations = new HashMap<Location, org.bukkit.util.Vector>();

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event){
		if(!CompMetadata.hasMetadata(event.getEntity(),"KittyArrow")){
			return;
		}

		Projectile projectile = event.getEntity();
		projectile.remove();

		explodedLocations.put(projectile.getLocation().getBlock().getLocation(), projectile.getVelocity());

		projectile.getWorld().createExplosion(projectile.getLocation(),4F);
	}

	@EventHandler
	public void onBlockExplode(BlockExplodeEvent event){
		Vector vector = explodedLocations.remove(event.getBlock().getLocation());

		if(vector != null){
			for(Block block : event.blockList())
				if(RandomUtil.chanceD(0.45))
					BlockUtil.shootBlock(block,vector);
				else
					block.setType(CompMaterial.AIR.getMaterial());

			event.setYield(0F);
		}

	}


	@Override
	protected void onBlockClick(PlayerInteractEvent event) {
		super.onBlockClick(event);
	}
}
