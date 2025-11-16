package de.siphalor.capsaicin.impl.food;

import de.siphalor.capsaicin.api.food.CamoFoodContext;
import net.minecraft.world.entity.LivingEntity;

public record CamoFoodContextImpl(LivingEntity user) implements CamoFoodContext {

}
