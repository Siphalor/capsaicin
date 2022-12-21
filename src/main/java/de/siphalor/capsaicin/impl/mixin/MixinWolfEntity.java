package de.siphalor.capsaicin.impl.mixin;

import de.siphalor.capsaicin.impl.food.properties.FoodPropertiesHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CatEntity.class)
public class MixinWolfEntity {
	@Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getFoodComponent()Lnet/minecraft/item/FoodComponent;"), locals = LocalCapture.CAPTURE_FAILSOFT)
	public void onFeedMob(CallbackInfoReturnable<ActionResult> cir, ItemStack stack) {
		FoodPropertiesHandler.currentUser = (LivingEntity) (Object) this;
		FoodPropertiesHandler.currentStack = stack;
	}

	@Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getFoodComponent()Lnet/minecraft/item/FoodComponent;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
	public void onMobFed(CallbackInfoReturnable<ActionResult> cir, ItemStack stack) {
		FoodPropertiesHandler.reset();
	}
}
