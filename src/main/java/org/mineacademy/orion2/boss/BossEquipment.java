package org.mineacademy.orion2.boss;

import lombok.Data;
import org.mineacademy.fo.menu.model.ItemCreator;

@Data // --> constructor, getter and even setters if fields are not final
public class BossEquipment {

	private final ItemCreator.ItemCreatorBuilder helmet, chestplate, leggings, boots;
}
