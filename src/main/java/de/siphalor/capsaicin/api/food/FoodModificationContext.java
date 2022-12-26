package de.siphalor.capsaicin.api.food;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Context for modifications in the food context.
 */
public interface FoodModificationContext {
	/**
	 * The item stack that is currently being processed.
	 * @return the stack
	 */
	@NotNull ItemStack getStack();

	/**
	 * The entity that is using or somehow else acting on the stack.
	 * @return the entity or <code>null</code> if no entity is known in the current context
	 */
	@Nullable LivingEntity getUser();
}
