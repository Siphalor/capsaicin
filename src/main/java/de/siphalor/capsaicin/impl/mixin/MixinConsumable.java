package de.siphalor.capsaicin.impl.mixin;

import de.siphalor.capsaicin.api.food.FoodEvents;
import de.siphalor.capsaicin.impl.food.FoodHandler;
import de.siphalor.capsaicin.impl.food.event.EatenEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//# if MC_VERSION_NUMBER >= 12102
@Mixin(Consumable.class)
public class MixinConsumable {
	@Inject(method = "onConsume", at = @At("HEAD"))
	public void onConsume(Level level, LivingEntity entity, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		FoodHandler foodHandler = FoodHandler.INSTANCE.get();
		foodHandler.reset();
		foodHandler.withUser(entity).withStack(stack);
	}

	@Inject(
			method = "onConsume",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V"
			)
	)
	public void onConsumed(Level level, LivingEntity entity, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		FoodEvents.EATEN.emit(new EatenEvent(FoodHandler.INSTANCE.get().createContext()));
		FoodHandler.INSTANCE.get().reset();
	}
}
//# end
