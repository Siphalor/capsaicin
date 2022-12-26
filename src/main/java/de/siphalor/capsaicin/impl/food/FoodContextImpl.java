package de.siphalor.capsaicin.impl.food;

import de.siphalor.capsaicin.api.food.FoodContext;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public record FoodContextImpl(@Nullable ItemStack stack, @Nullable BlockState blockState, @Nullable LivingEntity user) implements FoodContext {

}
