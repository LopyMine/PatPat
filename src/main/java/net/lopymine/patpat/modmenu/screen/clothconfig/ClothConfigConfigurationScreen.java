package net.lopymine.patpat.modmenu.screen.clothconfig;

import me.shedaniel.clothconfig2.api.*;
import me.shedaniel.clothconfig2.gui.entries.SubCategoryListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.client.PatPatClientConfig;
import net.lopymine.patpat.utils.TextUtils;

import java.util.function.Function;

public class ClothConfigConfigurationScreen {

	public static final Function<Boolean, Text> ENABLED_OR_DISABLE_FORMATTER = state -> TextUtils.text("modmenu.formatter.enable_or_disable." + state)
			.setStyle(Style.EMPTY.withFormatting(Boolean.TRUE.equals(state) ? Formatting.GREEN : Formatting.RED));

	private ClothConfigConfigurationScreen() {
		throw new IllegalStateException("Screen class");
	}


	public static Screen createScreen(Screen parent) {
		PatPatClientConfig config = PatPatClient.getConfig();

		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parent)
				.setTitle(TextUtils.text("modmenu.title"));

		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
		ConfigCategory general = builder.getOrCreateCategory(TextUtils.text("modmenu.title"));

		general.addEntry(getMainGroup(entryBuilder, config));
		general.addEntry(getResourcePackGroup(entryBuilder, config));
		general.addEntry(getSoundGroup(entryBuilder, config));
		general.addEntry(getVisualGroup(entryBuilder, config));
		general.addEntry(getServerGroup(entryBuilder, config));

		builder.setSavingRunnable(config::save);

