package de.siphalor.capsaicin.api.food;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface CamoFoodContext {
	@Nullable LivingEntity user();
}
