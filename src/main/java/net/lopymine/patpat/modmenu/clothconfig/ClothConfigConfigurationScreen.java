package net.lopymine.patpat.modmenu.clothconfig;

import me.shedaniel.clothconfig2.api.*;
import me.shedaniel.clothconfig2.gui.entries.SubCategoryListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.utils.TextUtils;

import java.util.function.Function;

public class ClothConfigConfigurationScreen {

	public static final Function<Boolean, Text> ENABLED_OR_DISABLE_FORMATTER = state -> TextUtils.text("modmenu.formatter.enable_or_disable." + state)
			.setStyle(Style.EMPTY.withFormatting(Boolean.TRUE.equals(state) ? Formatting.GREEN : Formatting.RED));

	private ClothConfigConfigurationScreen() {
		throw new IllegalStateException("Screen class");
	}


	public static Screen createScreen(Screen parent) {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		PatPatClientConfig defConfig = PatPatClientConfig.getNewInstance();

		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parent)
				.setTitle(TextUtils.text("modmenu.title"));

		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
		ConfigCategory general = builder.getOrCreateCategory(TextUtils.text("modmenu.title"));

		general.addEntry(getMainGroup(entryBuilder, config, defConfig));
		general.addEntry(getResourcePackGroup(entryBuilder, config, defConfig));
		general.addEntry(getSoundGroup(entryBuilder, config, defConfig));
		general.addEntry(getVisualGroup(entryBuilder, config, defConfig));
		general.addEntry(getServerGroup(entryBuilder, config, defConfig));

		builder.setSavingRunnable(config::saveAsync);

