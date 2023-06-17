package de.siphalor.capsaicin.api.food;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CamoFoodItem {
	@Nullable ItemStack getCamoFoodStack(@NotNull ItemStack stack, @NotNull CamoFoodContext context);
}
