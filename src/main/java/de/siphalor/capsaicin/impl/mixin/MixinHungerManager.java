package de.siphalor.capsaicin.impl.mixin;

import de.siphalor.capsaicin.api.food.FoodEvents;
import de.siphalor.capsaicin.impl.food.FoodHandler;
import de.siphalor.capsaicin.impl.food.event.EatenEvent;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ApiStatus.Internal
@Mixin(HungerManager.class)
public class MixinHungerManager {
	@Inject(method = "eat", at = @At("HEAD"))
	public void onEat(Item item, ItemStack stack, CallbackInfo ci) {
		FoodHandler.INSTANCE.get().withStack(stack);
	}

	@Inject(method = "eat", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/entity/player/HungerManager;add(IF)V"))
	public void onEaten(Item item, ItemStack stack, CallbackInfo ci) {
		FoodEvents.EATEN.emit(new EatenEvent(FoodHandler.INSTANCE.get().createContext()));
	}
}
