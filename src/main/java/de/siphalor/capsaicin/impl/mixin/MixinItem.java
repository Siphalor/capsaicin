package de.siphalor.capsaicin.impl.mixin;

import de.siphalor.capsaicin.impl.food.FoodHandler;
import de.siphalor.capsaicin.impl.util.IItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Item.class)
public class MixinItem implements IItem {
	@Shadow @Final private @Nullable FoodComponent foodComponent;

	@Inject(method = "getFoodComponent", at = @At("TAIL"), cancellable = true)
	public void onGetFoodComponent(CallbackInfoReturnable<FoodComponent> cir) {
		if (FoodHandler.INSTANCE.get().canApply()) {
			FoodComponent newFoodComponent = FoodHandler.INSTANCE.get().getFoodComponent();
			if (newFoodComponent != foodComponent) {
				cir.setReturnValue(newFoodComponent);
			}
		}
	}

	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getFoodComponent()Lnet/minecraft/item/FoodComponent;"), locals = LocalCapture.CAPTURE_FAILHARD)
	public void onUseFood(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir, ItemStack stack) {
		FoodHandler foodHandler = FoodHandler.INSTANCE.get();
		foodHandler.withStack(stack);
		foodHandler.withUser(player);
	}

	@Inject(method = "use", at = @At("RETURN"))
	public void onUseReturn(CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		FoodHandler.INSTANCE.get().reset();
	}

	@Override
	public FoodComponent capsaicin$getVanillaFoodComponent() {
		return foodComponent;
	}
}
