package de.siphalor.capsaicin.impl.food;

//- import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.api.food.*;
import de.siphalor.capsaicin.impl.food.properties.FoodPropertiesImpl;
//- import de.siphalor.capsaicin.impl.util.IItem;
import de.siphalor.capsaicin.impl.util.IItemStack;
import lombok.Getter;
//- import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//- import java.util.ArrayList;
import java.util.Collections;

@ApiStatus.Internal
public class FoodHandler implements DynamicFoodPropertiesAccess {
	public static final ThreadLocal<FoodHandler> INSTANCE = ThreadLocal.withInitial(FoodHandler::new);

	private @Nullable FoodProperties foodProperties;
	//# if MC_VERSION_NUMBER < 12005
	//- private int eatingTime;
	//# end
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
		//# if MC_VERSION_NUMBER >= 12005
		//noinspection ConstantValue
		if ((Object) this.stack instanceof IItemStack iStack) {
		//# else
		//- //noinspection ConstantValue
		//- if ((Object) item instanceof IItem iStack) {
		//# end
			stackFoodComponent = iStack.capsaicin$getVanillaFoodComponent();
			if (stackFoodComponent != null) {
				foodProperties = FoodPropertiesImpl.from(stackFoodComponent);
			}
		} else {
			stackFoodComponent = null;
		}
		//# if MC_VERSION_NUMBER < 12005
		//- if (item != null) {
		//- 	// Must not call stack.getMaxUseTime() here!
		//- 	// This would cause a stack overflow
		//- 	eatingTime = item.getUseDuration(this.stack);
		//- } else {
		//- 	eatingTime = 0;
		//- }
		//# end
		return this;
	}

	public @NotNull FoodHandler withBlockState(@NotNull BlockState blockState, @NotNull FoodProperties foodProperties) {
		this.stack = null;
		this.stackFoodComponent = null;

		this.blockState = blockState;
		this.foodProperties = foodProperties;
		//# if MC_VERSION_NUMBER < 12005
		//- this.eatingTime = 0;
		//# end
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
			//# if MC_VERSION_NUMBER >= 12005
			return new FoodContextImpl(stack, blockState, 0, 0, 0, user);
			//# else
			//- return new FoodContextImpl(stack, blockState, 0, 0, user);
			//# end
		}
		return new FoodContextImpl(
				stack,
				blockState,
				foodProperties.getHunger(),
				foodProperties.getSaturationModifier(),
				//# if MC_VERSION_NUMBER >= 12005
				foodProperties.getEatingTimeInSeconds(),
				//# end
				user
		);
	}

	@Override
	public @Nullable net.minecraft.world.food.FoodProperties getModifiedFoodComponent() {
		if (!isReady()) {
			return null;
		}

		@NotNull FoodProperties propertiesIn;
		if (foodProperties == null) {
			//# if MC_VERSION_NUMBER >= 12005
			propertiesIn = new FoodPropertiesImpl(0, 0F, 0F, false, Collections.emptyList());
			//# else
			//- propertiesIn = new FoodPropertiesImpl(0, 0F, false, new ArrayList<>());
			//# end
		} else {
			propertiesIn = new FoodPropertiesImpl(
					foodProperties.getHunger(),
					foodProperties.getSaturationModifier(),
					//# if MC_VERSION_NUMBER >= 12005
					foodProperties.getEatingTimeInSeconds(),
					//# end
					false,
					foodProperties.getStatusEffects()
			);
		}
		@NotNull FoodProperties propertiesOut = getFoodProperties(propertiesIn);
		if (propertiesOut == propertiesIn && !propertiesIn.isChanged()) {
			if (stackFoodComponent != null) {
				return stackFoodComponent;
			}
			//# if MC_VERSION_NUMBER >= 12005
			return new net.minecraft.world.food.FoodProperties(
					propertiesIn.getHunger(),
					propertiesIn.getSaturationModifier(),
					propertiesIn.isAlwaysEdible(),
					propertiesIn.getEatingTimeInSeconds(),
					propertiesIn.getStatusEffects()
			);
			//# else
			//- return new net.minecraft.world.food.FoodProperties.Builder()
			//- 		.nutrition(propertiesIn.getHunger())
			//- 		.saturationMod(propertiesIn.getSaturationModifier())
			//- 		.build();
			//# end
		}

		//# if MC_VERSION_NUMBER >= 12005
		return new net.minecraft.world.food.FoodProperties(
				propertiesOut.getHunger(),
				propertiesOut.getSaturationModifier(),
				propertiesOut.isAlwaysEdible(),
				propertiesOut.getEatingTimeInSeconds(),
				propertiesOut.getStatusEffects()
		);
		//# else
		//- @NotNull net.minecraft.world.food.FoodProperties.Builder builder = new net.minecraft.world.food.FoodProperties.Builder()
		//- 		.nutrition(propertiesOut.getHunger())
		//- 		.saturationMod(propertiesOut.getSaturationModifier());
		//- if (propertiesOut.isAlwaysEdible()) {
		//- 	builder.alwaysEat();
		//- }
		//- for (Pair<MobEffectInstance, Float> statusEffect : propertiesOut.getStatusEffects()) {
		//- 	builder.effect(statusEffect.getFirst(), statusEffect.getSecond());
		//- }
		//- if (stackFoodComponent != null) {
		//- 	if (stackFoodComponent.isFastFood()) {
		//- 		builder.fast();
		//- 	}
		//- 	if (stackFoodComponent.isMeat()) {
		//- 		builder.meat();
		//- 	}
		//- }
		//- return builder.build();
		//# end
	}

	protected @NotNull FoodProperties getFoodProperties(@NotNull FoodProperties foodProperties) {
		return FoodModifications.PROPERTIES_MODIFIERS.apply(foodProperties, createContext());
	}

	//# if MC_VERSION_NUMBER < 12005
	//- @Override
	//- public int getModifiedEatingTime() {
	//- 	if (!isReady()) {
	//- 		return 0;
	//- 	}
	//- 	return FoodModifications.EATING_TIME_MODIFIERS.apply(eatingTime, createContext());
	//- }
	//# end
}
