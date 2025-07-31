package net.lopymine.patpat.modmenu.clothconfig;

import me.shedaniel.clothconfig2.api.*;
import me.shedaniel.clothconfig2.gui.entries.SubCategoryListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.*;

import net.lopymine.patpat.client.config.PatPatClientConfig;
import net.lopymine.patpat.client.config.sub.*;
import net.lopymine.patpat.utils.*;

import java.util.function.Function;

public class ClothConfigConfigurationScreen {

	public static final Function<Boolean, Component> ENABLED_OR_DISABLED_FORMATTER = ModMenuUtils.getEnabledOrDisabledFormatterColored();

	public static final Style NAME_STYLE = Style.EMPTY.withBold(true);

	private ClothConfigConfigurationScreen() {
		throw new IllegalStateException("Screen class");
	}


	public static Screen createScreen(Screen parent) {
		PatPatClientConfig config = PatPatClientConfig.getInstance();
		PatPatClientConfig defConfig = PatPatClientConfig.getNewInstance();

		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parent)
				.setTitle(ModMenuUtils.getModTitle());

		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
		MutableComponent title = getCategoryName("general")
				.withStyle(NAME_STYLE);
		ConfigCategory general = builder.getOrCreateCategory(title);

		general.addEntry(getMainGroup(entryBuilder, config.getMainConfig(), defConfig.getMainConfig()));
		general.addEntry(getResourcePackGroup(entryBuilder, config.getResourcePacksConfig(), defConfig.getResourcePacksConfig()));
		general.addEntry(getSoundGroup(entryBuilder, config.getSoundsConfig(), defConfig.getSoundsConfig()));
		general.addEntry(getVisualGroup(entryBuilder, config.getVisualConfig(), defConfig.getVisualConfig()));
		general.addEntry(getMultiplayerGroup(entryBuilder, config.getMultiPlayerConfig(), defConfig.getMultiPlayerConfig()));
		//? if proxlib {
		general.addEntry(getProximityPacketsGroup(entryBuilder, config.getProximityPacketsConfig(), defConfig.getProximityPacketsConfig()));
		//?}

		builder.setSavingRunnable(config::saveAsync);

