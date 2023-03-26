package de.siphalor.capsaicin.impl.mixin;

import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.api.food.FoodEvents;
import de.siphalor.capsaicin.impl.food.FoodHandler;
import de.siphalor.capsaicin.impl.food.event.EatenEvent;
import de.siphalor.capsaicin.impl.food.properties.FoodPropertiesImpl;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
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
		FoodHandler foodHandler = FoodHandler.INSTANCE.get();
		foodHandler.reset();
		foodHandler.withUser(player);
		foodHandler.withBlockState(state, new FoodPropertiesImpl(2, 0.1F, false, new ArrayList<>()));
	}

	@Redirect(method = "tryEat", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;add(IF)V"))
	private static void eat(HungerManager hungerManager, int hunger, float saturationModifier) {
		FoodHandler foodHandler = FoodHandler.INSTANCE.get();
		FoodComponent foodComponent = foodHandler.getModifiedFoodComponent();

		if (foodComponent != null) {
			LivingEntity user = foodHandler.getUser();
			Random random = user.getRandom();
			hungerManager.add(foodComponent.getHunger(), foodComponent.getSaturationModifier());
			for (Pair<StatusEffectInstance, Float> effect : foodComponent.getStatusEffects()) {
				if (random.nextFloat() < effect.getSecond()) {
					user.addStatusEffect(effect.getFirst());
				}
			}
			FoodEvents.EATEN.emit(new EatenEvent(foodHandler.createContext()));
		}

		foodHandler.reset();
	}
}
