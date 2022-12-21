package de.siphalor.capsaicin.impl.mixin;

import de.siphalor.capsaicin.impl.food.properties.FoodPropertiesHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Item.class)
public class MixinItem {
	@Shadow @Final private @Nullable FoodComponent foodComponent;

	@Inject(method = "getFoodComponent", at = @At("TAIL"), cancellable = true)
	public void onGetFoodComponent(CallbackInfoReturnable<FoodComponent> cir) {
		if (FoodPropertiesHandler.canApply()) {
			FoodComponent newFoodComponent = FoodPropertiesHandler.getFoodComponent(foodComponent);
			if (newFoodComponent != foodComponent) {
				cir.setReturnValue(newFoodComponent);
			}
		}
	}

	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getFoodComponent()Lnet/minecraft/item/FoodComponent;"), locals = LocalCapture.CAPTURE_FAILHARD)
	public void onUseFood(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir, ItemStack stack) {
		FoodPropertiesHandler.currentUser = player;
		FoodPropertiesHandler.currentStack = stack;
	}

	@Inject(method = "use", at = @At("RETURN"))
	public void onUseReturn(CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		FoodPropertiesHandler.reset();
	}
}
