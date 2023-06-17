package de.siphalor.capsaicin.api.food;

import de.siphalor.capsaicin.api.modifier.Modifier;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

/**
 * A food modifier that is only applied, if the using entity is a player.
 * @param <Value> The class of the value
 */
@FunctionalInterface
public interface PlayerFoodModifier<Value> extends Modifier<Value, FoodContext> {
	@Override
	default @NotNull Value apply(@NotNull Value value, @NotNull FoodContext context) {
		if (context.user() instanceof PlayerEntity player) {
			return apply(value, context, player);
		}
		return value;
	}

	/**
	 * Apply the modifications to the value and return an updated (or new) version.
	 * @param value The input value
	 * @param context Context for this modification
	 * @param player The player that is using the stack
	 * @return An updated or new value based on the input, context and player
	 */
	@NotNull Value apply(@NotNull Value value, @NotNull FoodContext context, @NotNull PlayerEntity player);
}
