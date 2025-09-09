package net.lopymine.patpat.modmenu.pipec.option;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
@SuperBuilder
public class DecimalOption<T extends Number> extends NumberOption<T> {

	private final float step;

	@Override
	public Supplier<T> getGetter() {
		Supplier<T> original = super.getGetter();
		return () -> round(original.get());
	}

	@Override
	public Consumer<T> getSetter() {
		Consumer<T> original = super.getSetter();
		return value -> original.accept(round(value));
	}

	@SuppressWarnings("unchecked")
	private T round(T value) {
		if (value instanceof Float) {
			return (T) Float.valueOf(Math.round(value.floatValue() / step) * step);
		}
		if (value instanceof Double) {
			return (T) Double.valueOf(Math.round(value.doubleValue() / step) * step);
		}
		throw new IllegalArgumentException("Unsupported type: " + value.getClass());
	}

}