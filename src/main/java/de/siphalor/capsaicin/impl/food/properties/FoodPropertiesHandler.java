package de.siphalor.capsaicin.impl.food.properties;

import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.api.food.FoodModificationContext;
import de.siphalor.capsaicin.api.food.FoodModificationRegistry;
import de.siphalor.capsaicin.api.food.FoodProperties;
import de.siphalor.capsaicin.impl.food.FoodModificationContextImpl;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class FoodPropertiesHandler {
	public static ItemStack currentStack;
	public static LivingEntity currentUser;

	public static void reset() {
		currentStack = null;
		currentUser = null;
	}

	public static boolean canApply() {
		return currentStack != null;
	}

	public static FoodComponent getFoodComponent(FoodComponent foodComponent) {
		FoodProperties properties = FoodProperties.from(foodComponent);
		FoodModificationContext context = new FoodModificationContextImpl(currentStack, currentUser);
		FoodProperties finalProperties = FoodModificationRegistry.PROPERTIES_MODIFIERS.apply(properties, context);
		if (finalProperties == properties || !properties.isChanged()) {
			return foodComponent;
		} else {
			FoodComponent.Builder builder = new FoodComponent.Builder()
					.hunger(finalProperties.getHunger())
					.saturationModifier(finalProperties.getSaturationModifier());
			if (finalProperties.isAlwaysEdible()) {
				builder.alwaysEdible();
			}
			for (Pair<StatusEffectInstance, Float> statusEffect : finalProperties.getStatusEffects()) {
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
	}
}
