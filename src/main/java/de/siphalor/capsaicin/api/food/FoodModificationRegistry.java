package de.siphalor.capsaicin.api.food;

import de.siphalor.capsaicin.api.modifier.Modifiers;
import de.siphalor.capsaicin.impl.modifier.UniqueModifiers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FoodModificationRegistry {
	/**
	 * Modifier registry for food properties.
	 * @apiNote Never call {@link Item#getFoodComponent()} from within a properties modifier.
	 */
	public static final Modifiers<FoodProperties, FoodModificationContext> PROPERTIES_MODIFIERS = new UniqueModifiers<>();
	/**
	 * Modifier registry for the stack eating time (max use time).
	 * @apiNote Never call {@link ItemStack#getMaxUseTime()} from within an eating time modifier.
	 */
	public static final Modifiers<Integer, FoodModificationContext> EATING_TIME_MODIFIERS = new UniqueModifiers<>();
}
