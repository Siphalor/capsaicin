package de.siphalor.capsaicin.api.modifier;

/**
 * A modifier that may perform changes to a value based on a context.
 * @param <Value> The class of the value
 * @param <Context> The class of the operation context
 */
@FunctionalInterface
public interface Modifier<Value, Context> {
	/**
	 * Apply the modifications to the value and return an updated (or new) version.
	 * @param value The input value
	 * @param context Context for this modification
	 * @return An updated or new value based on the input and context
	 */
	Value apply(Value value, Context context);
}
