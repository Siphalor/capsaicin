package de.siphalor.capsaicin.impl.client.appleskin;

import de.siphalor.capsaicin.api.food.FoodModificationRegistry;
import de.siphalor.capsaicin.api.food.FoodProperties;
import de.siphalor.capsaicin.impl.food.FoodModificationContextImpl;
import de.siphalor.capsaicin.impl.food.properties.FoodPropertiesImpl;
import squeek.appleskin.api.AppleSkinApi;
import squeek.appleskin.api.event.FoodValuesEvent;
import squeek.appleskin.api.food.FoodValues;

import java.util.ArrayList;

/**
 * AppleSkin plugin for Capsaicin so that Capsaicin's food properties are visualized correctly.
 */
public class AppleSkinPlugin implements AppleSkinApi {
	@Override
	public void registerEvents() {
		FoodValuesEvent.EVENT.register(event -> {
			FoodProperties foodProperties = new FoodPropertiesImpl(event.modifiedFoodValues.hunger, event.modifiedFoodValues.saturationModifier, false, new ArrayList<>());
			FoodProperties newFoodProperties = FoodModificationRegistry.PROPERTIES_MODIFIERS.apply(foodProperties, new FoodModificationContextImpl(event.itemStack, event.player));
			if (foodProperties != newFoodProperties || foodProperties.isChanged()) {
				event.modifiedFoodValues = new FoodValues(newFoodProperties.getHunger(), newFoodProperties.getSaturationModifier());
			}
		});
	}
}
