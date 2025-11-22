package de.siphalor.capsaicin.impl.food.properties;

//- import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.api.food.FoodProperties;
//- import de.siphalor.capsaicin.impl.util.MutationDetectingList;
import lombok.Getter;
//- import lombok.RequiredArgsConstructor;
//- import net.minecraft.world.effect.MobEffectInstance;
//- import net.minecraft.world.item.component.Consumable;
//- import net.minecraft.world.item.consume_effects.ConsumeEffect;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

//- import java.util.ArrayList;
//- import java.util.List;

@ApiStatus.Internal
@Getter
public class FoodPropertiesImpl implements FoodProperties {
	private boolean changed;
	private int hunger;
	private float saturationModifier;
	private boolean alwaysEdible;
	//# if MC_VERSION_NUMBER < 12102
	//- //# if MC_VERSION_NUMBER >= 12005
	//- private @NotNull List<net.minecraft.world.food.FoodProperties.PossibleEffect> statusEffects;
	//- //# else
	//- private @NotNull List<Pair<MobEffectInstance, Float>> statusEffects;
	//- //# end
	//# end

	public static FoodPropertiesImpl from(@NotNull net.minecraft.world.food.FoodProperties foodComponent) {
		//# if MC_VERSION_NUMBER >= 12005
		return new FoodPropertiesImpl(
				foodComponent.nutrition(),
				foodComponent.saturation(),
				foodComponent.canAlwaysEat()
				/*# if MC_VERSION_NUMBER < 12102 *//*- , foodComponent.effects() *//*# end */
		);
		//# else
		//- return new FoodPropertiesImpl(
		//- 		foodComponent.getNutrition(),
		//- 		foodComponent.getSaturationModifier(),
		//- 		foodComponent.canAlwaysEat(),
		//- 		foodComponent.getEffects()
		//- );
		//# end
	}

	public static FoodPropertiesImpl copy(@NotNull FoodProperties foodProperties) {
		return new FoodPropertiesImpl(
				foodProperties.getHunger(),
				foodProperties.getSaturationModifier(),
				foodProperties.isAlwaysEdible()
				/*# if MC_VERSION_NUMBER < 12102 *//*- , foodComponent.getStatusEffects() *//*# end */
		);
	}

	public FoodPropertiesImpl(
			int hunger,
			float saturationModifier,
			boolean alwaysEdible
			//# if MC_VERSION_NUMBER >= 12102
			//# elif MC_VERSION_NUMBER >= 12005
			//- , @NotNull List<net.minecraft.world.food.FoodProperties.PossibleEffect> statusEffects
			//# else
			//- , @NotNull List<Pair<MobEffectInstance, Float>> statusEffects
			//# end
	) {
		this.hunger = hunger;
		this.saturationModifier = saturationModifier;
		this.alwaysEdible = alwaysEdible;
		//# if MC_VERSION_NUMBER < 12102
		//- this.statusEffects = new MutationDetectingList<>(new ArrayList<>(statusEffects), () -> changed = true);
		//# end
	}

	@Override
	public void setHunger(int hunger) {
		if (this.hunger != hunger) {
			this.hunger = hunger;
			changed = true;
		}
	}

	@Override
	public void setSaturationModifier(float saturationModifier) {
		if (this.saturationModifier != saturationModifier) {
			this.saturationModifier = saturationModifier;
			changed = true;
		}
	}

	@Override
	public void setAlwaysEdible(boolean alwaysEdible) {
		if (this.alwaysEdible != alwaysEdible) {
			this.alwaysEdible = alwaysEdible;
			changed = true;
		}
	}

	//# if MC_VERSION_NUMBER < 12102
	//- @Override
	//- //# if MC_VERSION_NUMBER >= 12005
	//- public void setStatusEffects(@NotNull List<net.minecraft.world.food.FoodProperties.PossibleEffect> statusEffects) {
	//- //# else
	//- public void setStatusEffects(@NotNull List<Pair<MobEffectInstance, Float>> statusEffects) {
	//- //# end
	//- 	if (this.statusEffects != statusEffects) {
	//- 		this.statusEffects = statusEffects;
	//- 		changed = true;
	//- 	}
	//- }
	//# end
}
