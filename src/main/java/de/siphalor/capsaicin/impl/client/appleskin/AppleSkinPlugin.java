package de.siphalor.capsaicin.impl.client.appleskin;

import de.siphalor.capsaicin.api.food.FoodModifications;
import de.siphalor.capsaicin.api.food.FoodProperties;
import de.siphalor.capsaicin.impl.client.polymer.PolymerProxy;
import de.siphalor.capsaicin.impl.food.FoodContextImpl;
import de.siphalor.capsaicin.impl.food.properties.FoodPropertiesImpl;
import net.fabricmc.loader.api.FabricLoader;
import squeek.appleskin.api.AppleSkinApi;
import squeek.appleskin.api.event.FoodValuesEvent;
//- import squeek.appleskin.api.food.FoodValues;

//- import java.util.ArrayList;

/**
 * AppleSkin plugin for Capsaicin so that Capsaicin's food properties are visualized correctly.
 */
public class AppleSkinPlugin implements AppleSkinApi {
	private static final boolean POLYMER_LOADED = FabricLoader.getInstance().isModLoaded("polymer");

	@Override
	public void registerEvents() {
		FoodValuesEvent.EVENT.register(event -> {
			if (POLYMER_LOADED) {
				// We can now safely proceed to load Polymer classes
				if (PolymerProxy.isPolymerItem(event.itemStack)) {
					// Already handled by MixinPolymerAppleskinPlugin
					return;
				}
			}

			//# if MC_VERSION_NUMBER >= 12005
			FoodProperties foodProperties = FoodPropertiesImpl.from(event.modifiedFoodComponent);
			net.minecraft.world.food.FoodProperties defaultFoodValues = event.defaultFoodComponent;
			//# else
			//- FoodProperties foodProperties = new FoodPropertiesImpl(
			//- 		event.modifiedFoodValues.hunger,
			//- 		event.modifiedFoodValues.saturationModifier,
			//- 		false,
			//- 		new ArrayList<>()
			//- );
			//- FoodValues defaultFoodValues = event.defaultFoodValues;
			//# end
			FoodProperties newFoodProperties = FoodModifications.PROPERTIES_MODIFIERS.apply(
					foodProperties,
					new FoodContextImpl(
							event.itemStack,
							null,
							//# if MC_VERSION_NUMBER >= 12005
							defaultFoodValues.nutrition(),
							defaultFoodValues.saturation(),
							defaultFoodValues.eatSeconds(),
							//# else
							//- defaultFoodValues.hunger,
							//- defaultFoodValues.saturationModifier,
							//# end
							event.player
					)
			);
			if (foodProperties != newFoodProperties || foodProperties.isChanged()) {
				//# if MC_VERSION_NUMBER >= 12005
				event.modifiedFoodComponent = new net.minecraft.world.food.FoodProperties(
						newFoodProperties.getHunger(),
						newFoodProperties.getSaturationModifier(),
						newFoodProperties.isAlwaysEdible(),
						newFoodProperties.getEatingTimeInSeconds(),
						newFoodProperties.getStatusEffects()
				);
				//# else
				//- event.modifiedFoodValues = new FoodValues(newFoodProperties.getHunger(), newFoodProperties.getSaturationModifier());
				//# end
			}
		});
	}
}
