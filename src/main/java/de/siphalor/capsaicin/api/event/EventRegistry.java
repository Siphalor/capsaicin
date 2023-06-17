package de.siphalor.capsaicin.api.event;

import org.jetbrains.annotations.NotNull;

public interface EventRegistry<Event> {
	/**
	 * Registers a new unique listener to this event
	 * @param listener the listener
	 */
	void on(@NotNull Listener<Event> listener);

	/**
	 * Emits the event to all registered listeners
	 * @param event the event
	 */
	void emit(@NotNull Event event);

	@FunctionalInterface
	interface Listener<Event> {
		void handle(@NotNull Event event);
	}
}
