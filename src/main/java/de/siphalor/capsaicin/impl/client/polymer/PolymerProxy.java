package de.siphalor.capsaicin.impl.client.polymer;

import eu.pb4.polymer.api.item.PolymerItemUtils;
import eu.pb4.polymer.impl.client.InternalClientRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class PolymerProxy {
	public static boolean isPolymerItem(ItemStack stack) {
		Identifier polymerId = PolymerItemUtils.getPolymerIdentifier(stack);
		if (polymerId != null) {
			return InternalClientRegistry.ITEMS.get(polymerId) != null;
		} else {
			return false;
		}
	}
}
