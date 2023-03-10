package de.siphalor.capsaicin.impl.food;

import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.api.food.FoodContext;
import de.siphalor.capsaicin.api.food.FoodModifications;
import de.siphalor.capsaicin.api.food.FoodProperties;
import de.siphalor.capsaicin.impl.food.properties.FoodPropertiesImpl;
import de.siphalor.capsaicin.impl.util.IItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;

@ApiStatus.Internal
public class FoodHandler {
	public static final ThreadLocal<FoodHandler> INSTANCE = ThreadLocal.withInitial(FoodHandler::new);

	private FoodProperties foodProperties;
	private int eatingTime;
	private ItemStack stack;
	private FoodComponent stackFoodComponent;
	private BlockState blockState;
	private LivingEntity user;

	public ItemStack getStack() {
		return stack;
	}

	public BlockState getBlockState() {
		return blockState;
	}

	public LivingEntity getUser() {
		return user;
	}

	public void withStack(ItemStack stack) {
		this.stack = stack;
		Item item = stack.getItem();
		if (item instanceof IItem iItem) {
			stackFoodComponent = iItem.capsaicin$getVanillaFoodComponent();
			if (stackFoodComponent != null) {
				foodProperties = FoodPropertiesImpl.from(stackFoodComponent);
			}
		} else {
			stackFoodComponent = null;
		}
		if (item != null) {
			// Must not call stack.getMaxUseTime() here!
			// This would cause a stack overflow
			eatingTime = item.getMaxUseTime(stack);
		} else {
			eatingTime = 0;
		}
	}

	public void withBlockState(BlockState blockState, FoodProperties foodProperties) {
		this.blockState = blockState;
		this.foodProperties = foodProperties;
		this.eatingTime = 0;
	}

	public void withUser(LivingEntity user) {
		this.user = user;
	}

	public void reset() {
		foodProperties = null;
		stack = null;
		stackFoodComponent = null;
		blockState = null;
		user = null;
	}

	public boolean canApply() {
		return stack != null || blockState != null;
	}

	public FoodContext createContext() {
		if (canApply()) {
			return new FoodContextImpl(stack, blockState, foodProperties.getHunger(), foodProperties.getSaturationModifier(), user);
		}
		return null;
	}

	public FoodComponent getFoodComponent() {
		FoodProperties propertiesIn;
		if (stack != null && stackFoodComponent != null) {
			propertiesIn = FoodProperties.from(stackFoodComponent);
		} else {
			propertiesIn = new FoodPropertiesImpl(foodProperties.getHunger(), foodProperties.getSaturationModifier(), false, new ArrayList<>());
		}
		FoodProperties propertiesOut = getFoodProperties(propertiesIn);
		if (propertiesOut == propertiesIn && !propertiesIn.isChanged()) {
			if (stackFoodComponent != null) {
				return stackFoodComponent;
			}
			return new FoodComponent.Builder()
					.hunger(propertiesIn.getHunger())
					.saturationModifier(propertiesIn.getSaturationModifier())
					.build();
		}

		FoodComponent.Builder builder = new FoodComponent.Builder()
				.hunger(propertiesOut.getHunger())
				.saturationModifier(propertiesOut.getSaturationModifier());
		if (propertiesOut.isAlwaysEdible()) {
			builder.alwaysEdible();
		}
		for (Pair<StatusEffectInstance, Float> statusEffect : propertiesOut.getStatusEffects()) {
			builder.statusEffect(statusEffect.getFirst(), statusEffect.getSecond());
		}
		if (stackFoodComponent.isSnack()) {
			builder.snack();
		}
		if (stackFoodComponent.isMeat()) {
			builder.meat();
		}
		return builder.build();
	}

	public FoodProperties getFoodProperties(FoodProperties foodProperties) {
		return FoodModifications.PROPERTIES_MODIFIERS.apply(foodProperties, createContext());
	}

	public int getEatingTime() {
		return FoodModifications.EATING_TIME_MODIFIERS.apply(eatingTime, createContext());
	}
}
