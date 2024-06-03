package net.lopymine.patpat.modmenu.screen.clothconfig;

import me.shedaniel.clothconfig2.api.*;
import me.shedaniel.clothconfig2.gui.entries.SubCategoryListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.client.PatPatClientConfig;

import java.util.function.Function;

public class ClothConfigConfigurationScreen {

	public static final Function<Boolean, Text> ENABLED_OR_DISABLE_FORMATTER = state -> Text.translatable("patpat.modmenu.formatter.enable_or_disable." + state)
			.setStyle(Style.EMPTY.withFormatting(Boolean.TRUE.equals(state) ? Formatting.GREEN : Formatting.RED));

	private ClothConfigConfigurationScreen() {
		throw new IllegalStateException("Screen class");
	}


	public static Screen createScreen(Screen parent) {
		PatPatClientConfig config = PatPatClient.getConfig();

		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parent)
				.setTitle(Text.translatable("patpat.modmenu.title"));

		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
		ConfigCategory general = builder.getOrCreateCategory(Text.translatable("patpat.modmenu.title"));

		general.addEntry(getMainGroup(entryBuilder, config));
		general.addEntry(getSoundGroup(entryBuilder, config));
		general.addEntry(getVisualGroup(entryBuilder, config));
		general.addEntry(getServerGroup(entryBuilder, config));

		builder.setSavingRunnable(config::save);

		return builder.build();
	}

	private static SubCategoryListEntry getMainGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(Text.translatable("patpat.modmenu.main"));
		subcategory.add(
				entryBuilder.startBooleanToggle(Text.translatable("patpat.modmenu.main.option.enable_mod"), config.isModEnabled())
						.setTooltip(Text.translatable("patpat.modmenu.main.option.enable_mod.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.isModEnabled())
						.setSaveConsumer(config::setModEnabled)
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getSoundGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(Text.translatable("patpat.modmenu.sound"));
		subcategory.add(
				entryBuilder.startBooleanToggle(Text.translatable("patpat.modmenu.sound.option.enable_sounds"), config.isSoundsEnabled())
						.setTooltip(Text.translatable("patpat.modmenu.sound.option.enable_sounds.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.isSoundsEnabled())
						.setSaveConsumer(config::setSoundsEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(Text.translatable("patpat.modmenu.sound.option.sounds_volume"), (float) config.getSoundsVolume())
						.setTooltip(Text.translatable("patpat.modmenu.sound.option.sounds_volume.description"))
						.setDefaultValue((float) PatPatClientConfig.DEFAULT.getSoundsVolume())
						.setMin(0)
						.setMax(1)
						.setSaveConsumer(value -> config.setSoundsVolume(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getVisualGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(Text.translatable("patpat.modmenu.visual"));
		subcategory.add(
				entryBuilder.startBooleanToggle(Text.translatable("patpat.modmenu.visual.option.lowering_animation_enabled"), config.isLoweringAnimationEnabled())
						.setTooltip(Text.translatable("patpat.modmenu.visual.option.lowering_animation_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.isLoweringAnimationEnabled())
						.setSaveConsumer(config::setLoweringAnimationEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(Text.translatable("patpat.modmenu.visual.option.hiding_nickname_enabled"), config.isNicknameHidingEnabled())
						.setTooltip(Text.translatable("patpat.modmenu.visual.option.hiding_nickname_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.isNicknameHidingEnabled())
						.setSaveConsumer(config::setNicknameHidingEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(Text.translatable("patpat.modmenu.visual.option.swing_hand_enabled"), config.isSwingHandEnabled())
						.setTooltip(Text.translatable("patpat.modmenu.visual.option.swing_hand_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.isSwingHandEnabled())
						.setSaveConsumer(config::setSwingHandEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(Text.translatable("patpat.modmenu.visual.option.hand_offset_x"), (float) config.getAnimationOffsetX())
						.setTooltip(Text.translatable("patpat.modmenu.visual.option.hand_offset_x.description"))
						.setDefaultValue((float) PatPatClientConfig.DEFAULT.getAnimationOffsetX())
						.setMin(-5)
						.setMax(5)
						.setSaveConsumer(value -> config.setAnimationOffsetX(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(Text.translatable("patpat.modmenu.visual.option.hand_offset_y"), (float) config.getAnimationOffsetY())
						.setTooltip(Text.translatable("patpat.modmenu.visual.option.hand_offset_y.description"))
						.setDefaultValue((float) PatPatClientConfig.DEFAULT.getAnimationOffsetY())
						.setMin(-5)
						.setMax(5)
						.setSaveConsumer(value -> config.setAnimationOffsetY(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(Text.translatable("patpat.modmenu.visual.option.hand_offset_z"), (float) config.getAnimationOffsetZ())
						.setTooltip(Text.translatable("patpat.modmenu.visual.option.hand_offset_z.description"))
						.setDefaultValue((float) PatPatClientConfig.DEFAULT.getAnimationOffsetZ())
						.setMin(-5)
						.setMax(5)
						.setSaveConsumer(value -> config.setAnimationOffsetZ(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getServerGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(Text.translatable("patpat.modmenu.server"));
		subcategory.add(
				entryBuilder.startBooleanToggle(Text.translatable("patpat.modmenu.server.option.pat_me_enabled"), config.isPatMeEnabled())
						.setTooltip(Text.translatable("patpat.modmenu.server.option.pat_me_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.isPatMeEnabled())
						.setSaveConsumer(config::setPatMeEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(Text.translatable("patpat.modmenu.server.option.bypass_server_resource_pack_priority_enabled"), config.isBypassServerResourcePackPriorityEnabled())
						.setTooltip(Text.translatable("patpat.modmenu.server.option.bypass_server_resource_pack_priority_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.isBypassServerResourcePackPriorityEnabled())
						.setSaveConsumer(config::setBypassServerResourcePackPriorityEnabled)
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

}
