package de.siphalor.capsaicin.api.food;

//# if MC_VERSION_NUMBER >= 12102

import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.List;

/**
 * Custom editable collection of general consumable properties.
 * @see net.minecraft.world.item.component.Consumable
 */
public interface ConsumableProperties {
	/**
	 * Gets the number of seconds it takes to consume an item.
	 */
	float getConsumeSeconds();

	/**
	 * Sets the number of seconds it takes to consume an item.
	 */
	void setConsumeSeconds(float consumeSeconds);

	/**
	 * Gets the <b>mutable</b> list of status effects applied when consuming the stack.
	 * @apiNote You're encouraged to mutate this list instead of setting a new one using {@link #setOnConsumeEffects(List)}.
	 */
	List<ConsumeEffect> getOnConsumeEffects();

	/**
	 * Sets the <b>mutable</b> list of status effects applied when consuming the stack.
	 * @param onConsumeEffects the new list of status effects, must be mutable
	 */
	void setOnConsumeEffects(List<ConsumeEffect> onConsumeEffects);

	/**
	 * Gets whether any properties have been changed
	 * @return whether any properties have been changed
	 */
	boolean isChanged();
}
//# end
