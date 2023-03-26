package de.siphalor.capsaicin.impl.food;

import de.siphalor.capsaicin.api.food.CamoFoodContext;
import net.minecraft.entity.LivingEntity;

public record CamoFoodContextImpl(LivingEntity user) implements CamoFoodContext {

}
