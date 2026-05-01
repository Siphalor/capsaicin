package de.siphalor.capsaicin.impl.mixin;

//- import com.llamalad7.mixinextras.sugar.Local;
import de.siphalor.capsaicin.impl.food.FoodHandler;
import net.minecraft.world.InteractionHand;
//- import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.TamableAnimal;
//- import net.minecraft.world.entity.animal.Cat;
//# if MC_VERSION_NUMBER >= 12105
//- import net.minecraft.world.entity.animal.wolf.Wolf;
//# else
//- import net.minecraft.world.entity.animal.Wolf;
//# end
import net.minecraft.world.entity.player.Player;
//- import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//- import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//- import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

//# if MC_VERSION_NUMBER >= 260100
@Mixin(TamableAnimal.class)
//# else
//- @Mixin({Wolf.class, Cat.class})
//# end
public class MixinFeedableEntities {
	//# if MC_VERSION_NUMBER >= 260100
	@Inject(method = "feed", at = @At("HEAD"))
	public void onFeedMob(
			Player player,
			InteractionHand hand,
			ItemStack stack,
			float healingFactor,
			float defaultHeal,
			CallbackInfo callbackInfo
	) {
	//# else
	//- @Inject(
	//- 		method = "mobInteract",
	//- 		at = @At(
	//- 				value = "INVOKE",
	//- 				//# if MC_VERSION_NUMBER >= 12005
	//- 				target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
	//- 				//# else
	//- 				target = "Lnet/minecraft/world/item/Item;getFoodProperties()Lnet/minecraft/world/food/FoodProperties;"
	//- 				//# end
	//- 		)
	//- )
	//- public void onFeedMob(
	//- 		Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, @Local ItemStack stack
	//- ) {
	//# end
		FoodHandler foodHandler = FoodHandler.INSTANCE.get();
		foodHandler.reset();
		foodHandler.withUser(player).withStack(stack);
	}

	//# if MC_VERSION_NUMBER >= 260100
	@Inject(method = "feed", at = @At("RETURN"))
	public void onMobFed(CallbackInfo callbackInfo) {
	//# else
	//- @Inject(
	//- 		method = "mobInteract",
	//- 		at = @At(
	//- 				value = "INVOKE",
	//- 				//# if MC_VERSION_NUMBER >= 12005
	//- 				target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;",
	//- 				//# else
	//- 				target = "Lnet/minecraft/world/item/Item;getFoodProperties()Lnet/minecraft/world/food/FoodProperties;",
	//- 				//# end
	//- 				shift = At.Shift.AFTER
	//- 		)
	//- )
	//- public void onMobFed(CallbackInfoReturnable<InteractionResult> cir) {
	//# end
		FoodHandler.INSTANCE.get().reset();
	}
}
