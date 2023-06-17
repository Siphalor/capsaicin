package de.siphalor.capsaicin.impl.food.properties;

import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.api.food.FoodProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
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
	private boolean alwaysEdible;
	private @NotNull List<Pair<StatusEffectInstance, Float>> statusEffects;

	public static FoodPropertiesImpl from(@NotNull FoodComponent foodComponent) {
		return new FoodPropertiesImpl(
				foodComponent.getHunger(),
				foodComponent.getSaturationModifier(),
				foodComponent.isAlwaysEdible(),
				new ArrayList<>(foodComponent.getStatusEffects())
		);
	}

	public FoodPropertiesImpl(int hunger, float saturationModifier, boolean alwaysEdible, @NotNull List<Pair<StatusEffectInstance, Float>> statusEffects) {
		this.hunger = hunger;
		this.saturationModifier = saturationModifier;
		this.alwaysEdible = alwaysEdible;
		this.statusEffects = new ReactiveList<>(statusEffects);
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

	@Override
	public void setStatusEffects(@NotNull List<Pair<StatusEffectInstance, Float>> statusEffects) {
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
