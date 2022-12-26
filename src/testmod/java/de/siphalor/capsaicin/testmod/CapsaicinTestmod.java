package de.siphalor.capsaicin.testmod;

import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.api.food.FoodModificationRegistry;
import de.siphalor.capsaicin.api.food.PlayerFoodModifier;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class CapsaicinTestmod implements ModInitializer {
	@Override
	public void onInitialize() {
		FoodModificationRegistry.EATING_TIME_MODIFIERS.register((PlayerFoodModifier<Integer>) (eatingTime, context, player) -> {
			float hungerRatio = 1F + player.getHungerManager().getFoodLevel() / 10F;
			return (int) (eatingTime * hungerRatio);
		}, new Identifier("capsaicin-testmod", "test"));
		FoodModificationRegistry.PROPERTIES_MODIFIERS.register((foodProperties, context) -> {
			if (context.getStack().getItem() == Items.COD) {
				foodProperties.getStatusEffects().add(Pair.of(new StatusEffectInstance(StatusEffects.POISON, 20 * 5, 2), 1F));
			} else if (context.getStack().getItem() == Items.COOKIE) {
				foodProperties.setAlwaysEdible(true);
			} else if (context.getStack().getItem() == Items.COOKED_BEEF) {
				foodProperties.setHunger(foodProperties.getHunger() / 2);
			}
			return foodProperties;
		}, new Identifier("capsaicin-testmod", "test"));
	}
}
