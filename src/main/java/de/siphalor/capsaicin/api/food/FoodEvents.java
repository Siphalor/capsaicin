package de.siphalor.capsaicin.api.food;

import de.siphalor.capsaicin.api.event.EventRegistry;
import de.siphalor.capsaicin.impl.event.EventRegistryImpl;
import org.jetbrains.annotations.NotNull;

public class FoodEvents {
	private FoodEvents() {}

	/**
	 * Event called after the food's hunger and saturation values have been calculated.
	 * It is undefined whether the status effects and other side effects have been applied yet.
	 */
	public static final EventRegistry<Eaten> EATEN = new EventRegistryImpl<>();

	public interface Eaten {
		@NotNull FoodContext context();
	}
}
