package de.siphalor.capsaicin.impl.modifier;

import de.siphalor.capsaicin.api.modifier.Modifier;
import de.siphalor.capsaicin.api.modifier.Modifiers;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A modifier registry that, only allows one modifier per identifier.
 * @param <Value> The type of the value that is modified.
 * @param <Context> The type of the context that is used for the modification.
 */
@ApiStatus.Internal
public class UniqueModifiers<Value, Context> implements Modifiers<Value, Context> {
	private final Set<Identifier> registeredIds = new HashSet<>();
	private final SortedSet<Entry<Value, Context>> modifiers = new TreeSet<>();

	@Override
	public void register(Modifier<Value, Context> modifier, Identifier id, int priority) {
		if (!registeredIds.add(id)) {
			throw new IllegalArgumentException("Modifier with id " + id + " already registered!");
		}
		modifiers.add(new Entry<>(modifier, id, priority));
	}

	@Override
	public Value apply(Value value, Context context) {
		for (Entry<Value, Context> entry : modifiers) {
			try {
				value = entry.modifier().apply(value, context);
			} catch (Exception e) {
				if (entry.modifier().ignoreErrors()) {
					e.printStackTrace();
				} else {
					throw new RuntimeException("Error while applying modifier " + entry.id(), e);
				}
			}
		}
		return value;
	}

	private static class Entry<Value, Context> implements Comparable<Entry<Value, Context>> {
		private final Modifier<Value, Context> modifier;
		private final Identifier id;
		private final long idHash;
		private final int priority;

		private Entry(@NotNull Modifier<Value, Context> modifier, @NotNull Identifier id, int priority) {
			this.modifier = modifier;
			this.id = id;
			this.idHash = id.hashCode();
			this.priority = priority;
		}

		@Override
		public int compareTo(@NotNull UniqueModifiers.Entry<Value, Context> o) {
			int cmp = Integer.compare(priority, o.priority);
			if (cmp == 0) {
				return Long.compare(idHash, o.idHash);
			}
			return cmp;
		}

		public Modifier<Value, Context> modifier() {
			return modifier;
		}

		public Identifier id() {
			return id;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Entry<?, ?> entry = (Entry<?, ?>) o;
			return idHash == entry.idHash && priority == entry.priority;
		}

		@Override
		public int hashCode() {
			return Objects.hash(idHash, priority);
		}
	}
}
