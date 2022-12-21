package de.siphalor.capsaicin.api.modifier;

public interface Modifier<Value, Context> {
	Value apply(Value value, Context context);
}
