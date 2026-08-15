package de.siphalor.capsaicin.impl.food;

import de.siphalor.capsaicin.api.food.FoodContext;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public record FoodContextImpl(
		@Nullable ItemStack stack,
		@Nullable BlockState blockState,
		int originalFoodHunger,
		float originalFoodSaturationModifier,
		//# if MC_VERSION_NUMBER >= 12102
		float originalFoodConsumeDurationSeconds,
		//# elif MC_VERSION_NUMBER >= 12005
		//- float originalFoodEatingTimeSeconds,
		//# else
		//- int originalFoodEatingTimeTicks,
		//# end
		@Nullable LivingEntity user
) implements FoodContext {

}
