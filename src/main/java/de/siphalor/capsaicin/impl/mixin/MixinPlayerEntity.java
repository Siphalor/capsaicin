package de.siphalor.capsaicin.impl.mixin;

import de.siphalor.capsaicin.impl.food.GenericFoodHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class, PlayerEntity.class})
public class MixinPlayerEntity {
	@Inject(method = "eatFood", at = @At("HEAD"))
	public void onEatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> callbackInfoReturnable) {
		GenericFoodHandler.currentUser = (LivingEntity)(Object) this;
		GenericFoodHandler.currentStack = stack;
	}
}
