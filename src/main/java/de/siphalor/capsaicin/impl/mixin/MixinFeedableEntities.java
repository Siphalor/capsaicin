package de.siphalor.capsaicin.impl.mixin;

import de.siphalor.capsaicin.impl.food.FoodHandler;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.WolfEntity;
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

@Mixin({WolfEntity.class, CatEntity.class})
public class MixinFeedableEntities {
	@Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getFoodComponent()Lnet/minecraft/item/FoodComponent;"), locals = LocalCapture.CAPTURE_FAILSOFT)
	public void onFeedMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir, ItemStack stack, Item item) {
		FoodHandler foodHandler = FoodHandler.INSTANCE.get();
		foodHandler.withUser(player);
		foodHandler.withStack(stack);
	}

	@Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getFoodComponent()Lnet/minecraft/item/FoodComponent;", shift = At.Shift.AFTER))
	public void onMobFed(CallbackInfoReturnable<ActionResult> cir) {
		FoodHandler.INSTANCE.get().reset();
	}
}
