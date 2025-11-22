package de.siphalor.capsaicin.impl.mixin;

//- import de.siphalor.capsaicin.api.food.FoodEvents;
import de.siphalor.capsaicin.impl.food.FoodHandler;
//- import de.siphalor.capsaicin.impl.food.event.EatenEvent;
import net.minecraft.world.entity.LivingEntity;
//- import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
//- import net.minecraft.world.level.Level;
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

	//# if MC_VERSION_NUMBER < 12102
	//- @Inject(method = "getUseItemRemainingTicks", at = @At("HEAD"))
	//- public void onGetItemUseTimeLeft(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
	//- 	FoodHandler foodHandler = FoodHandler.INSTANCE.get();
	//- 	foodHandler.reset();
	//- 	foodHandler.withUser((LivingEntity) (Object) this);
	//- }

	//- @Inject(
	//- 		method = "eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
	//- 		at = @At("RETURN")
	//- )
	//- public void onEatFoodReturn(Level world, ItemStack stack, CallbackInfoReturnable<ItemStack> callbackInfoReturnable) {
	//- 	FoodHandler.INSTANCE.get().reset();
	//- }

	//- //# if MC_VERSION_NUMBER >= 12100
	//- @Inject(
	//- 		method = "eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/ItemStack;",
	//- 		at = @At(
	//- 				value = "INVOKE",
	//- 				target = "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V"
	//- 		)
	//- )
	//- public void onEaten(
	//- 		Level level,
	//- 		ItemStack food,
	//- 		FoodProperties foodProperties,
	//- 		CallbackInfoReturnable<ItemStack> cir
	//- ) {
	//- 	FoodEvents.EATEN.emit(new EatenEvent(FoodHandler.INSTANCE.get().createContext()));
	//- }
	//- //# end
	//# end
}
