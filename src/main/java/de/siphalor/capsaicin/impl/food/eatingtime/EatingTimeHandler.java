package de.siphalor.capsaicin.impl.food.eatingtime;

import de.siphalor.capsaicin.impl.food.FoodHandler;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class EatingTimeHandler {
	@SuppressWarnings("unused") // Used via ASM in the mixin config
	public static int getEatingTime(ItemStack stack, int eatingTime) {
		FoodHandler foodHandler = FoodHandler.INSTANCE.get();
		if (foodHandler.canApply()) {
			foodHandler.withStack(stack);
			return foodHandler.getEatingTime();
		}
		return eatingTime;
	}
}
