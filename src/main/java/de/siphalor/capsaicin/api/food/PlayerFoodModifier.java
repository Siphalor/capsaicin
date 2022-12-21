package de.siphalor.capsaicin.api.food;

import de.siphalor.capsaicin.api.modifier.Modifier;
import net.minecraft.entity.player.PlayerEntity;

public interface PlayerFoodModifier<Value> extends Modifier<Value, FoodModificationContext> {
	@Override
	default Value apply(Value value, FoodModificationContext context) {
		if (context.getUser() instanceof PlayerEntity player) {
			return apply(value, context, player);
		}
		return value;
	}

	Value apply(Value value, FoodModificationContext context, PlayerEntity player);
}
