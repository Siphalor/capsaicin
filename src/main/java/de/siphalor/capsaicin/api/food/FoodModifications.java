package de.siphalor.capsaicin.api.food;

import de.siphalor.capsaicin.api.modifier.Modifiers;
import de.siphalor.capsaicin.impl.modifier.UniqueModifiers;

public class FoodModifications {
	private FoodModifications() {}

	/**
	 * Modifier registry for food properties.
	 * @apiNote Never call {@link net.minecraft.world.item.Item#getFoodProperties()},
	 * or {@code stack.get(DataComponents.FOOD)} respectively,
	 * from within a properties modifier.
	 */
	public static final Modifiers<FoodProperties, FoodContext> PROPERTIES_MODIFIERS = new UniqueModifiers<>();
	//# if MC_VERSION_NUMBER >= 12102
	/**
	 * Modifier registry for consumable food components.
	 * @apiNote Never call {@code stack.get(DataComponents.CONSUMABLE)} from within a consumable modifier.
	 */
	public static final Modifiers<ConsumableProperties, FoodContext> CONSUMABLE_MODIFIERS = new UniqueModifiers<>();
	//# elif MC_VERSION_NUMBER >= 12005
	//- /**
	//-  * Modifier registry for the stack eating time (max use time) in seconds.<br />
	//-  * This modifier is currently only used for items.
	//-  * @apiNote Never call {@link net.minecraft.world.item.ItemStack#getUseDuration()} or
	//-  * {@code stack.get(DataComponents.FOOD)}
	//-  * from within an eating time modifier.
	//-  */
	//- public static final Modifiers<Float, FoodContext> EATING_TIME_SECONDS_MODIFIERS = new UniqueModifiers<>();
	//# else
	//- /**
	//-  * Modifier registry for the stack eating time (max use time) in ticks.<br />
	//-  * This modifier is currently only used for items.
	//-  * @apiNote Never call {@link net.minecraft.world.item.ItemStack#getUseDuration()} from within an eating time modifier.
	//-  */
	//- public static final Modifiers<Integer, FoodContext> EATING_TIME_MODIFIERS = new UniqueModifiers<>();
	//# end
}
