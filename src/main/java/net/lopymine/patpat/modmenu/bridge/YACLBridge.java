package net.lopymine.patpat.modmenu.bridge;

//? >=1.20.1 {

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import net.lopymine.patpat.modmenu.common.*;
import net.lopymine.patpat.modmenu.common.image.*;
import net.lopymine.patpat.modmenu.common.option.*;

import org.jetbrains.annotations.Nullable;

//? if yacl: >=3.6.6 {
import dev.isxander.yacl3.gui.image.ImageRenderer;
import net.minecraft.client.gui.GuiGraphics;
//?}

public class YACLBridge {

	private YACLBridge() {
		throw new IllegalStateException("Bridge class");
	}

	public static Screen getScreen(@Nullable Screen parent) {
		PatConfig patConfig = PatConfig.generate();
		YetAnotherConfigLib.Builder builder = YetAnotherConfigLib.createBuilder();
		builder
				.title(patConfig.getTitle())
				.save(patConfig.getOnSave());
		for (PatCategory category : patConfig.getCategories()) {
			builder.category(getCategory(category));
		}

		return builder.build().generateScreen(parent);
	}

	private static ConfigCategory getCategory(PatCategory category) {
		ConfigCategory.Builder builder = ConfigCategory.createBuilder();
		builder.name(category.getName());
		Component description = category.getDescription();
		if (description != null) {
			builder.tooltip(description);
		}
		for (PatElement element : category.getElements()) {
			if (element instanceof PatGroup group) {
				builder.group(getGroup(group));
				// Костыль, чтобы листовые параметры не попадали в группу, спасибо isXander
				group.getOptions().forEach(option -> {
					if (option instanceof PatListOption<?> patListOption) {
						builder.option(getOption(patListOption));
					}
				});
			} else if (element instanceof AbstractPatOption<?> option) {
				builder.option(getOption(option));
			}
		}
		return builder.build();
	}

	private static OptionGroup getGroup(PatGroup group) {
		OptionGroup.Builder builder = OptionGroup.createBuilder();
		builder.name(group.getName());
		builder.collapsed(group.isCollapsed());
		PatDescription description = group.getDescription();
		if (description != null) {
			builder.description(getDescription(description));
		}

		for (AbstractPatOption<?> option : group.getOptions()) {
			// Костыль, чтобы листовые параметры не попадали в группу, спасибо isXander
			if (option instanceof PatListOption<?>) {
				continue;
			}
			builder.option(getOption(option));
		}
		return builder.build();
	}

	@SuppressWarnings("unchecked,raw")
	private static <T> Option<T> getOption(AbstractPatOption<T> abstractOption) {
		if (abstractOption instanceof PatListOption<?> listOption) {
			return (Option<T>) getListOption(listOption);
		}
		PatOption<T> option = (PatOption<T>) abstractOption;

		Option.Builder<T> builder = Option.createBuilder();
		builder.name(option.getName());
		PatDescription description = option.getDescription();
		if (description != null) {
			builder.description(getDescription(option.getDescription()));
		}
		builder.binding(
				option.getDefaultValue(),
				option.getGetter(),
				option.getSetter()
		);

		if (option instanceof SliderNumberOption<?> sliderNumberOption) {
			addSliderNumberOption(builder, sliderNumberOption);
		} else if (option instanceof NumberOption<?> numberOption) {
			addNumberOption(builder, numberOption);
		} else if (option instanceof BooleanOption) {
			builder.controller(opt -> (ControllerBuilder<T>) BooleanControllerBuilder.create((Option<Boolean>) opt).coloured(true));
		} else if (option instanceof EnumOption enumOption) {
			// TODO: Исправить предупреждения о сырых использованиях параметризованных классов
			((Option.Builder<? extends Enum>) builder).controller(opt -> EnumControllerBuilder.create(opt)
					.enumClass(enumOption.getEnumClass())
					.formatValue(o -> (Component) enumOption
							.getEnumNameProvider()
							.apply(o)
					)
			);
		} else {
			throw new IllegalArgumentException("Unsupported Option type: " + option);
		}
		return builder.build();
	}

