package de.siphalor.capsaicin.impl.mixin.client.appleskin;

import de.siphalor.capsaicin.impl.util.IItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import squeek.appleskin.helpers.FoodHelper;

@Mixin(FoodHelper.class)
public class MixinFoodHelper {
	private MixinFoodHelper() {}

	@Redirect(method = { "canConsume", "getDefaultFoodValues" }, at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/Item;getFoodProperties()Lnet/minecraft/world/food/FoodProperties;")
	)
	private static FoodProperties getFoodComponent(Item item) {
		if (item instanceof IItem iitem) {
			return iitem.capsaicin$getVanillaFoodComponent();
		}
		return item.getFoodProperties();
	}

}
