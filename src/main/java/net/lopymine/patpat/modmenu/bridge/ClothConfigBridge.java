package net.lopymine.patpat.modmenu.bridge;

import me.shedaniel.clothconfig2.api.*;
import me.shedaniel.clothconfig2.gui.entries.SubCategoryListEntry;
import me.shedaniel.clothconfig2.impl.builders.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

import net.lopymine.patpat.modmenu.common.*;
import net.lopymine.patpat.modmenu.common.option.*;
import net.lopymine.patpat.utils.ModMenuUtils;

import java.util.function.Function;
import org.jetbrains.annotations.Nullable;

public class ClothConfigBridge {

	public static final Function<Boolean, Component> ENABLED_OR_DISABLED_FORMATTER = ModMenuUtils.getEnabledOrDisabledFormatterColored();

	private ClothConfigBridge() {
		throw new IllegalStateException("Bridge class");
	}

	public static Screen getScreen(@Nullable Screen parent) {
		PatConfig patConfig = PatConfig.generate();
		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parent)
				.setTitle(patConfig.getTitle())
				.setSavingRunnable(patConfig.getOnSave());
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
		for (PatCategory category : patConfig.getCategories()) {
			ConfigCategory configCategory = builder.getOrCreateCategory(category.getName());
			addCategory(category, configCategory, entryBuilder);
		}
		return builder.build();
	}

	private static void addCategory(PatCategory category, ConfigCategory configCategory, ConfigEntryBuilder entryBuilder) {
		Component description = category.getDescription();
		if (description != null) {
			configCategory.setDescription(new FormattedText[]{description});
		}

		for (PatElement element : category.getElements()) {
			if (element instanceof PatGroup group) {
				configCategory.addEntry(getGroup(group, entryBuilder));
			} else if (element instanceof AbstractPatOption<?> option) {
				configCategory.addEntry(getOption(option, entryBuilder));
			}
		}
	}

	private static SubCategoryListEntry getGroup(PatGroup group, ConfigEntryBuilder entryBuilder) {

		SubCategoryBuilder subCategoryBuilder = entryBuilder.startSubCategory(group.getName());
		PatDescription description = group.getDescription();
		if (description != null) {
			subCategoryBuilder.setTooltip(description.getText());
		}
		subCategoryBuilder.setExpanded(!group.isCollapsed());
		for (AbstractPatOption<?> option : group.getOptions()) {
			subCategoryBuilder.add(getOption(option, entryBuilder));
		}
		return subCategoryBuilder.build();
	}

	@SuppressWarnings("unchecked")
	private static AbstractConfigListEntry<?> getOption(AbstractPatOption<?> option, ConfigEntryBuilder entryBuilder) {
		if (option instanceof PatListOption<?> listOption) {
			return getListOption(listOption, entryBuilder);
		}

		PatDescription description = option.getDescription();

		if (option instanceof SliderNumberOption<?> sliderNumberOption) {
			Class<?> type = sliderNumberOption.getType();
			if (type == Integer.class) {
				SliderNumberOption<Integer> sliderIntegerOption = (SliderNumberOption<Integer>) sliderNumberOption;
				IntSliderBuilder sliderBuilder = entryBuilder.startIntSlider(option.getName(), sliderIntegerOption.getGetter().get(), sliderIntegerOption.getMin(), sliderIntegerOption.getMax())
						.setDefaultValue(sliderIntegerOption.getDefaultValue())
						.setSaveConsumer(sliderIntegerOption.getSetter());
				if (description != null) {
					sliderBuilder.setTooltip(description.getText());
				}
				return sliderBuilder.build();
			}
			if (type == Long.class) {
				SliderNumberOption<Long> sliderLongOption = (SliderNumberOption<Long>) sliderNumberOption;
				LongSliderBuilder sliderBuilder = entryBuilder.startLongSlider(option.getName(), sliderLongOption.getGetter().get(), sliderLongOption.getMin(), sliderLongOption.getMax())
						.setDefaultValue(sliderLongOption.getDefaultValue())
						.setSaveConsumer(sliderLongOption.getSetter());
				if (description != null) {
					sliderBuilder.setTooltip(description.getText());
				}
				return sliderBuilder.build();
			}
			if (type == Float.class) {
				// В ClothConfig нет слайдеров с вещественными числами
				NumberOption<Float> floatNumberOption = (NumberOption<Float>) sliderNumberOption;
				FloatFieldBuilder fieldBuilder = entryBuilder.startFloatField(option.getName(), floatNumberOption.getGetter().get())
						.setMin(floatNumberOption.getMin())
						.setMax(floatNumberOption.getMax())
						.setDefaultValue(floatNumberOption.getDefaultValue())
						.setSaveConsumer(floatNumberOption.getSetter());
				if (description != null) {
					fieldBuilder.setTooltip(description.getText());
				}
				return fieldBuilder.build();
			}
			throw new IllegalArgumentException("Unsupported SliderNumberOption type: " + type);
		} else if (option instanceof NumberOption<?> numberOption) {
			Class<?> type = numberOption.getType();
			if (type == Float.class) {
				NumberOption<Float> floatNumberOption = (NumberOption<Float>) numberOption;
				FloatFieldBuilder fieldBuilder = entryBuilder.startFloatField(option.getName(), floatNumberOption.getGetter().get())
						.setMin(floatNumberOption.getMin())
						.setMax(floatNumberOption.getMax())
						.setDefaultValue(floatNumberOption.getDefaultValue())
						.setSaveConsumer(floatNumberOption.getSetter());
				if (description != null) {
					fieldBuilder.setTooltip(description.getText());
				}
				return fieldBuilder.build();
			}
			if (type == Double.class) {
				NumberOption<Double> doubleNumberOption = (NumberOption<Double>) numberOption;
				DoubleFieldBuilder fieldBuilder = entryBuilder.startDoubleField(option.getName(), doubleNumberOption.getGetter().get())
						.setMin(doubleNumberOption.getMin())
						.setMax(doubleNumberOption.getMax())
						.setDefaultValue(doubleNumberOption.getDefaultValue())
						.setSaveConsumer(doubleNumberOption.getSetter());
				if (description != null) {
					fieldBuilder.setTooltip(description.getText());
				}
				return fieldBuilder.build();
			}
			if (type == Integer.class) {
				NumberOption<Integer> integerNumberOption = (NumberOption<Integer>) numberOption;
				IntFieldBuilder fieldBuilder = entryBuilder.startIntField(option.getName(), integerNumberOption.getGetter().get())
						.setMin(integerNumberOption.getMin())
						.setMax(integerNumberOption.getMax())
						.setDefaultValue(integerNumberOption.getDefaultValue())
						.setSaveConsumer(integerNumberOption.getSetter());
				if (description != null) {
					fieldBuilder.setTooltip(description.getText());
				}
				return fieldBuilder.build();
			}
			if (type == Long.class) {
				NumberOption<Long> longNumberOption = (NumberOption<Long>) numberOption;
				LongFieldBuilder fieldBuilder = entryBuilder.startLongField(option.getName(), longNumberOption.getGetter().get())
						.setMin(longNumberOption.getMin())
						.setMax(longNumberOption.getMax())
						.setDefaultValue(longNumberOption.getDefaultValue())
						.setSaveConsumer(longNumberOption.getSetter());
				if (description != null) {
					fieldBuilder.setTooltip(description.getText());
				}
				return fieldBuilder.build();
			}

			throw new IllegalArgumentException("Unsupported NumberOption type: " + type);
		} else if (option instanceof BooleanOption booleanOption) {
			BooleanToggleBuilder toggleBuilder = entryBuilder.startBooleanToggle(option.getName(), booleanOption.getGetter().get())
					.setDefaultValue(booleanOption.getDefaultValue())
					.setYesNoTextSupplier(ENABLED_OR_DISABLED_FORMATTER)
					.setSaveConsumer(booleanOption.getSetter());
			if (description != null) {
				toggleBuilder.setTooltip(description.getText());
			}
			return toggleBuilder.build();
		} else if (option instanceof EnumOption enumOption) {
			// TODO: Исправить предупреждения о сырых использованиях параметризованных классов
			EnumSelectorBuilder<?> enumBuilder = entryBuilder.startEnumSelector(option.getName(), enumOption.getEnumClass(), (Enum<?>) enumOption.getGetter().get())
					.setEnumNameProvider(enumOption.getEnumNameProvider())
					.setDefaultValue((Enum<?>) enumOption.getDefaultValue())
					.setSaveConsumer(enumOption.getSetter());
			if (description != null) {
				enumBuilder.setTooltip(description.getText());
			}
			return enumBuilder.build();
		}
		throw new IllegalStateException("Unexpected PatOption: " + option);
	}

	@SuppressWarnings("unchecked")
	private static AbstractConfigListEntry<?> getListOption(PatListOption<?> option, ConfigEntryBuilder entryBuilder) {
		PatDescription description = option.getDescription();
		Class<?> type = option.getType();
		if (type == String.class) {
			PatListOption<String> stringOption = (PatListOption<String>) option;
			StringListBuilder stringList = entryBuilder.startStrList(option.getName(), stringOption.getGetter().get())
					.setDefaultValue(stringOption.getDefaultValue())
					.setSaveConsumer(stringOption.getSetter());
			if (description != null) {
				stringList.setTooltip(description.getText());
			}
			return stringList.build();
		}
		throw new IllegalStateException("Unexpected PatListOption: " + option);
	}


}