	@SuppressWarnings("unchecked")
	private static <T> void addSliderNumberOption(Option.Builder<T> builder, SliderNumberOption<?> sliderNumberOption) {
		builder.controller(opt -> {
			Class<?> type = sliderNumberOption.getType();
			if (type == Float.class) {
				SliderNumberOption<Float> sliderFloatOption = (SliderNumberOption<Float>) sliderNumberOption;
				return (ControllerBuilder<T>) FloatSliderControllerBuilder
						.create((Option<Float>) opt)
						.range(sliderFloatOption.getMin(), sliderFloatOption.getMax())
						.step(sliderFloatOption.getStep());
			}
			if (type == Long.class) {
				SliderNumberOption<Long> sliderLongOption = (SliderNumberOption<Long>) sliderNumberOption;
				return (ControllerBuilder<T>) LongSliderControllerBuilder
						.create((Option<Long>) opt)
						.range(sliderLongOption.getMin(), sliderLongOption.getMax())
						.step(sliderLongOption.getStep());
			}
			if (type == Double.class) {
				SliderNumberOption<Double> sliderDoubleOption = (SliderNumberOption<Double>) sliderNumberOption;
				return (ControllerBuilder<T>) DoubleSliderControllerBuilder
						.create((Option<Double>) opt)
						.range(sliderDoubleOption.getMin(), sliderDoubleOption.getMax())
						.step(sliderDoubleOption.getStep());
			}
			if (type == Integer.class) {
				SliderNumberOption<Integer> sliderIntegerOption = (SliderNumberOption<Integer>) sliderNumberOption;
				return (ControllerBuilder<T>) IntegerSliderControllerBuilder
						.create((Option<Integer>) opt)
						.range(sliderIntegerOption.getMin(), sliderIntegerOption.getMax())
						.step(sliderIntegerOption.getStep());
			}
			throw new IllegalArgumentException("Unsupported SliderNumberOption type: " + type);
		});
	}

	@SuppressWarnings("unchecked")
	private static <T> void addNumberOption(Option.Builder<T> builder, NumberOption<?> numberOption) {
		builder.controller(opt -> {
			Class<?> type = numberOption.getType();
			if (type == Float.class) {
				NumberOption<Float> floatOption = (NumberOption<Float>) numberOption;
				return (ControllerBuilder<T>) FloatFieldControllerBuilder
						.create((Option<Float>) opt)
						.range(floatOption.getMin(), floatOption.getMax());
			}
			if (type == Long.class) {
				NumberOption<Long> longOption = (NumberOption<Long>) numberOption;
				return (ControllerBuilder<T>) LongFieldControllerBuilder
						.create((Option<Long>) opt)
						.range(longOption.getMin(), longOption.getMax());
			}
			if (type == Double.class) {
				NumberOption<Double> doubleOption = (NumberOption<Double>) numberOption;
				return (ControllerBuilder<T>) DoubleFieldControllerBuilder
						.create((Option<Double>) opt)
						.range(doubleOption.getMin(), doubleOption.getMax());
			}
			if (type == Integer.class) {
				NumberOption<Integer> integerOption = (NumberOption<Integer>) numberOption;
				return (ControllerBuilder<T>) IntegerFieldControllerBuilder
						.create((Option<Integer>) opt)
						.range(integerOption.getMin(), integerOption.getMax());
			}

			throw new IllegalArgumentException("Unsupported NumberOption type: " + type);
		});
	}

	@SuppressWarnings("unchecked")
	private static <T> ListOption<T> getListOption(PatListOption<T> option) {
		ListOption.Builder<T> builder = ListOption.createBuilder();
		builder.name(option.getName());
		PatDescription description = option.getDescription();
		if (description != null) {
			builder.description(getDescription(option.getDescription()));
		}

		builder.initial(option.getInitial());
		builder.binding(
				option.getDefaultValue(),
				option.getGetter(),
				option.getSetter()
		);
		builder
				.minimumNumberOfEntries(option.getMinEntries())
				.maximumNumberOfEntries(option.getMaxEntries());

		Class<?> type = option.getType();
		if (type == String.class) {
			return builder.controller(opt -> (ControllerBuilder<T>) StringControllerBuilder.create((Option<String>) opt)).build();
		}

		throw new IllegalArgumentException("Unsupported ListOption type: " + type);
	}

	private static OptionDescription getDescription(PatDescription description) {
		OptionDescription.Builder builder = OptionDescription.createBuilder();
		builder.text(description.getText());
		AbstractPatImage abstractImage = description.getImage();
		if (abstractImage == null) {
			return builder.build();
		}

		if (abstractImage instanceof PatImage image) {
			switch (image.getType()) {
				case WEBP -> builder.webpImage(image.getResource());
				case IMAGE -> builder.image(image.getResource(), image.getWidth(), image.getHeight());
				default -> throw new IllegalArgumentException("Unsupported type for PatImage: " + image.getType());
			}
		}/*? if yacl: >=3.6.6 {*/ else if (abstractImage instanceof PatRenderer renderer) {
			builder.customImage(
					new ImageRenderer() {
						@Override
						public int render(GuiGraphics graphics, int x, int y, int renderWidth, float delta) {
							return renderer.getMethod().render(graphics, x, y, renderWidth, delta);
						}

						@Override
						public void close() {
							// NO-OP
						}
					}
			);
		}/*?}*/ else {
			throw new IllegalStateException("Value is not image: " + abstractImage);
		}
		return builder.build();
	}

}
//?}