		return builder.build();
	}

	private static SubCategoryListEntry getMainGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config, PatPatClientConfig defConfig) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(TextUtils.text("modmenu.main"));
		subcategory.add(
				entryBuilder.startBooleanToggle(TextUtils.text("modmenu.main.option.enable_mod"), config.getMainConfig().isModEnabled())
						.setTooltip(TextUtils.text("modmenu.main.option.enable_mod.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(defConfig.getMainConfig().isModEnabled())
						.setSaveConsumer(config.getMainConfig()::setModEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(TextUtils.text("modmenu.main.option.debug_log_enabled"), config.getMainConfig().isDebugLogEnabled())
						.setTooltip(TextUtils.text("modmenu.main.option.debug_log_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(defConfig.getMainConfig().isDebugLogEnabled())
						.setSaveConsumer(config.getMainConfig()::setDebugLogEnabled)
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getResourcePackGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config, PatPatClientConfig defConfig) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(TextUtils.text("modmenu.resource_packs"));
		subcategory.add(
				entryBuilder.startBooleanToggle(TextUtils.text("modmenu.resource_packs.option.skip_outdated_animations_enabled"), config.getResourcePacksConfig().isSkipOldAnimationsEnabled())
						.setTooltip(TextUtils.text("modmenu.resource_packs.option.skip_outdated_animations_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(defConfig.getResourcePacksConfig().isSkipOldAnimationsEnabled())
						.setSaveConsumer(config.getResourcePacksConfig()::setSkipOldAnimationsEnabled)
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getSoundGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config, PatPatClientConfig defConfig) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(TextUtils.text("modmenu.sound"));
		subcategory.add(
				entryBuilder.startBooleanToggle(TextUtils.text("modmenu.sound.option.enable_sounds"), config.getSoundsConfig().isSoundsEnabled())
						.setTooltip(TextUtils.text("modmenu.sound.option.enable_sounds.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(defConfig.getSoundsConfig().isSoundsEnabled())
						.setSaveConsumer(config.getSoundsConfig()::setSoundsEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(TextUtils.text("modmenu.sound.option.sounds_volume"), config.getSoundsConfig().getSoundsVolume())
						.setTooltip(TextUtils.text("modmenu.sound.option.sounds_volume.description"))
						.setDefaultValue(defConfig.getSoundsConfig().getSoundsVolume())
						.setMin(0)
						.setMax(1)
						.setSaveConsumer(value -> config.getSoundsConfig().setSoundsVolume(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getVisualGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config, PatPatClientConfig defConfig) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(TextUtils.text("modmenu.visual"));
		subcategory.add(
				entryBuilder.startBooleanToggle(TextUtils.text("modmenu.visual.option.hiding_nickname_enabled"), config.getVisualConfig().isHidingNicknameEnabled())
						.setTooltip(TextUtils.text("modmenu.visual.option.hiding_nickname_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(defConfig.getVisualConfig().isHidingNicknameEnabled())
						.setSaveConsumer(config.getVisualConfig()::setHidingNicknameEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(TextUtils.text("modmenu.visual.option.swing_hand_enabled"), config.getVisualConfig().isSwingHandEnabled())
						.setTooltip(TextUtils.text("modmenu.visual.option.swing_hand_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(defConfig.getVisualConfig().isSwingHandEnabled())
						.setSaveConsumer(config.getVisualConfig()::setSwingHandEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(TextUtils.text("modmenu.visual.option.hand_offset_x"), config.getVisualConfig().getAnimationOffsets().getX())
						.setTooltip(TextUtils.text("modmenu.visual.option.hand_offset_x.description"))
						.setDefaultValue(defConfig.getVisualConfig().getAnimationOffsets().getX())
						.setMin(-5)
						.setMax(5)
						.setSaveConsumer(value -> config.getVisualConfig().getAnimationOffsets().setX(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(TextUtils.text("modmenu.visual.option.hand_offset_y"), config.getVisualConfig().getAnimationOffsets().getY())
						.setTooltip(TextUtils.text("modmenu.visual.option.hand_offset_y.description"))
						.setDefaultValue(defConfig.getVisualConfig().getAnimationOffsets().getY())
						.setMin(-5)
						.setMax(5)
						.setSaveConsumer(value -> config.getVisualConfig().getAnimationOffsets().setY( Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(TextUtils.text("modmenu.visual.option.hand_offset_z"), config.getVisualConfig().getAnimationOffsets().getZ())
						.setTooltip(TextUtils.text("modmenu.visual.option.hand_offset_z.description"))
						.setDefaultValue(defConfig.getVisualConfig().getAnimationOffsets().getZ())
						.setMin(-5)
						.setMax(5)
						.setSaveConsumer(value -> config.getVisualConfig().getAnimationOffsets().setZ(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(TextUtils.text("modmenu.visual.option.camera_shacking"), config.getVisualConfig().isCameraShackingEnabled())
						.setTooltip(TextUtils.text("modmenu.visual.option.camera_shacking.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(defConfig.getVisualConfig().isCameraShackingEnabled())
						.setSaveConsumer(config.getVisualConfig()::setCameraShackingEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(TextUtils.text("modmenu.visual.option.pat_weight"), config.getVisualConfig().getPatWeight())
						.setTooltip(TextUtils.text("modmenu.visual.option.pat_weight.description"))
						.setDefaultValue(defConfig.getVisualConfig().getPatWeight())
						.setMin(0)
						.setMax(1)
						.setSaveConsumer(value -> config.getVisualConfig().setPatWeight(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getServerGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config, PatPatClientConfig defConfig) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(TextUtils.text("modmenu.server"));
		subcategory.add(
				entryBuilder.startBooleanToggle(TextUtils.text("modmenu.server.option.pat_me_enabled"), config.getServerConfig().isPatMeEnabled())
						.setTooltip(TextUtils.text("modmenu.server.option.pat_me_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(defConfig.getServerConfig().isPatMeEnabled())
						.setSaveConsumer(config.getServerConfig()::setPatMeEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(TextUtils.text("modmenu.server.option.bypass_server_resource_pack_priority_enabled"), config.getServerConfig().isBypassServerResourcePackPriorityEnabled())
						.setTooltip(TextUtils.text("modmenu.server.option.bypass_server_resource_pack_priority_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(defConfig.getServerConfig().isBypassServerResourcePackPriorityEnabled())
						.setSaveConsumer(config.getServerConfig()::setBypassServerResourcePackEnabled)
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

}
