package de.siphalor.capsaicin.impl.event;

import de.siphalor.capsaicin.api.event.EventRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

@ApiStatus.Internal
public class EventRegistryImpl<Event> implements EventRegistry<Event> {
	private final Set<Listener<Event>> listeners = Collections.newSetFromMap(new IdentityHashMap<>());

	@Override
	public void on(@NotNull Listener<Event> listener) {
		listeners.add(listener);
	}

	@Override
	public void emit(@NotNull Event event) {
		for (Listener<Event> listener : listeners) {
			try {
				listener.handle(event);
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}
	}
}
