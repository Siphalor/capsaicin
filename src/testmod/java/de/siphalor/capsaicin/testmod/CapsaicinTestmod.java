package de.siphalor.capsaicin.testmod;

//- import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.api.food.FoodContext;
import de.siphalor.capsaicin.api.food.FoodEvents;
import de.siphalor.capsaicin.api.food.FoodModifications;
//- import de.siphalor.capsaicin.api.food.PlayerFoodModifier;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CapsaicinTestmod implements ModInitializer {
	@Override
	public void onInitialize() {
		//# if MC_VERSION_NUMBER < 12005
		//- FoodModifications.EATING_TIME_MODIFIERS.register((PlayerFoodModifier<Integer>) (eatingTime, context, player) -> {
		//- 	float hungerRatio = 1F + player.getFoodData().getFoodLevel() / 10F;
		//- 	return (int) (eatingTime * hungerRatio);
		//- }, new ResourceLocation("capsaicin-testmod", "test"));
		//# end

		FoodModifications.PROPERTIES_MODIFIERS.register((foodProperties, context) -> {
			ItemStack stack = context.stack();
			if (stack == null) {
				return foodProperties;
			}
			if (stack.getItem() == Items.COD) {
				foodProperties.getStatusEffects().add(
						//# if MC_VERSION_NUMBER >= 12005
						new FoodProperties.PossibleEffect(new MobEffectInstance(MobEffects.POISON, 20 * 5, 2), 1F)
						//# else
						//- Pair.of(new MobEffectInstance(MobEffects.POISON, 20 * 5, 2), 1F)
						//# end
				);
			} else if (stack.getItem() == Items.COOKIE) {
				foodProperties.setAlwaysEdible(true);
			} else if (stack.getItem() == Items.COOKED_BEEF) {
				foodProperties.setHunger(foodProperties.getHunger() / 2);
			//# if MC_VERSION_NUMBER >= 12005
			} else if (context.user() instanceof Player player) {
				float hungerRatio = 1F + player.getFoodData().getFoodLevel() / 10F;
				foodProperties.setEatingTimeInSeconds(foodProperties.getEatingTimeInSeconds() * hungerRatio);
			//# end
			}
			return foodProperties;
		}, new ResourceLocation("capsaicin-testmod", "test"));

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
