package de.siphalor.capsaicin.api.food;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
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

	//# if MC_VERSION_NUMBER >= 12102
	/**
	 * The consume duration in seconds <b>before any modifications</b>.
	 * @return the consume duration in fractional seconds
	 */
	float originalFoodConsumeDurationSeconds();
	//# elif MC_VERSION_NUMBER >= 12005
	//- /**
	//-  * The consume duration in seconds <b>before any modifications</b>.
	//-  * @return the consume duration in fractional seconds
	//-  */
	//- float originalFoodEatingTimeSeconds();
	//# else
	//- /**
	//-  * The eating time in ticks <b>before any modifications</b>.
	//-  * @return the consume duration in game ticks
	//-  */
	//- int originalFoodEatingTimeTicks();
	//# end
}
