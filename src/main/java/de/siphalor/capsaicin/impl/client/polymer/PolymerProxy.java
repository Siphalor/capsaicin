package de.siphalor.capsaicin.impl.client.polymer;

import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import eu.pb4.polymer.core.impl.client.InternalClientRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class PolymerProxy {
	public static boolean isPolymerItem(ItemStack stack) {
		ResourceLocation polymerId = PolymerItemUtils.getPolymerIdentifier(stack);
		if (polymerId != null) {
			return InternalClientRegistry.ITEMS.get(polymerId) != null;
		} else {
			return false;
		}
	}
}
