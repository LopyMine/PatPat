package net.lopymine.patpat.modmenu.screen.yacl.simple;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import net.minecraft.text.Text;

import net.lopymine.patpat.utils.ModMenuUtils;

import java.util.function.*;

public class SimpleOptionsCollector {
	private final String groupId;

	private SimpleOptionsCollector(String groupId) {
		this.groupId = groupId;
	}

	public static SimpleOptionsCollector createBuilder(String groupId) {
		return new SimpleOptionsCollector(groupId);
	}

	public Option<Boolean> addBooleanOption(String optionId, boolean defValue, Supplier<Boolean> getter, Consumer<Boolean> setter, ValueFormatter<Boolean> formatter) {
		return this.option(Boolean.class, optionId, defValue, getter, setter)
				.controller((o) -> BooleanControllerBuilder.create(o).coloured(true).formatValue(formatter))
				.build();
	}

	public Option<Double> addDoubleOptionAsSlider(String optionId, double min, double max, double step, double defValue, Supplier<Double> getter, Consumer<Double> setter) {
		return this.option(Double.class, optionId, defValue, getter, setter)
				.controller((o) -> DoubleSliderControllerBuilder.create(o).range(min, max).step(step))
				.build();
	}

	private <C> Option.Builder<C> option(Class<C> ignored, String optionId, C defValue, Supplier<C> getter, Consumer<C> setter) {
		String optionKey = ModMenuUtils.getOptionKey(this.groupId, optionId);
		String optionDescription = ModMenuUtils.getDescriptionKey(optionKey);

		return Option.<C>createBuilder()
				.name(Text.translatable(optionKey))
				.description(OptionDescription.of(Text.translatable(optionDescription)))
				.binding(defValue, getter, setter);
	}

	public Option<?>[] collect(Option<?>... options) {
		return options;
	}
}
