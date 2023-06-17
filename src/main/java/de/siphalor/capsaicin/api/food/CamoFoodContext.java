package de.siphalor.capsaicin.api.food;

import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface CamoFoodContext {
	@Nullable LivingEntity user();
}
