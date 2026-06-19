package de.siphalor.capsaicin.impl.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@ApiStatus.Internal
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NullableOptional<T> {
	private static final NullableOptional<?> EMPTY = new NullableOptional<>(true, null);

	@Getter
	private final boolean empty;
	private final @Nullable T value;

	public static <T> NullableOptional<T> empty() {
		//noinspection unchecked
		return (NullableOptional<T>) EMPTY;
	}

	public static <T> NullableOptional<T> of(T value) {
		return new NullableOptional<>(false, value);
	}

	public @Nullable T unwrap() {
		return value;
	}

	public void ifPresent(Consumer<@Nullable T> consumer) {
		if (!empty) {
			consumer.accept(value);
		}
	}
}
