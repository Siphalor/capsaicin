package de.siphalor.capsaicin.impl.mixin;

import de.siphalor.capsaicin.impl.food.GenericFoodHandler;
import de.siphalor.capsaicin.impl.food.eatingtime.EatingTimeHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
	@Inject(method = "getStackInHand", at = @At("HEAD"))
	public void onGetStackInHand(CallbackInfoReturnable<ItemStack> callbackInfoReturnable) {
		EatingTimeHandler.user = (LivingEntity)(Object) this;
	}

	@Inject(method = "getItemUseTimeLeft", at = @At("HEAD"))
	public void onGetItemUseTimeLeft(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
		EatingTimeHandler.user = (LivingEntity)(Object) this;
	}

	@Inject(method = "eatFood", at = @At("RETURN"))
	public void onEatFoodReturn(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> callbackInfoReturnable) {
		GenericFoodHandler.reset();
	}
}
