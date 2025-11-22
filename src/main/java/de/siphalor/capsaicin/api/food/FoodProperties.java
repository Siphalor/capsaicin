package de.siphalor.capsaicin.api.food;

//- import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.impl.food.properties.FoodPropertiesImpl;
//- import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Custom editable collection of food related properties.<br />
 * This class when then project used Yarn mappings.
 * The naming conflict with {@link net.minecraft.world.food.FoodProperties} is unintentional.
 * For the time being, you'll have to use the fully qualified name for either of these classes.
 */
public interface FoodProperties {
	/**
	 * Gets the hunger value.
	 * @return the hunger value
	 */
	int getHunger();

	/**
	 * Sets the hunger value.
	 * @param hunger the new hunger value
	 */
	void setHunger(int hunger);

	/**
	 * Gets the saturation modifier.
	 * @return the saturation modifier
	 */
	float getSaturationModifier();

	/**
	 * Sets the saturation modifier.
	 * @param saturationModifier the new saturation modifier
	 */
	void setSaturationModifier(float saturationModifier);

	//# if MC_VERSION_NUMBER >= 12005
	/**
	 * Gets the time in seconds it takes to eat one unit of food.
	 * @return the time in seconds
	 */
	float getEatingTimeInSeconds();

	/**
	 * Sets the time in seconds it takes to eat one unit of food.
	 * @param eatingTimeInSeconds the new time in seconds
	 */
	void setEatingTimeInSeconds(float eatingTimeInSeconds);
	//# end

	/**
	 * Gets whether the item is always edible.
	 * @return whether the item is always edible
	 */
	boolean isAlwaysEdible();

	/**
	 * Sets whether the item is always edible.
	 * @param alwaysEdible whether the item is always edible
	 */
	void setAlwaysEdible(boolean alwaysEdible);

	/**
	 * Gets the list of status effects applied when consuming the stack.
	 * @return the list of status effects, may be mutated
	 */
	//# if MC_VERSION_NUMBER >= 12005
	@NotNull List<net.minecraft.world.food.FoodProperties.PossibleEffect> getStatusEffects();
	//# else
	//- @NotNull List<Pair<MobEffectInstance, Float>> getStatusEffects();
	//# end

	/**
	 * Sets the list of status effects applied when consuming the stack.
	 * @apiNote The list must be mutable.
	 * @param statusEffects the new, mutable list of status effects
	 */
	//# if MC_VERSION_NUMBER >= 12005
	void setStatusEffects(@NotNull List<net.minecraft.world.food.FoodProperties.PossibleEffect> statusEffects);
	//# else
	//- void setStatusEffects(@NotNull List<Pair<MobEffectInstance, Float>> statusEffects);
	//# end

	/**
	 * Gets whether any properties have been changed
	 * @return whether any properties have been changed
	 */
	boolean isChanged();

	/**
	 * Creates a new food properties instance from the given food component.
	 * @param foodComponent the food component
	 * @return the food properties instance
	 */
	@Contract("_ -> new")
	static @NotNull FoodProperties from(@NotNull net.minecraft.world.food.FoodProperties foodComponent) {
		return FoodPropertiesImpl.from(foodComponent);
	}
}
