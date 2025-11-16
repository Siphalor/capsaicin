package de.siphalor.capsaicin.impl.mixin;

import de.siphalor.capsaicin.impl.food.FoodHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class, Player.class})
public class MixinPlayerEntity {
	@Inject(method = "eat", at = @At("HEAD"))
	public void onEatFood(Level world, ItemStack stack, CallbackInfoReturnable<ItemStack> callbackInfoReturnable) {
		FoodHandler foodHandler = FoodHandler.INSTANCE.get();
		foodHandler.reset();
		foodHandler.withUser((LivingEntity) (Object) this).withStack(stack);
	}
}