		return builder.build();
	}

	private static SubCategoryListEntry getMainGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(TextUtils.text("modmenu.main"));
		subcategory.add(
				entryBuilder.startBooleanToggle(TextUtils.text("modmenu.main.option.enable_mod"), config.getMainConfig().isModEnabled())
						.setTooltip(TextUtils.text("modmenu.main.option.enable_mod.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.getMainConfig().isModEnabled())
						.setSaveConsumer(config.getMainConfig()::setModEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(TextUtils.text("modmenu.main.option.debug_log_enabled"), config.getMainConfig().isDebugLogEnabled())
						.setTooltip(TextUtils.text("modmenu.main.option.debug_log_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.getMainConfig().isDebugLogEnabled())
						.setSaveConsumer(config.getMainConfig()::setDebugLogEnabled)
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getResourcePackGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(TextUtils.text("modmenu.resource_packs"));
		subcategory.add(
				entryBuilder.startBooleanToggle(TextUtils.text("modmenu.resource_packs.option.skip_outdated_animations_enabled"), config.getResourcePacksConfig().isSkipOldAnimationsEnabled())
						.setTooltip(TextUtils.text("modmenu.resource_packs.option.skip_outdated_animations_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.getResourcePacksConfig().isSkipOldAnimationsEnabled())
						.setSaveConsumer(config.getResourcePacksConfig()::setSkipOldAnimationsEnabled)
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getSoundGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(TextUtils.text("modmenu.sound"));
		subcategory.add(
				entryBuilder.startBooleanToggle(TextUtils.text("modmenu.sound.option.enable_sounds"), config.getSoundsConfig().isSoundsEnabled())
						.setTooltip(TextUtils.text("modmenu.sound.option.enable_sounds.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.getSoundsConfig().isSoundsEnabled())
						.setSaveConsumer(config.getSoundsConfig()::setSoundsEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(TextUtils.text("modmenu.sound.option.sounds_volume"), config.getSoundsConfig().getSoundsVolume())
						.setTooltip(TextUtils.text("modmenu.sound.option.sounds_volume.description"))
						.setDefaultValue(PatPatClientConfig.DEFAULT.getSoundsConfig().getSoundsVolume())
						.setMin(0)
						.setMax(1)
						.setSaveConsumer(value -> config.getSoundsConfig().setSoundsVolume(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getVisualGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(TextUtils.text("modmenu.visual"));
		subcategory.add(
				entryBuilder.startBooleanToggle(TextUtils.text("modmenu.visual.option.hiding_nickname_enabled"), config.getVisualConfig().isHidingNicknameEnabled())
						.setTooltip(TextUtils.text("modmenu.visual.option.hiding_nickname_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.getVisualConfig().isHidingNicknameEnabled())
						.setSaveConsumer(config.getVisualConfig()::setHidingNicknameEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(TextUtils.text("modmenu.visual.option.swing_hand_enabled"), config.getVisualConfig().isSwingHandEnabled())
						.setTooltip(TextUtils.text("modmenu.visual.option.swing_hand_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.getVisualConfig().isSwingHandEnabled())
						.setSaveConsumer(config.getVisualConfig()::setSwingHandEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(TextUtils.text("modmenu.visual.option.hand_offset_x"), config.getVisualConfig().getAnimationOffsets().x())
						.setTooltip(TextUtils.text("modmenu.visual.option.hand_offset_x.description"))
						.setDefaultValue(PatPatClientConfig.DEFAULT.getVisualConfig().getAnimationOffsets().x())
						.setMin(-5)
						.setMax(5)
						.setSaveConsumer(value -> config.getVisualConfig().getAnimationOffsets().x = Math.round(value * 100) / 100f)
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(TextUtils.text("modmenu.visual.option.hand_offset_y"), config.getVisualConfig().getAnimationOffsets().y())
						.setTooltip(TextUtils.text("modmenu.visual.option.hand_offset_y.description"))
						.setDefaultValue(PatPatClientConfig.DEFAULT.getVisualConfig().getAnimationOffsets().y())
						.setMin(-5)
						.setMax(5)
						.setSaveConsumer(value -> config.getVisualConfig().getAnimationOffsets().y = Math.round(value * 100) / 100f)
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(TextUtils.text("modmenu.visual.option.hand_offset_z"), config.getVisualConfig().getAnimationOffsets().z())
						.setTooltip(TextUtils.text("modmenu.visual.option.hand_offset_z.description"))
						.setDefaultValue(PatPatClientConfig.DEFAULT.getVisualConfig().getAnimationOffsets().z())
						.setMin(-5)
						.setMax(5)
						.setSaveConsumer(value -> config.getVisualConfig().getAnimationOffsets().z = Math.round(value * 100) / 100f)
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(TextUtils.text("modmenu.visual.option.camera_shacking"), config.getVisualConfig().isCameraShackingEnabled())
						.setTooltip(TextUtils.text("modmenu.visual.option.camera_shacking.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.getVisualConfig().isCameraShackingEnabled())
						.setSaveConsumer(config.getVisualConfig()::setCameraShackingEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(TextUtils.text("modmenu.visual.option.pat_weight"), config.getVisualConfig().getPatWeight())
						.setTooltip(TextUtils.text("modmenu.visual.option.pat_weight.description"))
						.setDefaultValue(PatPatClientConfig.DEFAULT.getVisualConfig().getPatWeight())
						.setMin(0)
						.setMax(1)
						.setSaveConsumer(value -> config.getVisualConfig().setPatWeight(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getServerGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(TextUtils.text("modmenu.server"));
		subcategory.add(
				entryBuilder.startBooleanToggle(TextUtils.text("modmenu.server.option.pat_me_enabled"), config.getServerConfig().isPatMeEnabled())
						.setTooltip(TextUtils.text("modmenu.server.option.pat_me_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.getServerConfig().isPatMeEnabled())
						.setSaveConsumer(config.getServerConfig()::setPatMeEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(TextUtils.text("modmenu.server.option.bypass_server_resource_pack_priority_enabled"), config.getServerConfig().isBypassServerResourcePackPriorityEnabled())
						.setTooltip(TextUtils.text("modmenu.server.option.bypass_server_resource_pack_priority_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.getServerConfig().isBypassServerResourcePackPriorityEnabled())
						.setSaveConsumer(config.getServerConfig()::setBypassServerResourcePackEnabled)
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

}
