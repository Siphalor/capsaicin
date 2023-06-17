package de.siphalor.capsaicin.impl.mixin.client.appleskin;

import de.siphalor.capsaicin.api.food.CamoFoodContext;
import de.siphalor.capsaicin.api.food.CamoFoodItem;
import de.siphalor.capsaicin.impl.food.CamoFoodContextImpl;
import de.siphalor.capsaicin.impl.food.FoodHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import squeek.appleskin.helpers.DynamicFood;

/**
 * Sneakily implements AppleSkin's DynamicFood on CamoFoodItem, if AppleSkin is present.
 */
@Mixin(CamoFoodItem.class)
public interface MixinCamoFoodItem extends DynamicFood {
	@Shadow
	@Nullable ItemStack getCamoFoodStack(ItemStack stack, CamoFoodContext context);

	@Override
	default int getDynamicHunger(ItemStack stack, PlayerEntity player) {
		@Nullable ItemStack camoFoodStack = getCamoFoodStack(stack, new CamoFoodContextImpl(player));
		if (camoFoodStack == null) {
			return 0;
		}
		@Nullable FoodComponent foodComponent = FoodHandler.INSTANCE.get().withStack(stack).withUser(player).getModifiedFoodComponent();
		if (foodComponent == null) {
			return 0;
		}
		return foodComponent.getHunger();
	}

	@Override
	default float getDynamicSaturation(ItemStack stack, PlayerEntity player) {
		@Nullable ItemStack camoFoodStack = getCamoFoodStack(stack, new CamoFoodContextImpl(player));
		if (camoFoodStack == null) {
			return 0F;
		}
		@Nullable FoodComponent foodComponent = FoodHandler.INSTANCE.get().withStack(stack).withUser(player).getModifiedFoodComponent();
		if (foodComponent == null) {
			return 0F;
		}
		return foodComponent.getSaturationModifier();
	}
}
