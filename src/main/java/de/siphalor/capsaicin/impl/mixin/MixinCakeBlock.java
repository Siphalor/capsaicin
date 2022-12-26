package de.siphalor.capsaicin.impl.mixin;

import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.api.food.FoodEvents;
import de.siphalor.capsaicin.api.food.FoodProperties;
import de.siphalor.capsaicin.impl.food.GenericFoodHandler;
import de.siphalor.capsaicin.impl.food.event.EatenEvent;
import de.siphalor.capsaicin.impl.food.properties.FoodPropertiesImpl;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Random;

@ApiStatus.Internal
@Mixin(CakeBlock.class)
public class MixinCakeBlock {
	@Inject(method = "tryEat", at = @At("HEAD"))
	private static void onEat(WorldAccess world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<ActionResult> cir) {
		GenericFoodHandler.currentUser = player;
		GenericFoodHandler.currentBlockState = state;
		GenericFoodHandler.currentStack = null;
	}

	@Redirect(method = "tryEat", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;add(IF)V"))
	private static void eat(HungerManager hungerManager, int hunger, float saturationModifier) {
		FoodProperties foodProperties = GenericFoodHandler.getFoodProperties(new FoodPropertiesImpl(hunger, saturationModifier, false, new ArrayList<>()));

		LivingEntity user = GenericFoodHandler.currentUser;
		Random random = user.getRandom();
		hungerManager.add(foodProperties.getHunger(), foodProperties.getSaturationModifier());
		for (Pair<StatusEffectInstance, Float> effect : foodProperties.getStatusEffects()) {
			if (random.nextFloat() < effect.getSecond()) {
				user.addStatusEffect(effect.getFirst());
			}
		}
		FoodEvents.EATEN.emit(new EatenEvent(GenericFoodHandler.createContext()));

		GenericFoodHandler.reset();
	}
}
