package de.siphalor.capsaicin.api.event;

public interface EventRegistry<Event> {
	/**
	 * Registers a new unique listener to this event
	 * @param listener the listener
	 */
	void on(Listener<Event> listener);

	/**
	 * Emits the event to all registered listeners
	 * @param event the event
	 */
	void emit(Event event);

	@FunctionalInterface
	interface Listener<Event> {
		void handle(Event event);
	}
}
