package de.siphalor.capsaicin.impl.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import de.siphalor.capsaicin.api.food.CamoFoodItem;
import de.siphalor.capsaicin.impl.food.FoodHandler;
import de.siphalor.capsaicin.impl.util.NullableOptional;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ConsumableListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

//# if MC_VERSION_NUMBER >= 12006
@Mixin(DataComponentHolder.class)
public interface MixinDataComponentHolder {
	@Shadow
	DataComponentMap getComponents();

	@Inject(method = "get", at = @At("HEAD"), cancellable = true)
	default <T> void get(DataComponentType<T> componentType, CallbackInfoReturnable<T> cir) {
		handleGet(componentType).ifPresent(cir::setReturnValue);
	}

	@Inject(method = "getOrDefault", at = @At("HEAD"), cancellable = true)
	default <T> void getOrDefault(DataComponentType<T> componentType, T defaultValue, CallbackInfoReturnable<T> cir) {
		handleGet(componentType).ifPresent(value -> cir.setReturnValue(value == null ? defaultValue : value));
	}

	@Unique
	default <T> NullableOptional<T> handleGet(DataComponentType<T> componentType) {
		//noinspection ConstantValue
		if ((Object) this instanceof ItemStack stack) {
			if (componentType == DataComponents.FOOD) {
				if (stack.getItem() instanceof CamoFoodItem || getComponents().has(DataComponents.FOOD)) {
					//noinspection unchecked
					return NullableOptional.of((T) FoodHandler.INSTANCE.get().withStack(stack).getModifiedFoodComponent());
				}
				//# if MC_VERSION_NUMBER >= 12102
			} else if (componentType == DataComponents.CONSUMABLE) {
				if (stack.getItem() instanceof CamoFoodItem || getComponents().has(DataComponents.CONSUMABLE) && getComponents().has(DataComponents.FOOD)) {
					//noinspection unchecked
					return NullableOptional.of((T) FoodHandler.INSTANCE.get().withStack(stack).getModifiedConsumableComponent());
				}
				//# end
			}
		}
		return NullableOptional.empty();
	}

	//# if MC_VERSION_NUMBER >= 12102
	@WrapMethod(method = "getAllOfType")
	default <T> Stream<T> getAllOfType(Class<T> clazz, Operation<Stream<T>> operation) {
		//noinspection ConstantValue
		if ((Object) this instanceof ItemStack stack && clazz == ConsumableListener.class) {
			return operation.call(clazz).map(listener -> {
				if (listener instanceof FoodProperties) {
					//noinspection unchecked
					return (T) FoodHandler.INSTANCE.get().withStack(stack).getModifiedFoodComponent();
				}
				return listener;
			});
		}
		return operation.call(clazz);
	}
	//# end
}
//# end
