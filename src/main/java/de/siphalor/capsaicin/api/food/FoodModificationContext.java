package de.siphalor.capsaicin.api.food;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FoodModificationContext {
	@NotNull ItemStack getStack();
	@Nullable LivingEntity getUser();
}
