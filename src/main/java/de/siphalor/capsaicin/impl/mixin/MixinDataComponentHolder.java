package de.siphalor.capsaicin.impl.mixin;

import de.siphalor.capsaicin.impl.food.FoodHandler;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//# if MC_VERSION_NUMBER >= 12006
@Mixin(DataComponentHolder.class)
public interface MixinDataComponentHolder {
	@Shadow
	DataComponentMap getComponents();

	@Inject(method = "get", at = @At("HEAD"), cancellable = true)
	default <T> void get(DataComponentType<T> componentType, CallbackInfoReturnable<T> cir) {
		//noinspection ConstantValue
		if ((Object) this instanceof ItemStack stack && componentType == DataComponents.FOOD) {
			FoodProperties foodProperties = getComponents().get(DataComponents.FOOD);
			if (foodProperties != null) {
				//noinspection unchecked
				cir.setReturnValue((T) FoodHandler.INSTANCE.get().withStack(stack).getModifiedFoodComponent());
			}
		}
	}

	@Inject(method = "getOrDefault", at = @At("HEAD"), cancellable = true)
	default <T> void getOrDefault(DataComponentType<T> componentType, T defaultValue, CallbackInfoReturnable<T> cir) {
		//noinspection ConstantValue
		if ((Object) this instanceof ItemStack stack && componentType == DataComponents.FOOD) {
			FoodProperties foodProperties = getComponents().get(DataComponents.FOOD);
			if (foodProperties != null) {
				//noinspection unchecked
				cir.setReturnValue((T) FoodHandler.INSTANCE.get().withStack(stack).getModifiedFoodComponent());
			}
		}
	}
}
//# end
