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
		@Nullable LivingEntity user
) implements FoodContext {

}
