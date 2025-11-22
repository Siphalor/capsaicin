package de.siphalor.capsaicin.api.food;

import de.siphalor.capsaicin.impl.food.FoodHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.block.state.BlockState;
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
	@Nullable net.minecraft.world.food.FoodProperties getStackOriginalFoodComponent();

	//# if MC_VERSION_NUMBER >= 12102
	/**
	 * Gets the basic, unmodified consumable component.
	 * @return the consumable component
	 */
	@Nullable Consumable getStackOriginalConsumableComponent();
	//# end

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
	@Nullable net.minecraft.world.food.FoodProperties getModifiedFoodComponent();

	//# if MC_VERSION_NUMBER >= 12102
	/**
	 * Gets the modified consumable component or <code>null</code> if the instance is not ready.
	 * @return the consumable component
	*/
	@Nullable Consumable getModifiedConsumableComponent();

	//# if MC_VERSION_NUMBER < 12005
	//- /**
	//-  * Gets the modified eating time or <code>0</code> if the instance is not ready.
	//-  * @return the eating time
	//-  * @see #isReady()
	//-  */
	//- int getModifiedEatingTime();
	//# end
}
