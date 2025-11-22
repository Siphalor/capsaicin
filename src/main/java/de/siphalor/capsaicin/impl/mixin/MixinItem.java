package de.siphalor.capsaicin.impl.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.siphalor.capsaicin.impl.food.FoodHandler;
//- import de.siphalor.capsaicin.impl.util.IItem;
//- import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
//- import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
//- import org.jetbrains.annotations.Nullable;
//- import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
//- import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//- import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Item.class)
public abstract class MixinItem /*# if MC_VERSION_NUMBER < 12005 *//*- implements IItem *//*# end */
{
	//# if MC_VERSION_NUMBER < 12005
	//- @Shadow @Final private @Nullable FoodProperties foodProperties;

	//- @Inject(method = "getFoodProperties", at = @At("TAIL"), cancellable = true)
	//- public void onGetFoodComponent(CallbackInfoReturnable<FoodProperties> cir) {
	//- 	FoodHandler foodHandler = FoodHandler.INSTANCE.get();
	//- 	if (foodHandler.isReady()) {
	//- 		FoodProperties newFoodComponent = foodHandler.getModifiedFoodComponent();
	//- 		if (newFoodComponent != foodProperties) {
	//- 			cir.setReturnValue(newFoodComponent);
	//- 		}
	//- 	}
	//- }
	//# end

	@Inject(
			method = "use",
			//# if MC_VERSION_NUMBER >= 12005
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
			)
			//# else
			//- at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;getFoodProperties()Lnet/minecraft/world/food/FoodProperties;")
			//# end
	)
	public void onUseFood(Level world, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir, @Local ItemStack stack) {
		FoodHandler foodHandler = FoodHandler.INSTANCE.get();
		foodHandler.reset();
		foodHandler.withUser(player).withStack(stack);
	}

	@Inject(method = "use", at = @At("RETURN"))
	public void onUseReturn(CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
		FoodHandler.INSTANCE.get().reset();
	}

	//# if MC_VERSION_NUMBER < 12005
	//- @Override
	//- public FoodProperties capsaicin$getVanillaFoodComponent() {
	//- 	return foodProperties;
	//- }
	//# end
}
