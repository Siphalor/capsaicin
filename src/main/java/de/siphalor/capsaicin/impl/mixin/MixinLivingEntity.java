package de.siphalor.capsaicin.impl.mixin;

import de.siphalor.capsaicin.impl.food.FoodHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
	@Inject(method = "getItemInHand", at = @At("HEAD"))
	public void onGetStackInHand(CallbackInfoReturnable<ItemStack> callbackInfoReturnable) {
		FoodHandler foodHandler = FoodHandler.INSTANCE.get();
		foodHandler.reset();
		foodHandler.withUser((LivingEntity) (Object) this);
	}

	@Inject(method = "getUseItemRemainingTicks", at = @At("HEAD"))
	public void onGetItemUseTimeLeft(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
		FoodHandler foodHandler = FoodHandler.INSTANCE.get();
		foodHandler.reset();
		foodHandler.withUser((LivingEntity) (Object) this);
	}

	@Inject(method = "eat", at = @At("RETURN"))
	public void onEatFoodReturn(Level world, ItemStack stack, CallbackInfoReturnable<ItemStack> callbackInfoReturnable) {
		FoodHandler.INSTANCE.get().reset();
	}
}
