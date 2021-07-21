package org.mineacademy.orion2.event;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.remain.CompParticle;
import org.mineacademy.orion2.OrionPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ProjectileListener implements Listener {

	private final Set<UUID> shotEggs = new HashSet<>();

	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		Projectile shot = event.getEntity();



		if(shot.getShooter() instanceof Player){
			if(shot instanceof Egg){
				shotEggs.add(shot.getUniqueId());
			}
			else if(shot instanceof Arrow){
				new BukkitRunnable() {
					@Override
					public void run(){
						if(!shot.isValid() || shot.isOnGround()){
							cancel();

							return;
						}
						CompParticle.ENCHANTMENT_TABLE.spawn(shot.getLocation());

					}
				}.runTaskTimer(OrionPlugin.getInstance(), 0, 1);
			}



		}
	}
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		Projectile shot = event.getEntity();
		if(shot instanceof Egg && shotEggs.contains(shot.getUniqueId())){
			shotEggs.remove(shot.getUniqueId());

			shot.getWorld().spawn(shot.getLocation(), Creeper.class);
		}
	}


}
