package de.siphalor.capsaicin.impl.mixin;

import de.siphalor.capsaicin.impl.util.IItemStack;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
//- import org.spongepowered.asm.mixin.injection.Inject;

@ApiStatus.Internal
@Mixin(ItemStack.class)
public abstract class MixinItemStack /*# if MC_VERSION_NUMBER >= 12005 */implements IItemStack/*# end */ {
	//# if MC_VERSION_NUMBER >= 12005
	@Shadow
	public abstract DataComponentMap getComponents();

	@Override
	public FoodProperties capsaicin$getVanillaFoodComponent() {
		return getComponents().get(DataComponents.FOOD);
	}

	//# end
}
