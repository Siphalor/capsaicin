package de.siphalor.capsaicin.api.modifier;

import net.minecraft.util.Identifier;

public interface Modifiers<Value, Context> {
	void register(Modifier<Value, Context> modifier, Identifier id, int priority);
	default void register(Modifier<Value, Context> modifier, Identifier id) {
		register(modifier, id, 0);
	}

	Value apply(Value value, Context context);
}