		return builder.build();
	}

	private static SubCategoryListEntry getMainGroup(ConfigEntryBuilder entryBuilder, PatPatClientMainConfig config, PatPatClientMainConfig defConfig) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(getGroupName("main"));
		subcategory.add(
				entryBuilder.startBooleanToggle(ModMenuUtils.getOptionName("enable_mod"), config.isModEnabled())
						.setTooltip(ModMenuUtils.getOptionDescription("enable_mod"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLED_FORMATTER)
						.setDefaultValue(defConfig.isModEnabled())
						.setSaveConsumer(config::setModEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(ModMenuUtils.getOptionName("debug_log_enabled"), config.isDebugLogEnabled())
						.setTooltip(ModMenuUtils.getOptionDescription("debug_log_enabled"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLED_FORMATTER)
						.setDefaultValue(defConfig.isDebugLogEnabled())
						.setSaveConsumer(config::setDebugLogEnabled)
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getResourcePackGroup(ConfigEntryBuilder entryBuilder, PatPatClientResourcePacksConfig config, PatPatClientResourcePacksConfig defConfig) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(getGroupName("custom_animations"));
		subcategory.add(
				entryBuilder.startBooleanToggle(ModMenuUtils.getOptionName("skip_outdated_animations_enabled"), config.isSkipOldAnimationsEnabled())
						.setTooltip(ModMenuUtils.getOptionDescription("skip_outdated_animations_enabled"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLED_FORMATTER)
						.setDefaultValue(defConfig.isSkipOldAnimationsEnabled())
						.setSaveConsumer(config::setSkipOldAnimationsEnabled)
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getSoundGroup(ConfigEntryBuilder entryBuilder, PatPatClientSoundsConfig config, PatPatClientSoundsConfig defConfig) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(getGroupName("sound"));
		subcategory.add(
				entryBuilder.startBooleanToggle(ModMenuUtils.getOptionName("enable_sounds"), config.isSoundsEnabled())
						.setTooltip(ModMenuUtils.getOptionDescription("enable_sounds"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLED_FORMATTER)
						.setDefaultValue(defConfig.isSoundsEnabled())
						.setSaveConsumer(config::setSoundsEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(ModMenuUtils.getOptionName("sounds_volume"), config.getSoundsVolume())
						.setTooltip(ModMenuUtils.getOptionDescription("sounds_volume"))
						.setDefaultValue(defConfig.getSoundsVolume())
						.setMin(0)
						.setMax(1)
						.setSaveConsumer(value -> config.setSoundsVolume(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getVisualGroup(ConfigEntryBuilder entryBuilder, PatPatClientVisualConfig config, PatPatClientVisualConfig defConfig) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(getGroupName("visual"));
		subcategory.add(
				entryBuilder.startBooleanToggle(ModMenuUtils.getOptionName("hiding_nickname_enabled"), config.isHidingNicknameEnabled())
						.setTooltip(ModMenuUtils.getOptionDescription("hiding_nickname_enabled"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLED_FORMATTER)
						.setDefaultValue(defConfig.isHidingNicknameEnabled())
						.setSaveConsumer(config::setHidingNicknameEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(ModMenuUtils.getOptionName("client_swing_hand_enabled"), config.isClientSwingHandEnabled())
						.setTooltip(ModMenuUtils.getOptionDescription("swing_hand_enabled"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLED_FORMATTER)
						.setDefaultValue(defConfig.isClientSwingHandEnabled())
						.setSaveConsumer(config::setClientSwingHandEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(ModMenuUtils.getOptionName("server_swing_hand_enabled"), config.isServerSwingHandEnabled())
						.setTooltip(ModMenuUtils.getOptionDescription("swing_hand_enabled"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLED_FORMATTER)
						.setDefaultValue(defConfig.isServerSwingHandEnabled())
						.setSaveConsumer(config::setServerSwingHandEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(ModMenuUtils.getOptionName("animation_offset_x"), config.getAnimationOffsets().getX())
						.setTooltip(ModMenuUtils.getOptionDescription("animation_offset_x"))
						.setDefaultValue(defConfig.getAnimationOffsets().getX())
						.setMin(-5)
						.setMax(5)
						.setSaveConsumer(value -> config.getAnimationOffsets().setX(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(ModMenuUtils.getOptionName("animation_offset_y"), config.getAnimationOffsets().getY())
						.setTooltip(ModMenuUtils.getOptionDescription("animation_offset_y"))
						.setDefaultValue(defConfig.getAnimationOffsets().getY())
						.setMin(-5)
						.setMax(5)
						.setSaveConsumer(value -> config.getAnimationOffsets().setY( Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(ModMenuUtils.getOptionName("animation_offset_z"), config.getAnimationOffsets().getZ())
						.setTooltip(ModMenuUtils.getOptionDescription("animation_offset_z"))
						.setDefaultValue(defConfig.getAnimationOffsets().getZ())
						.setMin(-5)
						.setMax(5)
						.setSaveConsumer(value -> config.getAnimationOffsets().setZ(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(ModMenuUtils.getOptionName("camera_shacking"), config.isCameraShackingEnabled())
						.setTooltip(ModMenuUtils.getOptionDescription("camera_shacking"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLED_FORMATTER)
						.setDefaultValue(defConfig.isCameraShackingEnabled())
						.setSaveConsumer(config::setCameraShackingEnabled)
						.build()
		);
		
		subcategory.add(
				entryBuilder.startFloatField(ModMenuUtils.getOptionName("pat_weight"), config.getPatWeight())
						.setTooltip(ModMenuUtils.getOptionDescription("pat_weight"))
						.setDefaultValue(defConfig.getPatWeight())
						.setMin(0)
						.setMax(1)
						.setSaveConsumer(value -> config.setPatWeight(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getMultiplayerGroup(ConfigEntryBuilder entryBuilder, PatPatClientMultiplayerConfig config, PatPatClientMultiplayerConfig defConfig) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(getGroupName("multiplayer"));
		subcategory.add(
				entryBuilder.startBooleanToggle(ModMenuUtils.getOptionName("pat_me_enabled"), config.isPatMeEnabled())
						.setTooltip(ModMenuUtils.getOptionDescription("pat_me_enabled"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLED_FORMATTER)
						.setDefaultValue(defConfig.isPatMeEnabled())
						.setSaveConsumer(config::setPatMeEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(ModMenuUtils.getOptionName("bypass_server_animations_priority_enabled"), config.isBypassServerResourcePackPriorityEnabled())
						.setTooltip(ModMenuUtils.getOptionDescription("bypass_server_animations_priority_enabled"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLED_FORMATTER)
						.setDefaultValue(defConfig.isBypassServerResourcePackPriorityEnabled())
						.setSaveConsumer(config::setBypassServerResourcePackEnabled)
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getProximityPacketsGroup(ConfigEntryBuilder entryBuilder, PatPatClientProximityPacketsConfig config, PatPatClientProximityPacketsConfig defConfig) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(getGroupName("proximity_packets"));
		subcategory.add(
				entryBuilder.startBooleanToggle(ModMenuUtils.getOptionName("proximity_packets_enabled").withStyle(ChatFormatting.RED), config.isProximityPacketsEnabled())
						.setTooltip(ModMenuUtils.getOptionDescriptionWithWarn("proximity_packets_enabled"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLED_FORMATTER)
						.setDefaultValue(defConfig.isProximityPacketsEnabled())
						.setSaveConsumer(config::setProximityPacketsEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startIntField(ModMenuUtils.getOptionName("max_packets_per_second"), config.getMaxPacketsPerSecond())
						.setTooltip(ModMenuUtils.getOptionDescription("max_packets_per_second"))
						.setDefaultValue(defConfig.getMaxPacketsPerSecond())
						.setMin(1)
						.setMax(50)
						.setSaveConsumer(config::setMaxPacketsPerSecond)
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	public static MutableComponent getCategoryName(String categoryId) {
		return ModMenuUtils.getName(ModMenuUtils.getCategoryKey(categoryId)).withStyle(NAME_STYLE);
	}

	public static MutableComponent getGroupName(String groupId) {
		return ModMenuUtils.getName(ModMenuUtils.getGroupKey(groupId)).withStyle(NAME_STYLE);
	}


}
