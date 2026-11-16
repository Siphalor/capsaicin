package de.siphalor.capsaicin.impl.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import de.siphalor.capsaicin.impl.food.FoodHandler;
//- import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
//- import net.minecraft.client.renderer.ItemInHandRenderer;
//- import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//# if MC_VERSION_NUMBER >= 12005
//# if MC_VERSION_NUMBER >= 260300
@Mixin(FirstPersonHandsAndItemsRenderer.class)
//# else
//- @Mixin(ItemInHandRenderer.class)
//# end
public class MixinItemInHandRenderer {
	//# if MC_VERSION_NUMBER >= 260300
	@Shadow
	@Final
	private Minecraft minecraft;
	//# end

	@Inject(
			//# if MC_VERSION_NUMBER >= 260200
			method = "submitArmWithItem",
			//# else
			//- method = "renderArmWithItem",
			//# end
			at = @At("HEAD")
	)
	private void onRenderArmWithItem(
			//# if MC_VERSION_NUMBER >= 260300
			PlayerRenderState playerRenderState,
			FirstPersonHandsAndItemsRenderState handsAndItemsRenderState,
			//# else
			//- AbstractClientPlayer player,
			//# end
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
		//# if MC_VERSION_NUMBER >= 260300
		foodHandler.withUser(minecraft.player).withStack(stack);
		//# else
		//- foodHandler.withUser(player).withStack(stack);
		//# end
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
