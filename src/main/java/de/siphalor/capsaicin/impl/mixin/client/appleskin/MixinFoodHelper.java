package de.siphalor.capsaicin.impl.mixin.client.appleskin;

import de.siphalor.capsaicin.impl.util.IItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import squeek.appleskin.helpers.FoodHelper;

@Mixin(FoodHelper.class)
public class MixinFoodHelper {
	private MixinFoodHelper() {}

	@Redirect(method = { "canConsume", "getDefaultFoodValues" }, at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/item/Item;getFoodComponent()Lnet/minecraft/item/FoodComponent;")
	)
	private static FoodComponent getFoodComponent(Item item) {
		if (item instanceof IItem iitem) {
			return iitem.capsaicin$getVanillaFoodComponent();
		}
		return item.getFoodComponent();
	}

}
