package de.siphalor.capsaicin.impl.food.eatingtime;

import de.siphalor.capsaicin.api.food.FoodModificationRegistry;
import de.siphalor.capsaicin.impl.food.FoodModificationContextImpl;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class EatingTimeHandler {
	public static LivingEntity user;

	@SuppressWarnings("unused") // Used via ASM in the mixin config
	public static int getEatingTime(ItemStack stack, int eatingTime) {
		return FoodModificationRegistry.EATING_TIME_MODIFIERS.apply(eatingTime, new FoodModificationContextImpl(stack, user));
	}
}
