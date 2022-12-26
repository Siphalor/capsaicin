package de.siphalor.capsaicin.impl.food.event;

import de.siphalor.capsaicin.api.food.FoodContext;
import de.siphalor.capsaicin.api.food.FoodEvents;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public record EatenEvent(FoodContext context) implements FoodEvents.Eaten {

}
