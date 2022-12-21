package de.siphalor.capsaicin.impl.mixin;

import de.siphalor.capsaicin.impl.food.properties.FoodPropertiesHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CatEntity.class)
public class MixinCatEntity {
	@Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getFoodComponent()Lnet/minecraft/item/FoodComponent;"), locals = LocalCapture.CAPTURE_FAILSOFT)
	public void onFeedMob(PlayerEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir, ItemStack stack, Item item) {
		FoodPropertiesHandler.currentUser = (LivingEntity) (Object) this;
		FoodPropertiesHandler.currentStack = stack;
	}

	@Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getFoodComponent()Lnet/minecraft/item/FoodComponent;", shift = At.Shift.AFTER))
	public void onMobFed(CallbackInfoReturnable<ActionResult> cir) {
		FoodPropertiesHandler.reset();
	}
}
