package de.siphalor.capsaicin.impl.util;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class MutationDetectingList<T> extends AbstractList<T> {
	private final List<T> delegate;
	private final Runnable mutationCallback;

	@Override
	public T get(int index) {
		return delegate.get(index);
	}

	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public void add(int index, T element) {
		delegate.add(index, element);
		mutationCallback.run();
	}

	@Override
	public boolean addAll(@NotNull Collection<? extends T> c) {
		boolean superChanged = delegate.addAll(c);
		if (superChanged) {
			mutationCallback.run();
		}
		return superChanged;
	}

	@Override
	public T set(int index, T element) {
		T old = delegate.set(index, element);
		mutationCallback.run();
		return old;
	}

	@Override
	public T remove(int index) {
		T old = delegate.remove(index);
		mutationCallback.run();
		return old;
	}

	@Override
	public void clear() {
		delegate.clear();
		mutationCallback.run();
	}
}
