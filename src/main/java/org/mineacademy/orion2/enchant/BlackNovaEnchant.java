package org.mineacademy.orion2.enchant;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.SimpleEnchantment;
import org.mineacademy.fo.remain.CompParticle;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.remain.Remain;

// Enchants are registered in bukkit automatically when you load your plugin
// OBS: Removing your plugin will remove them from your items
public class BlackNovaEnchant extends SimpleEnchantment /*implements Listener*/ {

	@Getter
	private static final Enchantment instance = new BlackNovaEnchant();

	private BlackNovaEnchant() {
		super("Black Nova", 5);
	}

	// OBS! This will be called for all events so you must check if the involved item stack
	// has this enchant using ItemStack#containsEnchantment
	/*@EventHandler
	public void onInventory(InventoryClickEvent event) {
		ItemStack item = event.getCursor();

		if (item != null && item.containsEnchantment(this)) {
			// run your code here
		}
	}*/

	// Called automatically only when the damager is a living entity having a hand item containing this enchant
	@Override
	protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof LivingEntity))
			return;

		if (damager instanceof Player) {
			final Player player = (Player) damager;
			//final PlayerCache cache = PlayerCache.getCache(player); // --> you can connect it to your class system here

			final int cooldown = Remain.getCooldown(player, Material.IRON_SWORD);

			if (cooldown > 0) {
				Common.tellNoPrefix(player, "&8[&4RPG&8] &cYour tool is loading, wait " + Common.plural(cooldown / 20 + 1, "second") + ".");

				event.setCancelled(true);
				return;
			}

			Remain.setCooldown(player, Material.IRON_SWORD, 5 * 20);
		}

		final LivingEntity victim = (LivingEntity) event.getEntity();

		// Edit: If you want the cool sound and particle you seen in the demo, uncomment the lines below
		CompSound.BLAZE_HIT.play(damager.getLocation());
		CompParticle.CRIT.spawn(victim.getLocation());

		victim.setFireTicks(3 * 20);
		victim.setVelocity(damager.getEyeLocation().getDirection().multiply(4).setY(5));
	}
}
