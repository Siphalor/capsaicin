package de.siphalor.capsaicin.impl.food;

import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.api.food.*;
import de.siphalor.capsaicin.impl.food.properties.FoodPropertiesImpl;
import de.siphalor.capsaicin.impl.util.IItem;
import lombok.Getter;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@ApiStatus.Internal
public class FoodHandler implements DynamicFoodPropertiesAccess {
	public static final ThreadLocal<FoodHandler> INSTANCE = ThreadLocal.withInitial(FoodHandler::new);

	private @Nullable FoodProperties foodProperties;
	private int eatingTime;
	@Getter
	private @Nullable ItemStack stack;
	private @Nullable net.minecraft.world.food.FoodProperties stackFoodComponent;
	@Getter
	private @Nullable BlockState blockState;
	@Getter
	private @Nullable LivingEntity user;

	public static FoodHandler createInheriting() {
		FoodHandler parent = INSTANCE.get();
		FoodHandler foodHandler = new FoodHandler();
		foodHandler.foodProperties = parent.foodProperties;
		foodHandler.stack = parent.stack;
		foodHandler.stackFoodComponent = parent.stackFoodComponent;
		foodHandler.blockState = parent.blockState;
		foodHandler.user = parent.user;
		return foodHandler;
	}

	public @Nullable net.minecraft.world.food.FoodProperties getStackOriginalFoodComponent() {
		return stackFoodComponent;
	}

	public @NotNull FoodHandler withStack(@NotNull ItemStack stack) {
		this.blockState = null;

		this.stack = stack;
		Item item = stack.getItem();
		if (item instanceof CamoFoodItem camoFoodItem) {
			this.stack = camoFoodItem.getCamoFoodStack(stack, new CamoFoodContextImpl(user));
			if (this.stack == null) {
				return this;
			}
			item = this.stack.getItem();
		}
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
			eatingTime = item.getUseDuration(this.stack);
		} else {
			eatingTime = 0;
		}
		return this;
	}

	public @NotNull FoodHandler withBlockState(@NotNull BlockState blockState, @NotNull FoodProperties foodProperties) {
		this.stack = null;
		this.stackFoodComponent = null;

		this.blockState = blockState;
		this.foodProperties = foodProperties;
		this.eatingTime = 0;
		return this;
	}

	public @NotNull FoodHandler withUser(@NotNull LivingEntity user) {
		this.user = user;
		return this;
	}

	public void reset() {
		foodProperties = null;
		stack = null;
		stackFoodComponent = null;
		blockState = null;
		user = null;
	}

	public boolean isReady() {
		return stack != null || blockState != null;
	}

	public FoodContext createContext() {
		if (foodProperties == null) {
			return new FoodContextImpl(stack, blockState, 0, 0, user);
		}
		return new FoodContextImpl(stack, blockState, foodProperties.getHunger(), foodProperties.getSaturationModifier(), user);
	}

	@Override
	public @Nullable net.minecraft.world.food.FoodProperties getModifiedFoodComponent() {
		if (!isReady()) {
			return null;
		}

		@NotNull FoodProperties propertiesIn;
		if (foodProperties == null) {
			propertiesIn = new FoodPropertiesImpl(0, 0F, false, new ArrayList<>());
		} else {
			propertiesIn = new FoodPropertiesImpl(foodProperties.getHunger(), foodProperties.getSaturationModifier(), false, foodProperties.getStatusEffects());
		}
		@NotNull FoodProperties propertiesOut = getFoodProperties(propertiesIn);
		if (propertiesOut == propertiesIn && !propertiesIn.isChanged()) {
			if (stackFoodComponent != null) {
				return stackFoodComponent;
			}
			return new net.minecraft.world.food.FoodProperties.Builder()
					.nutrition(propertiesIn.getHunger())
					.saturationMod(propertiesIn.getSaturationModifier())
					.build();
		}

		@NotNull net.minecraft.world.food.FoodProperties.Builder builder = new net.minecraft.world.food.FoodProperties.Builder()
				.nutrition(propertiesOut.getHunger())
				.saturationMod(propertiesOut.getSaturationModifier());
		if (propertiesOut.isAlwaysEdible()) {
			builder.alwaysEat();
		}
		for (Pair<MobEffectInstance, Float> statusEffect : propertiesOut.getStatusEffects()) {
			builder.effect(statusEffect.getFirst(), statusEffect.getSecond());
		}
		if (stackFoodComponent != null) {
			if (stackFoodComponent.isFastFood()) {
				builder.fast();
			}
			if (stackFoodComponent.isMeat()) {
				builder.meat();
			}
		}
		return builder.build();
	}

	protected @NotNull FoodProperties getFoodProperties(@NotNull FoodProperties foodProperties) {
		return FoodModifications.PROPERTIES_MODIFIERS.apply(foodProperties, createContext());
	}

	@Override
	public int getModifiedEatingTime() {
		if (!isReady()) {
			return 0;
		}
		return FoodModifications.EATING_TIME_MODIFIERS.apply(eatingTime, createContext());
	}
}
