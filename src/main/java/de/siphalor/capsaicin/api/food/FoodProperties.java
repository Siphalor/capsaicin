package de.siphalor.capsaicin.api.food;

import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.impl.food.properties.FoodPropertiesImpl;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;

import java.util.List;

public interface FoodProperties {
	int getHunger();
	void setHunger(int hunger);
	float getSaturationModifier();
	void setSaturationModifier(float saturationModifier);
	boolean isAlwaysEdible();
	void setAlwaysEdible(boolean alwaysEdible);
	List<Pair<StatusEffectInstance, Float>> getStatusEffects();
	void setStatusEffects(List<Pair<StatusEffectInstance, Float>> statusEffects);

	boolean isChanged();

	static FoodProperties from(FoodComponent foodComponent) {
		return FoodPropertiesImpl.from(foodComponent);
	}
}
