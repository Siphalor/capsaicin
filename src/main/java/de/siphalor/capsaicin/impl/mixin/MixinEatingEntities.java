package de.siphalor.capsaicin.impl.mixin;

import de.siphalor.capsaicin.impl.food.FoodHandler;
import net.minecraft.world.entity.LivingEntity;
//- import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//# if MC_VERSION_NUMBER >= 12100
@Mixin(LivingEntity.class)
//# else
//- // In these versions, the Player class completely overrides the eat method
//- @Mixin({LivingEntity.class, Player.class})
//# end
public class MixinEatingEntities {
	//# if MC_VERSION_NUMBER >= 12100
	@Inject(
			method = "eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
			at = @At("HEAD")
	)
	//# else
	//- @Inject(method = "eat", at = @At("HEAD"))
	//# end
	public void onEatFood(Level world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		FoodHandler foodHandler = FoodHandler.INSTANCE.get();
		foodHandler.reset();
		foodHandler.withUser((LivingEntity) (Object) this).withStack(stack);
	}
}
