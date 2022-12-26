package de.siphalor.capsaicin.testmod;

import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.api.food.FoodContext;
import de.siphalor.capsaicin.api.food.FoodEvents;
import de.siphalor.capsaicin.api.food.FoodModifications;
import de.siphalor.capsaicin.api.food.PlayerFoodModifier;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class CapsaicinTestmod implements ModInitializer {
	@Override
	public void onInitialize() {
		FoodModifications.EATING_TIME_MODIFIERS.register((PlayerFoodModifier<Integer>) (eatingTime, context, player) -> {
			float hungerRatio = 1F + player.getHungerManager().getFoodLevel() / 10F;
			return (int) (eatingTime * hungerRatio);
		}, new Identifier("capsaicin-testmod", "test"));
		FoodModifications.PROPERTIES_MODIFIERS.register((foodProperties, context) -> {
			ItemStack stack = context.stack();
			if (stack == null) {
				return foodProperties;
			}
			if (stack.getItem() == Items.COD) {
				foodProperties.getStatusEffects().add(Pair.of(new StatusEffectInstance(StatusEffects.POISON, 20 * 5, 2), 1F));
			} else if (stack.getItem() == Items.COOKIE) {
				foodProperties.setAlwaysEdible(true);
			} else if (stack.getItem() == Items.COOKED_BEEF) {
				foodProperties.setHunger(foodProperties.getHunger() / 2);
			}
			return foodProperties;
		}, new Identifier("capsaicin-testmod", "test"));
		FoodEvents.EATEN.on(event -> {
			FoodContext context = event.context();
			if (context.stack() != null) {
				System.out.println("Ate from stack: " + context.stack());
			} else {
				System.out.println("Ate from block: " + context.blockState());
			}
		});
	}
}
