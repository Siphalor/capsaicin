package de.siphalor.capsaicin.impl.util;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;

public interface IItemStack {
	FoodProperties capsaicin$getVanillaFoodComponent();
	//# if MC_VERSION_NUMBER >= 12102
	Consumable capsaicin$getVanillaConsumableComponent();
	//# end
}
