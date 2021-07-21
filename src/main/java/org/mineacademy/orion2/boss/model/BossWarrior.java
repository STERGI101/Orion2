package org.mineacademy.orion2.boss.model;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.SimpleEnchant;
import org.mineacademy.fo.remain.CompAttribute;
import org.mineacademy.fo.remain.CompColor;
import org.mineacademy.fo.remain.CompItemFlag;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.orion2.boss.skill.BossSkillBlockAttack;
import org.mineacademy.orion2.boss.skill.BossSkillLightning;
import org.mineacademy.orion2.boss.skill.BossSkillThrow;

public class BossWarrior extends Boss {

	protected BossWarrior() {
		super("Skeleton Warrior", EntityType.SKELETON);

		setCustomName("&4Skeleton Warrior");
		setHealth(5.0);

		setEquipment(
				buildWarriorArmor(CompMaterial.LEATHER_HELMET,
						"&cWarrior Helmet",
						"",
						"The legendary warrior",
						"armor with enchants."),

				buildWarriorArmor(CompMaterial.LEATHER_CHESTPLATE,
						"&cWarrior Chestplate",
						"",
						"The legendary warrior",
						"armor with enchants."),

				buildWarriorArmor(CompMaterial.LEATHER_LEGGINGS,
						"&cWarrior Leggings",
						"",
						"The legendary warrior",
						"armor with enchants."),

				buildWarriorArmor(CompMaterial.LEATHER_BOOTS,
						"&cWarrior Boots",
						"",
						"The legendary warrior",
						"armor with enchants."));

		setDroppedExp(500);
		addAttribute(CompAttribute.GENERIC_MOVEMENT_SPEED, 0.5);
		addPotionEffect(PotionEffectType.REGENERATION);
		setPassenger(EntityType.CAVE_SPIDER);

		addSkill(new BossSkillBlockAttack());
		addSkill(new BossSkillLightning());
		addSkill(new BossSkillThrow());
	}

	private ItemCreator.ItemCreatorBuilder buildWarriorArmor(final CompMaterial material, final String title, final String... lore) {
		return ItemCreator.of(material, title, lore)
				.enchant(new SimpleEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1))
				.color(CompColor.RED)
				.flag(CompItemFlag.HIDE_ATTRIBUTES);
	}
}
