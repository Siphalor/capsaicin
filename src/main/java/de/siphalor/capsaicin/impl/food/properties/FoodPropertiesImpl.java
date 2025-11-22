package de.siphalor.capsaicin.impl.food.properties;

//- import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.api.food.FoodProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
//- import lombok.RequiredArgsConstructor;
//- import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ApiStatus.Internal
@Getter
public class FoodPropertiesImpl implements FoodProperties {
	private boolean changed;
	private int hunger;
	private float saturationModifier;
	//# if MC_VERSION_NUMBER >= 12005
	private float eatingTimeInSeconds;
	//# end
	private boolean alwaysEdible;
	//# if MC_VERSION_NUMBER >= 12005
	private @NotNull List<net.minecraft.world.food.FoodProperties.PossibleEffect> statusEffects;
	//# else
	//- private @NotNull List<Pair<MobEffectInstance, Float>> statusEffects;
	//# end

	public static FoodPropertiesImpl from(@NotNull net.minecraft.world.food.FoodProperties foodComponent) {
		//# if MC_VERSION_NUMBER >= 12005
		return new FoodPropertiesImpl(
				foodComponent.nutrition(),
				foodComponent.saturation(),
				foodComponent.eatSeconds(),
				foodComponent.canAlwaysEat(),
				foodComponent.effects()
		);
		//# else
		//- return new FoodPropertiesImpl(
		//- 		foodComponent.getNutrition(),
		//- 		foodComponent.getSaturationModifier(),
		//- 		foodComponent.canAlwaysEat(),
		//- 		new ArrayList<>(foodComponent.getEffects())
		//- );
		//# end
	}

	public FoodPropertiesImpl(
			int hunger,
			float saturationModifier,
			//# if MC_VERSION_NUMBER >= 12005
			float eatingTimeInSeconds,
			boolean alwaysEdible,
			@NotNull List<net.minecraft.world.food.FoodProperties.PossibleEffect> statusEffects
			//# else
			//- boolean alwaysEdible,
			//- @NotNull List<Pair<MobEffectInstance, Float>> statusEffects
			//# end
	) {
		this.hunger = hunger;
		this.saturationModifier = saturationModifier;
		//# if MC_VERSION_NUMBER >= 12005
		this.eatingTimeInSeconds = eatingTimeInSeconds;
		//# end
		this.alwaysEdible = alwaysEdible;
		this.statusEffects = new ReactiveList<>(new ArrayList<>(statusEffects));
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

	//# if MC_VERSION_NUMBER >= 12005
	@Override
	public void setEatingTimeInSeconds(float eatingTimeInSeconds) {
		if (this.eatingTimeInSeconds != eatingTimeInSeconds) {
			this.eatingTimeInSeconds = eatingTimeInSeconds;
			changed = true;
		}
	}
	//# end

	@Override
	public void setAlwaysEdible(boolean alwaysEdible) {
		if (this.alwaysEdible != alwaysEdible) {
			this.alwaysEdible = alwaysEdible;
			changed = true;
		}
	}

	@Override
	//# if MC_VERSION_NUMBER >= 12005
	public void setStatusEffects(@NotNull List<net.minecraft.world.food.FoodProperties.PossibleEffect> statusEffects) {
	//# else
	//- public void setStatusEffects(@NotNull List<Pair<MobEffectInstance, Float>> statusEffects) {
	//# end
		if (this.statusEffects != statusEffects) {
			this.statusEffects = statusEffects;
			changed = true;
		}
	}

	@EqualsAndHashCode(callSuper = false)
	class ReactiveList<T> extends AbstractList<T> {
		private final @NotNull List<T> delegate;

		ReactiveList(@NotNull List<T> delegate) {
			this.delegate = delegate;
		}

		@Override
		public T get(int index) {
			return delegate.get(index);
		}

		@Override
		public int size() {
			return delegate.size();
		}

		@Override
		public void add(int index, T element) {
			delegate.add(index, element);
			changed = true;
		}

		@Override
		public boolean addAll(@NotNull Collection<? extends T> c) {
			boolean superChanged = delegate.addAll(c);
			if (superChanged) {
				changed = true;
			}
			return superChanged;
		}

		@Override
		public T set(int index, T element) {
			T old = delegate.set(index, element);
			changed = true;
			return old;
		}

		@Override
		public T remove(int index) {
			T old = delegate.remove(index);
			changed = true;
			return old;
		}

		@Override
		public void clear() {
			delegate.clear();
			changed = true;
		}
	}
}
