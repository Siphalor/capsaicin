package de.siphalor.capsaicin.api.food;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Context for food related events.
 */
public interface FoodContext {
	/**
	 * The item stack that is currently being processed.
	 * @return the stack
	 */
	@Nullable ItemStack stack();

	/**
	 * The block state that is currently being processed.
	 * @return the block state
	 */
	@Nullable BlockState blockState();

	/**
	 * The entity that is using or somehow else acting on the stack.
	 * @return the entity or <code>null</code> if no entity is known in the current context
	 */
	@Nullable LivingEntity user();

	/**
	 * The amount of hunger that is restored <b>before any modifications</b>.
	 * @return the amount of hunger
	 */
	int originalFoodHunger();

	/**
	 * The saturation modifier <b>before any modifications</b>.
	 * @return the saturation modifier
	 */
	float originalFoodSaturationModifier();
}
