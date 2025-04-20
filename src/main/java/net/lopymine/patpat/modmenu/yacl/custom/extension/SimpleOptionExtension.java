package net.lopymine.patpat.modmenu.yacl.custom.extension;

//? if >=1.20.1 {

import dev.isxander.yacl3.api.controller.*;
import net.minecraft.text.Text;

import net.lopymine.patpat.utils.ModMenuUtils;
import net.lopymine.patpat.modmenu.yacl.custom.base.SimpleOption.Builder;
import net.lopymine.patpat.modmenu.yacl.custom.utils.EnumWithText;

import java.util.function.Function;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class SimpleOptionExtension {

	public static final Function<Boolean, Text> ENABLED_OR_DISABLED_FORMATTER = ModMenuUtils.getEnabledOrDisabledFormatter();

	//

	public static Builder<String> withController(Builder<String> builder, Object... ignored) {
		builder.getOptionBuilder().controller(StringControllerBuilder::create);
		return builder;
	}

	//

	public static <T extends Enum<T>&EnumWithText> Builder<T> withController(Builder<T> builder, Class<T> clazz) {
		builder.getOptionBuilder().controller((o) -> EnumControllerBuilder.create(o).enumClass(clazz).formatValue(EnumWithText::getText));
		return builder;
	}

	//

	public static Builder<Boolean> withController(Builder<Boolean> builder) {
		return withController(builder, ENABLED_OR_DISABLED_FORMATTER);
	}

	public static Builder<Boolean> withController(Builder<Boolean> builder, @Nullable Function<Boolean, Text> valueFormatter) {
		builder.getOptionBuilder().controller((o) -> {
			BooleanControllerBuilder b = BooleanControllerBuilder.create(o).coloured(true);
			if (valueFormatter != null) {
				b.formatValue(valueFormatter::apply);
			}
			return b;
		});
		return builder;
	}

	//

	public static Builder<Double> withController(Builder<Double> builder, double min, double max, double step) {
		return withController(builder, min, max, step, true, null);
	}

	public static Builder<Double> withController(Builder<Double> builder, double min, double max, double step, boolean slider) {
		return withController(builder, min, max, step, slider, null);
	}

	public static Builder<Double> withController(Builder<Double> builder, double min, double max, double step, boolean slider, @Nullable Function<Double, Text> valueFormatter) {
		builder.getOptionBuilder().controller((o) -> {
			ValueFormattableController<Double, ?> b = slider ?
					DoubleSliderControllerBuilder.create(o).range(min, max).step(step)
					:
					DoubleFieldControllerBuilder.create(o).range(min, max);
			if (valueFormatter != null) {
				b.formatValue(valueFormatter::apply);
			}
			return b;
		});
		return builder;
	}

	//

	public static Builder<Float> withController(Builder<Float> builder, float min, float max, float step) {
		return withController(builder, min, max, step, true, null);
	}

	public static Builder<Float> withController(Builder<Float> builder, float min, float max, float step, boolean slider) {
		return withController(builder, min, max, step, slider, null);
	}

	public static Builder<Float> withController(Builder<Float> builder, float min, float max, float step, boolean slider, @Nullable Function<Float, Text> valueFormatter) {
		builder.getOptionBuilder().controller((o) -> {
			ValueFormattableController<Float, ?> b = slider ?
					FloatSliderControllerBuilder.create(o).range(min, max).step(step)
					:
					FloatFieldControllerBuilder.create(o).range(min, max);
			if (valueFormatter != null) {
				b.formatValue(valueFormatter::apply);
			}
			return b;
		});
		return builder;
	}

	//

	public static Builder<Integer> withController(Builder<Integer> builder, int min, int max, int step) {
		return withController(builder, min, max, step, true, null);
	}

	public static Builder<Integer> withController(Builder<Integer> builder, int min, int max, int step, boolean slider) {
		return withController(builder, min, max, step, slider, null);
	}

	public static Builder<Integer> withController(Builder<Integer> builder, int min, int max, int step, boolean slider, @Nullable Function<Integer, Text> valueFormatter) {
		builder.getOptionBuilder().controller((o) -> {
			ValueFormattableController<Integer, ?> b = slider ?
					IntegerSliderControllerBuilder.create(o).range(min, max).step(step)
					:
					IntegerFieldControllerBuilder.create(o).range(min, max);
			if (valueFormatter != null) {
				b.formatValue(valueFormatter::apply);
			}
			return b;
		});
		return builder;
	}
}
//?}
