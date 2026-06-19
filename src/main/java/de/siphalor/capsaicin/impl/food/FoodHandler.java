package de.siphalor.capsaicin.impl.food;

//- import com.mojang.datafixers.util.Pair;
import de.siphalor.capsaicin.api.food.*;
import de.siphalor.capsaicin.impl.food.properties.ConsumablePropertiesImpl;
import de.siphalor.capsaicin.impl.food.properties.FoodPropertiesImpl;
//- import de.siphalor.capsaicin.impl.util.IItem;
import de.siphalor.capsaicin.impl.util.IItemStack;
import lombok.Getter;
//- import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

//- import java.util.ArrayList;
//- import java.util.Optional;

@ApiStatus.Internal
public class FoodHandler implements DynamicFoodPropertiesAccess {
	public static final ThreadLocal<FoodHandler> INSTANCE = ThreadLocal.withInitial(FoodHandler::new);

	//# if MC_VERSION_NUMBER < 12005
	//- private int eatingTime;
	//# end
	@Getter
	private @Nullable ItemStack stack;
	private @Nullable net.minecraft.world.food.FoodProperties stackFoodComponent;
	private @Nullable FoodProperties foodProperties;
	//# if MC_VERSION_NUMBER >= 12102
	private @Nullable Consumable stackConsumableComponent;
	private @Nullable ConsumableProperties consumableProperties;
	//# end
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
		//# if MC_VERSION_NUMBER >= 12102
		foodHandler.stackConsumableComponent = parent.stackConsumableComponent;
		//# end
		foodHandler.blockState = parent.blockState;
		foodHandler.user = parent.user;
		return foodHandler;
	}

	public @Nullable net.minecraft.world.food.FoodProperties getStackOriginalFoodComponent() {
		return stackFoodComponent;
	}

	//# if MC_VERSION_NUMBER >= 12102
	public @Nullable Consumable getStackOriginalConsumableComponent() {
		return stackConsumableComponent;
	}
	//# end

	public @NotNull FoodHandler withStack(@NotNull ItemStack stack) {
		this.blockState = null;

		this.stack = stack;
		Item item = stack.getItem();
		if (item instanceof CamoFoodItem camoFoodItem) {
			this.stack = camoFoodItem.getCamoFoodStack(stack, new CamoFoodContextImpl(user));
			if (this.stack != null) {
				item = this.stack.getItem();
			}
		}

		foodProperties = null;
		//# if MC_VERSION_NUMBER >= 12005
		//noinspection ConstantValue
		if ((Object) this.stack instanceof IItemStack iStack) {
		//# else
		//- //noinspection ConstantValue
		//- if ((Object) item instanceof IItem iStack) {
		//# end
			this.stackFoodComponent = iStack.capsaicin$getVanillaFoodComponent();
			if (this.stackFoodComponent != null) {
				foodProperties = FoodPropertiesImpl.from(this.stackFoodComponent);
			}
			//# if MC_VERSION_NUMBER >= 12102
			this.stackConsumableComponent = iStack.capsaicin$getVanillaConsumableComponent();
			if (this.stackConsumableComponent != null) {
				consumableProperties = ConsumablePropertiesImpl.from(this.stackConsumableComponent);
			}
			//# end
		} else {
			this.stackFoodComponent = null;
			/*# if MC_VERSION_NUMBER >= 12102 */this.stackConsumableComponent = null;/*# end */
		}
		//# if MC_VERSION_NUMBER < 12005
		//- if (item != null) {
		//- 	// Must not call stack.getMaxUseTime() here!
		//- 	// This would cause a stack overflow
		//- 	this.eatingTime = item.getUseDuration(this.stack);
		//- } else {
		//- 	this.eatingTime = 0;
		//- }
		//# end
		return this;
	}

	public @NotNull FoodHandler withBlockState(@NotNull BlockState blockState, @NotNull FoodProperties foodProperties) {
		this.stack = null;
		this.stackFoodComponent = null;

		this.blockState = blockState;
		this.foodProperties = foodProperties;
		//# if MC_VERSION_NUMBER >= 12102
		this.consumableProperties = new ConsumablePropertiesImpl(0F, Collections.emptyList());
		//# elif MC_VERSION_NUMBER < 12005
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
		//# if MC_VERSION_NUMBER >= 12102
		stackConsumableComponent = null;
		//# end
		blockState = null;
		user = null;
	}

	public boolean isReady() {
		return stack != null || blockState != null;
	}

	public FoodContext createContext() {
		if (foodProperties == null) {
			//# if MC_VERSION_NUMBER >= 12005
			return new FoodContextImpl(stack, blockState, 0, 0, user);
			//# else
			//- return new FoodContextImpl(stack, blockState, 0, 0, user);
			//# end
		}
		return new FoodContextImpl(
				stack,
				blockState,
				foodProperties.getHunger(),
				foodProperties.getSaturationModifier(),
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
			//# if MC_VERSION_NUMBER >= 12102
			propertiesIn = new FoodPropertiesImpl(0, 0F, false);
			//# else
			//- propertiesIn = new FoodPropertiesImpl(0, 0F, false, Collections.emptyList());
			//# end
		} else {
			propertiesIn = FoodPropertiesImpl.copy(foodProperties);
		}
		FoodContext foodContext = createContext();
		@NotNull FoodProperties propertiesOut = calcFoodProperties(propertiesIn, foodContext);
		boolean changed = propertiesIn != propertiesOut || propertiesOut.isChanged();

		//# if MC_VERSION_NUMBER >= 12005 && MC_VERSION_NUMBER < 12102
		//- float eatingTimeSecondsIn = Optional.ofNullable(stackFoodComponent)
		//- 		.map(net.minecraft.world.food.FoodProperties::eatSeconds)
		//- 		.orElse(0F);
		//- float eatingTimeSecondsOut = calcModifiedEatingTimeSeconds(eatingTimeSecondsIn, foodContext);
		//- changed |= eatingTimeSecondsIn != eatingTimeSecondsOut;
		//# end

		if (!changed) {
			if (stackFoodComponent != null) {
				return stackFoodComponent;
			}
			//# if MC_VERSION_NUMBER >= 12005
			return new net.minecraft.world.food.FoodProperties(
					propertiesIn.getHunger(),
					propertiesIn.getSaturationModifier(),
					propertiesIn.isAlwaysEdible()
					//# if MC_VERSION_NUMBER < 12102
					//- /*# if MC_VERSION_NUMBER >= 12005 */, eatingTimeSecondsIn/*# end */
					//- /*# if MC_VERSION_NUMBER >= 12100 */, Optional.empty()/*# end */
					//- , propertiesIn.getStatusEffects()
					//# end
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
				propertiesOut.isAlwaysEdible()
				//# if MC_VERSION_NUMBER < 12102
				//- , eatingTimeSecondsOut,
				//- //# if MC_VERSION_NUMBER >= 12100
				//- Optional.ofNullable(stackFoodComponent).flatMap(net.minecraft.world.food.FoodProperties::usingConvertsTo),
				//- //# end
				//- propertiesOut.getStatusEffects()
				//# end
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

	protected @NotNull FoodProperties calcFoodProperties(
			FoodProperties foodProperties,
			FoodContext foodContext
	) {
		return FoodModifications.PROPERTIES_MODIFIERS.apply(foodProperties, foodContext);
	}

	//# if MC_VERSION_NUMBER >= 12102
	@Override
	public @Nullable Consumable getModifiedConsumableComponent() {
		if (!isReady()) {
			return null;
		}

		@NotNull ConsumableProperties propertiesIn;
		if (consumableProperties == null) {
			propertiesIn = new ConsumablePropertiesImpl(0F, Collections.emptyList());
		} else {
			propertiesIn = ConsumablePropertiesImpl.copy(consumableProperties);
		}
		FoodContext foodContext = createContext();
		@NotNull ConsumableProperties propertiesOut = calcConsumableProperties(propertiesIn, foodContext);

		if (propertiesIn == propertiesOut && !propertiesOut.isChanged()) {
			return stackConsumableComponent;
		}

		if (stackConsumableComponent != null) {
			return new Consumable(
					propertiesOut.getConsumeSeconds(),
					stackConsumableComponent.animation(),
					stackConsumableComponent.sound(),
					stackConsumableComponent.hasConsumeParticles(),
					propertiesOut.getOnConsumeEffects()
			);
		} else {
			return new Consumable(
					propertiesOut.getConsumeSeconds(),
					ItemUseAnimation.EAT,
					SoundEvents.GENERIC_EAT,
					true,
					propertiesOut.getOnConsumeEffects()
			);
		}
	}

	protected @NotNull ConsumableProperties calcConsumableProperties(
			ConsumableProperties consumableProperties,
			FoodContext foodContext
	) {
		return FoodModifications.CONSUMABLE_MODIFIERS.apply(consumableProperties, foodContext);
	}
	//# end

	//# if MC_VERSION_NUMBER >= 12102
	//# elif MC_VERSION_NUMBER >= 12005
	//- public float calcModifiedEatingTimeSeconds(float eatingTime, FoodContext foodContext) {
		//- return FoodModifications.EATING_TIME_SECONDS_MODIFIERS.apply(eatingTime, foodContext);
	//- }
	//# else
	//- @Override
	//- public int getModifiedEatingTime() {
	//- 	if (!isReady()) {
	//- 		return 0;
	//- 	}
	//- 	return FoodModifications.EATING_TIME_MODIFIERS.apply(eatingTime, createContext());
	//- }
	//# end
}
