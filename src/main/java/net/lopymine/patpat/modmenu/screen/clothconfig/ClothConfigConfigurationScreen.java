package net.lopymine.patpat.modmenu.screen.clothconfig;

import me.shedaniel.clothconfig2.api.*;
import me.shedaniel.clothconfig2.gui.entries.SubCategoryListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import net.lopymine.patpat.PatPat;
import net.lopymine.patpat.client.PatPatClient;
import net.lopymine.patpat.config.client.PatPatClientConfig;

import java.util.function.Function;

public class ClothConfigConfigurationScreen {

	public static final Function<Boolean, Text> ENABLED_OR_DISABLE_FORMATTER = state -> PatPat.text("modmenu.formatter.enable_or_disable." + state)
			.setStyle(Style.EMPTY.withFormatting(Boolean.TRUE.equals(state) ? Formatting.GREEN : Formatting.RED));

	private ClothConfigConfigurationScreen() {
		throw new IllegalStateException("Screen class");
	}


	public static Screen createScreen(Screen parent) {
		PatPatClientConfig config = PatPatClient.getConfig();

		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parent)
				.setTitle(PatPat.text("modmenu.title"));

		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
		ConfigCategory general = builder.getOrCreateCategory(PatPat.text("modmenu.title"));

		general.addEntry(getMainGroup(entryBuilder, config));
		general.addEntry(getResourcePackGroup(entryBuilder, config));
		general.addEntry(getSoundGroup(entryBuilder, config));
		general.addEntry(getVisualGroup(entryBuilder, config));
		general.addEntry(getServerGroup(entryBuilder, config));

		builder.setSavingRunnable(config::save);

		return builder.build();
	}

	private static SubCategoryListEntry getMainGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(PatPat.text("modmenu.main"));
		subcategory.add(
				entryBuilder.startBooleanToggle(PatPat.text("modmenu.main.option.enable_mod"), config.isModEnabled())
						.setTooltip(PatPat.text("modmenu.main.option.enable_mod.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.isModEnabled())
						.setSaveConsumer(config::setModEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(PatPat.text("modmenu.main.option.debug_log_enabled"), config.isDebugLogEnabled())
						.setTooltip(PatPat.text("modmenu.main.option.debug_log_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.isDebugLogEnabled())
						.setSaveConsumer(config::setDebugLogEnabled)
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getResourcePackGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(PatPat.text("modmenu.resource_packs"));
		subcategory.add(
				entryBuilder.startBooleanToggle(PatPat.text("modmenu.resource_packs.option.skip_outdated_animations_enabled"), config.isSkipOldAnimationsEnabled())
						.setTooltip(PatPat.text("modmenu.resource_packs.option.skip_outdated_animations_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.isSkipOldAnimationsEnabled())
						.setSaveConsumer(config::setSkipOldAnimationsEnabled)
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getSoundGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(PatPat.text("modmenu.sound"));
		subcategory.add(
				entryBuilder.startBooleanToggle(PatPat.text("modmenu.sound.option.enable_sounds"), config.isSoundsEnabled())
						.setTooltip(PatPat.text("modmenu.sound.option.enable_sounds.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.isSoundsEnabled())
						.setSaveConsumer(config::setSoundsEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(PatPat.text("modmenu.sound.option.sounds_volume"), config.getSoundsVolume())
						.setTooltip(PatPat.text("modmenu.sound.option.sounds_volume.description"))
						.setDefaultValue(PatPatClientConfig.DEFAULT.getSoundsVolume())
						.setMin(0)
						.setMax(1)
						.setSaveConsumer(value -> config.setSoundsVolume(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getVisualGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(PatPat.text("modmenu.visual"));
		subcategory.add(
				entryBuilder.startBooleanToggle(PatPat.text("modmenu.visual.option.hiding_nickname_enabled"), config.isNicknameHidingEnabled())
						.setTooltip(PatPat.text("modmenu.visual.option.hiding_nickname_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.isNicknameHidingEnabled())
						.setSaveConsumer(config::setNicknameHidingEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(PatPat.text("modmenu.visual.option.swing_hand_enabled"), config.isSwingHandEnabled())
						.setTooltip(PatPat.text("modmenu.visual.option.swing_hand_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.isSwingHandEnabled())
						.setSaveConsumer(config::setSwingHandEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(PatPat.text("modmenu.visual.option.hand_offset_x"), config.getAnimationOffsetX())
						.setTooltip(PatPat.text("modmenu.visual.option.hand_offset_x.description"))
						.setDefaultValue(PatPatClientConfig.DEFAULT.getAnimationOffsetX())
						.setMin(-5)
						.setMax(5)
						.setSaveConsumer(value -> config.setAnimationOffsetX(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(PatPat.text("modmenu.visual.option.hand_offset_y"), config.getAnimationOffsetY())
						.setTooltip(PatPat.text("modmenu.visual.option.hand_offset_y.description"))
						.setDefaultValue(PatPatClientConfig.DEFAULT.getAnimationOffsetY())
						.setMin(-5)
						.setMax(5)
						.setSaveConsumer(value -> config.setAnimationOffsetY(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(PatPat.text("modmenu.visual.option.hand_offset_z"), config.getAnimationOffsetZ())
						.setTooltip(PatPat.text("modmenu.visual.option.hand_offset_z.description"))
						.setDefaultValue(PatPatClientConfig.DEFAULT.getAnimationOffsetZ())
						.setMin(-5)
						.setMax(5)
						.setSaveConsumer(value -> config.setAnimationOffsetZ(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(PatPat.text("modmenu.visual.option.camera_shacking"), config.isCameraShackingEnabled())
						.setTooltip(PatPat.text("modmenu.visual.option.camera_shacking.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.isCameraShackingEnabled())
						.setSaveConsumer(config::setCameraShackingEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startFloatField(PatPat.text("modmenu.visual.option.pat_weight"), config.getPatWeight())
						.setTooltip(PatPat.text("modmenu.visual.option.pat_weight.description"))
						.setDefaultValue(PatPatClientConfig.DEFAULT.getPatWeight())
						.setMin(0)
						.setMax(1)
						.setSaveConsumer(value -> config.setPatWeight(Math.round(value * 100) / 100f))
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

	private static SubCategoryListEntry getServerGroup(ConfigEntryBuilder entryBuilder, PatPatClientConfig config) {
		SubCategoryBuilder subcategory = entryBuilder.startSubCategory(PatPat.text("modmenu.server"));
		subcategory.add(
				entryBuilder.startBooleanToggle(PatPat.text("modmenu.server.option.pat_me_enabled"), config.isPatMeEnabled())
						.setTooltip(PatPat.text("modmenu.server.option.pat_me_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.isPatMeEnabled())
						.setSaveConsumer(config::setPatMeEnabled)
						.build()
		);
		subcategory.add(
				entryBuilder.startBooleanToggle(PatPat.text("modmenu.server.option.bypass_server_resource_pack_priority_enabled"), config.isBypassServerResourcePackEnabled())
						.setTooltip(PatPat.text("modmenu.server.option.bypass_server_resource_pack_priority_enabled.description"))
						.setYesNoTextSupplier(ENABLED_OR_DISABLE_FORMATTER)
						.setDefaultValue(PatPatClientConfig.DEFAULT.isBypassServerResourcePackEnabled())
						.setSaveConsumer(config::setBypassServerResourcePackEnabled)
						.build()
		);
		subcategory.setExpanded(true);
		return subcategory.build();
	}

}
