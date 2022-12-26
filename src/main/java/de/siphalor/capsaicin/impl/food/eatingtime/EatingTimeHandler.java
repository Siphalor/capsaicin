package de.siphalor.capsaicin.impl.food.eatingtime;

import de.siphalor.capsaicin.api.food.FoodModifications;
import de.siphalor.capsaicin.impl.food.FoodContextImpl;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class EatingTimeHandler {
	public static LivingEntity user;

	@SuppressWarnings("unused") // Used via ASM in the mixin config
	public static int getEatingTime(ItemStack stack, int eatingTime) {
		return FoodModifications.EATING_TIME_MODIFIERS.apply(eatingTime, new FoodContextImpl(stack, null, user));
	}
}
