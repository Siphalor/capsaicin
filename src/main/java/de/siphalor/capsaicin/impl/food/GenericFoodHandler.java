package de.siphalor.capsaicin.impl.food;

import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.api.food.FoodContext;
import de.siphalor.capsaicin.api.food.FoodModifications;
import de.siphalor.capsaicin.api.food.FoodProperties;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class GenericFoodHandler {
	public static ItemStack currentStack;
	public static BlockState currentBlockState;
	public static LivingEntity currentUser;

	public static void reset() {
		currentStack = null;
		currentBlockState = null;
		currentUser = null;
	}

	public static boolean canApply() {
		return currentStack != null || currentBlockState != null;
	}

	public static FoodContext createContext() {
		if (canApply()) {
			return new FoodContextImpl(currentStack, currentBlockState, currentUser);
		}
		return null;
	}

	public static FoodComponent getFoodComponent(FoodComponent foodComponent) {
		FoodProperties propertiesIn = FoodProperties.from(foodComponent);
		FoodProperties propertiesOut = getFoodProperties(propertiesIn);
		if (propertiesOut == propertiesIn && !propertiesIn.isChanged()) {
			return foodComponent;
		}

		FoodComponent.Builder builder = new FoodComponent.Builder()
				.hunger(propertiesOut.getHunger())
				.saturationModifier(propertiesOut.getSaturationModifier());
		if (propertiesOut.isAlwaysEdible()) {
			builder.alwaysEdible();
		}
		for (Pair<StatusEffectInstance, Float> statusEffect : propertiesOut.getStatusEffects()) {
			builder.statusEffect(statusEffect.getFirst(), statusEffect.getSecond());
		}
		if (foodComponent.isSnack()) {
			builder.snack();
		}
		if (foodComponent.isMeat()) {
			builder.meat();
		}
		return builder.build();
	}

	public static FoodProperties getFoodProperties(FoodProperties foodProperties) {
		return FoodModifications.PROPERTIES_MODIFIERS.apply(foodProperties, createContext());
	}
}
