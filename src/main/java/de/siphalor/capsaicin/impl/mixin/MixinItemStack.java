package de.siphalor.capsaicin.impl.mixin;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;

@ApiStatus.Internal
@Mixin(ItemStack.class)
public abstract class MixinItemStack {
	// This is a dummy class, the actual modifications are done via ASM in the mixin config
}
