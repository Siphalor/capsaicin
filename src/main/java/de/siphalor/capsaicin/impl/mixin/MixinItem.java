package de.siphalor.capsaicin.impl.mixin;

import de.siphalor.capsaicin.impl.food.FoodHandler;
import de.siphalor.capsaicin.impl.util.IItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
	@Shadow @Final private @Nullable FoodProperties foodProperties;

	@Inject(method = "getFoodProperties", at = @At("TAIL"), cancellable = true)
	public void onGetFoodComponent(CallbackInfoReturnable<FoodProperties> cir) {
		FoodHandler foodHandler = FoodHandler.INSTANCE.get();
		if (foodHandler.isReady()) {
			FoodProperties newFoodComponent = foodHandler.getModifiedFoodComponent();
			if (newFoodComponent != foodProperties) {
				cir.setReturnValue(newFoodComponent);
			}
		}
	}

	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;getFoodProperties()Lnet/minecraft/world/food/FoodProperties;"), locals = LocalCapture.CAPTURE_FAILHARD)
	public void onUseFood(Level world, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir, ItemStack stack) {
		FoodHandler foodHandler = FoodHandler.INSTANCE.get();
		foodHandler.reset();
		foodHandler.withUser(player).withStack(stack);
	}

	@Inject(method = "use", at = @At("RETURN"))
	public void onUseReturn(CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
		FoodHandler.INSTANCE.get().reset();
	}

	@Override
	public FoodProperties capsaicin$getVanillaFoodComponent() {
		return foodProperties;
	}
}
