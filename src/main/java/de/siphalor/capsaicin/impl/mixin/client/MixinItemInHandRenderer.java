package de.siphalor.capsaicin.impl.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import de.siphalor.capsaicin.impl.food.FoodHandler;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
//- import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//# if MC_VERSION_NUMBER >= 12005
@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {
	@Inject(
			//# if MC_VERSION_NUMBER >= 260200
			method = "submitArmWithItem",
			//# else
			//- method = "renderArmWithItem",
			//# end
			at = @At("HEAD")
	)
	private void onRenderArmWithItem(
			AbstractClientPlayer player,
			float partialTick,
			float pitch,
			InteractionHand hand,
			float swingProgress,
			ItemStack stack,
			float equippedProgress,
			PoseStack poseStack,
			//# if MC_VERSION_NUMBER >= 12109
			SubmitNodeCollector nodeCollector,
			//# else
			//- MultiBufferSource buffer,
			//# end
			int packedLight,
			CallbackInfo ci
	) {
		FoodHandler foodHandler = FoodHandler.INSTANCE.get();
		foodHandler.reset();
		foodHandler.withUser(player).withStack(stack);
	}

	@Inject(
			//# if MC_VERSION_NUMBER >= 260200
			method = "submitArmWithItem",
			//# else
			//- method = "renderArmWithItem",
			//# end
			at = @At("TAIL")
	)
	private void onRenderArmWithItemTail(CallbackInfo ci) {
		FoodHandler.INSTANCE.get().reset();
	}
}
//# end
