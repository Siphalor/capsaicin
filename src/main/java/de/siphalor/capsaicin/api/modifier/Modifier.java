package de.siphalor.capsaicin.api.modifier;

@FunctionalInterface
public interface Modifier<Value, Context> {
	Value apply(Value value, Context context);
}
