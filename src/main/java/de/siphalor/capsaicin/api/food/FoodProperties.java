package de.siphalor.capsaicin.api.food;

import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.impl.food.properties.FoodPropertiesImpl;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;

import java.util.List;

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
	List<Pair<StatusEffectInstance, Float>> getStatusEffects();

	/**
	 * Sets the list of status effects applied when consuming the stack.
	 * @apiNote The list must be mutable.
	 * @param statusEffects the new, mutable list of status effects
	 */
	void setStatusEffects(List<Pair<StatusEffectInstance, Float>> statusEffects);

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
	static FoodProperties from(FoodComponent foodComponent) {
		return FoodPropertiesImpl.from(foodComponent);
	}
}
