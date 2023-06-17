package de.siphalor.capsaicin.impl.mixin.client.polymer;

import de.siphalor.capsaicin.impl.food.FoodHandler;
import eu.pb4.polymer.core.api.client.ClientPolymerItem;
import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import eu.pb4.polymer.core.impl.client.compat.AppleSkinCompatibility;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
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
	private static void fixPolymerAppleskin(FoodValuesEvent event, CallbackInfo ci, Identifier id, ClientPolymerItem polymerItem) {
		Item actualItem = polymerItem.registryEntry();
		if (actualItem != null) {
			FoodHandler foodHandler = FoodHandler.INSTANCE.get();
			ItemStack actualStack = new ItemStack(actualItem, event.itemStack.getCount());
			NbtCompound realNbt = event.itemStack.getSubNbt(PolymerItemUtils.REAL_TAG);
			if (realNbt != null) {
				actualStack.setNbt(realNbt);
			}
			foodHandler.withUser(MinecraftClient.getInstance().player).withStack(actualStack);
			FoodComponent originalFoodComponent = foodHandler.getStackOriginalFoodComponent();
			if (originalFoodComponent != null) {
				event.defaultFoodValues = new FoodValues(originalFoodComponent.getHunger(), originalFoodComponent.getSaturationModifier());
			}
			FoodComponent foodComponent = foodHandler.getModifiedFoodComponent();
			if (foodComponent != null) {
				event.modifiedFoodValues = new FoodValues(foodComponent.getHunger(), foodComponent.getSaturationModifier());
			}
			foodHandler.reset();
			ci.cancel();
		}
	}
}
