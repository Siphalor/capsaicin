package de.siphalor.capsaicin.api.modifier;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public interface Modifiers<Value, Context> {
	/**
	 * Registers a new modifier with the given unique id and a priority. <br />
	 * Ordering of the modifiers is performed by their priorities (lower=earlier)
	 * and by the hash codes of their ids if their priorities match.
	 * This ensures a stable ordering across launches and versions.
	 *
	 * @param modifier The modifier
	 * @param id The unique id of this modifier
	 * @param priority The priority of this modifier, lower means earlier and higher means later
	 */
	void register(@NotNull Modifier<Value, Context> modifier, @NotNull Identifier id, int priority);
	/**
	 * Registers a new modifier with the given unique id and default priority (0). <br />
	 * Ordering of the modifiers is performed by their priorities (lower=earlier)
	 * and by the hash codes of their ids if their priorities match.
	 * This ensures a stable ordering across launches and versions.
	 *
	 * @param modifier The modifier
	 * @param id The unique id of this modifier
	 */
	default void register(@NotNull Modifier<Value, Context> modifier, @NotNull Identifier id) {
		register(modifier, id, 0);
	}

	@NotNull Value apply(@NotNull Value value, @NotNull Context context);
}
