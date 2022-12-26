package de.siphalor.capsaicin.impl.food;

import de.siphalor.capsaicin.api.food.FoodModificationContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public record FoodModificationContextImpl(ItemStack stack, LivingEntity user) implements FoodModificationContext {
	@Override
	public @NotNull ItemStack getStack() {
		return stack();
	}

	@Override
	public @Nullable LivingEntity getUser() {
		return user();
	}
}
