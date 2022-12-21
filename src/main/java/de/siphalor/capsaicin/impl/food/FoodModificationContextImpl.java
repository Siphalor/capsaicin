package de.siphalor.capsaicin.impl.food;

import de.siphalor.capsaicin.api.food.FoodModificationContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class FoodModificationContextImpl implements FoodModificationContext {
	private final ItemStack stack;
	private final LivingEntity user;

	public FoodModificationContextImpl(ItemStack stack, LivingEntity user) {
		this.stack = stack;
		this.user = user;
	}

	@Override
	public @NotNull ItemStack getStack() {
		return stack;
	}

	@Override
	public @Nullable LivingEntity getUser() {
		return user;
	}
}
