package de.siphalor.capsaicin.impl.mixin.client.polymer;

import de.siphalor.capsaicin.impl.food.FoodHandler;
import eu.pb4.polymer.core.api.client.ClientPolymerItem;
import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import eu.pb4.polymer.core.impl.client.compat.AppleSkinCompatibility;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import squeek.appleskin.api.event.FoodValuesEvent;
import squeek.appleskin.api.food.FoodValues;

@Mixin(AppleSkinCompatibility.class)
public class MixinPolymerAppleskinPlugin {
	private MixinPolymerAppleskinPlugin() {}

	@Inject(method = "lambda$registerEvents$0", remap = false, at = @At(
			value = "INVOKE",
			target = "Lsqueek/appleskin/api/food/FoodValues;<init>(IF)V"
	), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
	private static void fixPolymerAppleskin(FoodValuesEvent event, CallbackInfo ci, ResourceLocation id, ClientPolymerItem polymerItem) {
		Item actualItem = polymerItem.registryEntry();
		if (actualItem != null) {
			FoodHandler foodHandler = FoodHandler.INSTANCE.get();
			ItemStack actualStack = new ItemStack(actualItem, event.itemStack.getCount());
			CompoundTag realNbt = event.itemStack.getTagElement(PolymerItemUtils.REAL_TAG);
			if (realNbt != null) {
				actualStack.setTag(realNbt);
			}
			foodHandler.withUser(Minecraft.getInstance().player).withStack(actualStack);
			FoodProperties originalFoodComponent = foodHandler.getStackOriginalFoodComponent();
			if (originalFoodComponent != null) {
				event.defaultFoodValues = new FoodValues(originalFoodComponent.getNutrition(), originalFoodComponent.getSaturationModifier());
			}
			FoodProperties foodComponent = foodHandler.getModifiedFoodComponent();
			if (foodComponent != null) {
				event.modifiedFoodValues = new FoodValues(foodComponent.getNutrition(), foodComponent.getSaturationModifier());
			}
			foodHandler.reset();
			ci.cancel();
		}
	}
}
