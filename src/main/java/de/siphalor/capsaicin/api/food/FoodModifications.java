package de.siphalor.capsaicin.api.food;

import de.siphalor.capsaicin.api.modifier.Modifiers;
import de.siphalor.capsaicin.impl.modifier.UniqueModifiers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FoodModifications {
	private FoodModifications() {}

	/**
	 * Modifier registry for food properties.
	 * @apiNote Never call {@link Item#getFoodComponent()} from within a properties modifier.
	 */
	public static final Modifiers<FoodProperties, FoodContext> PROPERTIES_MODIFIERS = new UniqueModifiers<>();
	/**
	 * Modifier registry for the stack eating time (max use time).<br />
	 * This modifier is currently only used for items.
	 * @apiNote Never call {@link ItemStack#getMaxUseTime()} from within an eating time modifier.
	 */
	public static final Modifiers<Integer, FoodContext> EATING_TIME_MODIFIERS = new UniqueModifiers<>();
}
