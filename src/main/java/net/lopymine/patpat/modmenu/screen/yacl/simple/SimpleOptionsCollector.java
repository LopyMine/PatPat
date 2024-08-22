package net.lopymine.patpat.modmenu.screen.yacl.simple;

//? >=1.20 {
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.OptionDescription.Builder;
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

	public Option<Boolean> getBooleanOption(String optionId, boolean defValue, Supplier<Boolean> getter, Consumer<Boolean> setter, ValueFormatter<Boolean> formatter, SimpleContent content) {
		return this.getOption(optionId, defValue, getter, setter, content)
				.controller(o -> BooleanControllerBuilder.create(o)
						.coloured(true)
						.formatValue(formatter))
				.build();
	}

	public Option<Float> getFloatOptionAsSlider(String optionId, float min, float max, float step, float defValue, Supplier<Float> getter, Consumer<Float> setter, SimpleContent content) {
		return this.getOption(optionId, defValue, getter, setter, content)
				.controller(o -> FloatSliderControllerBuilder.create(o).range(min, max).step(step))
				.build();
	}

	public Option<Boolean> getBooleanOption(String optionId, boolean defValue, Supplier<Boolean> getter, Consumer<Boolean> setter, ValueFormatter<Boolean> formatter) {
		return this.getOption(optionId, defValue, getter, setter, SimpleContent.NONE)
				.controller(o -> BooleanControllerBuilder.create(o)
						.coloured(true)
						.formatValue(formatter)
				)
				.build();
	}

	public Option<Float> getFloatOptionAsSlider(String optionId, float min, float max, float step, float defValue, Supplier<Float> getter, Consumer<Float> setter) {
		return this.getOption(optionId, defValue, getter, setter, SimpleContent.NONE)
				.controller(o -> FloatSliderControllerBuilder.create(o).range(min, max).step(step))
				.build();
	}

	private <C> Option.Builder<C> getOption(String optionId, C defValue, Supplier<C> getter, Consumer<C> setter, SimpleContent content) {
		String optionKey = ModMenuUtils.getOptionKey(this.groupId, optionId);
		String optionDescription = ModMenuUtils.getDescriptionKey(optionKey);

		Builder descriptionBuilder = OptionDescription.createBuilder().text(Text.translatable(optionDescription));
		if (content == SimpleContent.IMAGE) {
			descriptionBuilder.image(ModMenuUtils.getContentId(content, optionId), 600, 600);
		}
		if (content == SimpleContent.WEBP) {
			descriptionBuilder.webpImage(ModMenuUtils.getContentId(content, optionId));
		}
		return Option.<C>createBuilder()
				.name(Text.translatable(optionKey))
				.description(descriptionBuilder.build())
				.binding(defValue, getter, setter);
	}

	public Option<?>[] collect(Option<?>... options) {
		return options;
	}

	public <T> Option<T> getIf(Option<T> option, BooleanSupplier condition) {
		if (condition.getAsBoolean()) {
			return option;
		}
		return null;
	}
}
//?}

