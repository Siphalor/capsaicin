package de.siphalor.capsaicin.impl.food.eatingtime;

import de.siphalor.capsaicin.impl.food.FoodHandler;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings("unused") // Used via ASM in the mixin config
@ApiStatus.Internal
public class EatingTimeHandler {
	private EatingTimeHandler() {}

	public static int getEatingTime(ItemStack stack, int eatingTime) {
		FoodHandler foodHandler = FoodHandler.INSTANCE.get();
		if (foodHandler.isReady()) {
			foodHandler.withStack(stack);
			return foodHandler.getModifiedEatingTime();
		}
		return eatingTime;
	}
}
