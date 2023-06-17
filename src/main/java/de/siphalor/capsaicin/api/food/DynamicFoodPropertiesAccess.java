package de.siphalor.capsaicin.api.food;

import de.siphalor.capsaicin.impl.food.FoodHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Experimental
public interface DynamicFoodPropertiesAccess {
	/**
	 * Creates a new instance in the current execution context
	 * (certain data is inherited from the current execution context).
	 * @return the new instance
	 */
	@Contract(value = " -> new", pure = true)
	static @NotNull DynamicFoodPropertiesAccess create() {
		return FoodHandler.createInheriting();
	}

	/**
	 * Sets the stack to be used for the calculations.
	 * Will remove block state data.
	 * @param stack the stack
	 * @return this instance
	 */
	@Contract("_ -> this")
	@NotNull DynamicFoodPropertiesAccess withStack(@NotNull ItemStack stack);

	/**
	 * Sets the block state to be used for the calculations.
	 * Will remove stack data.
	 * @param blockState the block state
	 * @param foodProperties the food properties
	 * @return this instance
	 */
	@Contract("_, _ -> this")
	@NotNull DynamicFoodPropertiesAccess withBlockState(@NotNull BlockState blockState, @NotNull FoodProperties foodProperties);

	/**
	 * Sets the user to be used for the calculations.
	 * @param user the user
	 * @return this instance
	 */
	@Contract("_ -> this")
	@NotNull DynamicFoodPropertiesAccess withUser(@NotNull LivingEntity user);

	/**
	 * Gets the basic, unmodified food component.
	 * @return the food component
	 */
	@Nullable FoodComponent getStackOriginalFoodComponent();

	/**
	 * Returns whether the instance is ready to be used.
	 * Either a stack or a block state must be set.<br />
	 * If <code>false</code> is returned, {@link #getModifiedFoodComponent()} will return <code>null</code>
	 * and {@link #getModifiedEatingTime()} will return <code>0</code>.
	 * @return whether the instance is ready
	 */
	boolean isReady();

	/**
	 * Gets the modified food component or <code>null</code> if the instance is not ready.
	 * @return the food component
	 * @see #isReady()
	 */
	@Nullable FoodComponent getModifiedFoodComponent();

	/**
	 * Gets the modified eating time or <code>0</code> if the instance is not ready.
	 * @return the eating time
	 * @see #isReady()
	 */
	int getModifiedEatingTime();
}
