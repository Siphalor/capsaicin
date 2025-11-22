package de.siphalor.capsaicin.impl.mixin;

//- import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.api.food.FoodEvents;
import de.siphalor.capsaicin.impl.food.FoodHandler;
import de.siphalor.capsaicin.impl.food.event.EatenEvent;
import de.siphalor.capsaicin.impl.food.properties.FoodPropertiesImpl;
import net.minecraft.core.BlockPos;
//- import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
//- import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//- import java.util.ArrayList;
//- import java.util.Collections;

@ApiStatus.Internal
@Mixin(CakeBlock.class)
public class MixinCakeBlock {
	@Inject(method = "eat", at = @At("HEAD"))
	private static void onEat(LevelAccessor world, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<InteractionResult> cir) {
		FoodHandler foodHandler = FoodHandler.INSTANCE.get();
		foodHandler.reset();
		foodHandler.withUser(player);
		//# if MC_VERSION_NUMBER >= 12102
		foodHandler.withBlockState(state, new FoodPropertiesImpl(2, 0.1F, false));
		//# else
		//- foodHandler.withBlockState(state, new FoodPropertiesImpl(2, 0.1F, false, Collections.emptyList()));
		//# end
	}

	@Redirect(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"))
	private static void eat(FoodData hungerManager, int hunger, float saturationModifier) {
		FoodHandler foodHandler = FoodHandler.INSTANCE.get();
		FoodProperties foodComponent = foodHandler.getModifiedFoodComponent();

		if (foodComponent != null) {
			LivingEntity user = foodHandler.getUser();
			//# if MC_VERSION_NUMBER >= 12005
			hungerManager.eat(foodComponent.nutrition(), foodComponent.saturation());
			//# if MC_VERSION_NUMBER >= 12102
			Consumable consumableComponent = foodHandler.getModifiedConsumableComponent();
			if (consumableComponent != null && !consumableComponent.onConsumeEffects().isEmpty()) {
				for (ConsumeEffect onConsumeEffect : consumableComponent.onConsumeEffects()) {
					onConsumeEffect.apply(user.level(), ItemStack.EMPTY, user);
				}
			}
			//# else
			//- RandomSource random = user.getRandom();
			//- for (FoodProperties.PossibleEffect effect : foodComponent.effects()) {
			//- 	if (random.nextFloat() < effect.probability()) {
			//- 		user.addEffect(effect.effect());
			//- 	}
			//- }
			//# end
			//# else
			//- hungerManager.eat(foodComponent.getNutrition(), foodComponent.getSaturationModifier());
			//- RandomSource random = user.getRandom();
			//- for (Pair<MobEffectInstance, Float> effect : foodComponent.getEffects()) {
				//- if (random.nextFloat() < effect.getSecond()) {
					//- user.addEffect(effect.getFirst());
				//- }
			//- }
			//# end
			FoodEvents.EATEN.emit(new EatenEvent(foodHandler.createContext()));
		}

		foodHandler.reset();
	}
}
