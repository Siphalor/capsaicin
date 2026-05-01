package de.siphalor.capsaicin.testmod;

//- import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.api.food.FoodContext;
import de.siphalor.capsaicin.api.food.FoodEvents;
import de.siphalor.capsaicin.api.food.FoodModifications;
//- import de.siphalor.capsaicin.api.food.PlayerFoodModifier;
//- import de.siphalor.capsaicin.api.food.PlayerFoodModifier;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
//- import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
//- import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class CapsaicinTestmod implements ModInitializer {
	@Override
	public void onInitialize() {
		//# if MC_VERSION_NUMBER < 12103
		//- FoodModifications.EATING_TIME_SECONDS_MODIFIERS.register((PlayerFoodModifier<Float>) (eatingTime, context, player) -> {
		//- 	float hungerRatio = 1F + player.getFoodData().getFoodLevel() / 10F;
		//- 	return eatingTime * hungerRatio;
		//- }, createId("test"));
		//# elif MC_VERSION_NUMBER < 12005
		//- FoodModifications.EATING_TIME_MODIFIERS.register((PlayerFoodModifier<Integer>) (eatingTime, context, player) -> {
		//- 	float hungerRatio = 1F + player.getFoodData().getFoodLevel() / 10F;
		//- 	return (int) (eatingTime * hungerRatio);
		//- }, createId("test"));
		//# end

		//# if MC_VERSION_NUMBER >= 12102
		FoodModifications.CONSUMABLE_MODIFIERS.register((consumableProperties, context) -> {
			ItemStack stack = context.stack();
			if (stack == null) {
				return consumableProperties;
			} else if (stack.getItem() == Items.COD) {
				consumableProperties.getOnConsumeEffects().add(new ApplyStatusEffectsConsumeEffect(
						new MobEffectInstance(MobEffects.POISON, 20 * 5, 2)
				));
			} else if (context.user() instanceof Player player) {
				float hungerRatio = 1F + player.getFoodData().getFoodLevel() / 10F;
				consumableProperties.setConsumeSeconds(consumableProperties.getConsumeSeconds() * hungerRatio);
			}
			return consumableProperties;
		}, createId("test"));
		//# end

		FoodModifications.PROPERTIES_MODIFIERS.register((foodProperties, context) -> {
			ItemStack stack = context.stack();
			if (stack == null) {
				return foodProperties;
			//# if MC_VERSION_NUMBER < 12102
			//- } else if (stack.getItem() == Items.COD) {
			//- 	foodProperties.getStatusEffects().add(
			//- 			//# if MC_VERSION_NUMBER >= 12005
			//- 			new FoodProperties.PossibleEffect(new MobEffectInstance(MobEffects.POISON, 20 * 5, 2), 1F)
			//- 			//# else
			//- 			Pair.of(new MobEffectInstance(MobEffects.POISON, 20 * 5, 2), 1F)
			//- 			//# end
			//- 	);
			//# end
			} else if (stack.getItem() == Items.COOKIE) {
				foodProperties.setAlwaysEdible(true);
			} else if (stack.getItem() == Items.COOKED_BEEF) {
				foodProperties.setHunger(foodProperties.getHunger() / 2);
			}
			return foodProperties;
		}, createId("test"));

		FoodEvents.EATEN.on(event -> {
			FoodContext context = event.context();
			if (context.stack() != null) {
				System.out.println("Ate from stack: " + context.stack());
			} else {
				System.out.println("Ate from block: " + context.blockState());
			}
		});
	}

	//# if MC_VERSION_NUMBER >= 12111
	private Identifier createId(String path) {
		return Identifier.fromNamespaceAndPath("capsaicin-testmod", path);
	}
	//# else
	//- private ResourceLocation createId(String path) {
	//- 	//# if MC_VERSION_NUMBER >= 12100
	//- 	return ResourceLocation.fromNamespaceAndPath("capsaicin-testmod", path);
	//- 	//# else
	//- 	return new ResourceLocation("capsaicin-testmod", path);
	//- 	//# end
	//- }
	//# end
}
