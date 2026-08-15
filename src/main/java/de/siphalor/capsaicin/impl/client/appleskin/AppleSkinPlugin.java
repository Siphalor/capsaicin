package de.siphalor.capsaicin.impl.client.appleskin;

import de.siphalor.capsaicin.api.food.FoodContext;
import de.siphalor.capsaicin.api.food.FoodModifications;
import de.siphalor.capsaicin.api.food.FoodProperties;
import de.siphalor.capsaicin.impl.client.polymer.PolymerProxy;
import de.siphalor.capsaicin.impl.food.FoodContextImpl;
import de.siphalor.capsaicin.impl.food.properties.FoodPropertiesImpl;
import de.siphalor.capsaicin.impl.util.IItemStack;
import net.fabricmc.loader.api.FabricLoader;
import squeek.appleskin.api.AppleSkinApi;
import squeek.appleskin.api.event.FoodValuesEvent;

//- import java.util.Optional;
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
			//# if MC_VERSION_NUMBER >= 12102
			float defaultConsumeDuration = ((IItemStack)(Object) event.itemStack)
					.capsaicin$getVanillaConsumableComponent().consumeSeconds();
			//# elif MC_VERSION_NUMBER >= 12005
			//- float defaultConsumeDuration =
			//- 		Optional.ofNullable((IItemStack)(Object) event.itemStack)
			//- 				.map(IItemStack::capsaicin$getVanillaFoodComponent)
			//- 				.map(net.minecraft.world.food.FoodProperties::eatDurationTicks)
			//- 				.orElse(0);
			//# else
			//- // ItemStack#getUseDuration yields the modified consume duration
			//- int defaultConsumeDuration = event.itemStack.getItem().getUseDuration(event.itemStack);
			//# end

			FoodContext context = new FoodContextImpl(
					event.itemStack,
					null,
					//# if MC_VERSION_NUMBER >= 12005
					defaultFoodValues.nutrition(),
					defaultFoodValues.saturation(),
					//# else
					//- defaultFoodValues.hunger,
					//- defaultFoodValues.saturationModifier,
					//# end
					defaultConsumeDuration,
					event.player
			);
			FoodProperties newFoodProperties = FoodModifications.PROPERTIES_MODIFIERS.apply(foodProperties, context);
			boolean changed = foodProperties != newFoodProperties || foodProperties.isChanged();
			//# if MC_VERSION_NUMBER >= 12005 && MC_VERSION_NUMBER < 12102
			//- float newEatingTimeSeconds = FoodModifications.EATING_TIME_SECONDS_MODIFIERS.apply(
			//- 		event.modifiedFoodComponent.eatSeconds(),
			//- 		context
			//- );
			//- changed |= newEatingTimeSeconds != event.modifiedFoodComponent.eatSeconds();
			//# end
			if (changed) {
				//# if MC_VERSION_NUMBER >= 12005
				event.modifiedFoodComponent = new net.minecraft.world.food.FoodProperties(
						newFoodProperties.getHunger(),
						newFoodProperties.getSaturationModifier(),
						newFoodProperties.isAlwaysEdible()
						//# if MC_VERSION_NUMBER < 12102
						//- , newEatingTimeSeconds,
						//- /*# if MC_VERSION_NUMBER >= 12100 */defaultFoodValues.usingConvertsTo(),/*# end */
						//- newFoodProperties.getStatusEffects()
						//# end
				);
				//# else
				//- event.modifiedFoodValues = new FoodValues(newFoodProperties.getHunger(), newFoodProperties.getSaturationModifier());
				//# end
			}
		});
	}
}
