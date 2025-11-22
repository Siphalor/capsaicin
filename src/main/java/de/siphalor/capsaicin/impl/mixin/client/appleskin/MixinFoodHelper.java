package de.siphalor.capsaicin.impl.mixin.client.appleskin;

//- import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//- import de.siphalor.capsaicin.impl.util.IItem;
import de.siphalor.capsaicin.impl.util.IItemStack;
import net.minecraft.core.component.DataComponentType;
//- import net.minecraft.world.food.FoodProperties;
//- import net.minecraft.world.item.Item;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//- import org.spongepowered.asm.mixin.injection.Redirect;
import squeek.appleskin.helpers.FoodHelper;

@Mixin(FoodHelper.class)
public class MixinFoodHelper {
	private MixinFoodHelper() {}

	//# if MC_VERSION_NUMBER >= 12005
	@WrapOperation(method = "getDefaultFoodValues", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/ItemStack;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"
	))
	private static <T> T getFoodComponent(
			ItemStack stack,
			DataComponentType<T> componentType,
			T defaultValue,
			Operation<T> original
	) {
		//noinspection ConstantValue
		if ((Object) stack instanceof IItemStack iStack) {
			if (componentType == DataComponents.FOOD) {
				//noinspection unchecked
				return (T) iStack.capsaicin$getVanillaFoodComponent();
			}
			// Here we would also redirect consumable data for effects when AppleSkin comes around to allow modifying it
		}
		return original.call(stack, componentType, defaultValue);
	}
	//# else
	//- @Redirect(method = { "canConsume", "getDefaultFoodValues" }, at = @At(
	//- 		value = "INVOKE",
	//- 		target = "Lnet/minecraft/world/item/Item;getFoodProperties()Lnet/minecraft/world/food/FoodProperties;")
	//- )
	//- private static FoodProperties getFoodComponent(Item item) {
	//- 	if (item instanceof IItem iitem) {
	//- 		return iitem.capsaicin$getVanillaFoodComponent();
	//- 	}
	//- 	return item.getFoodProperties();
	//- }
	//# end
}
